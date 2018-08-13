package br.com.andrewribeiro.ribrest.controller;

import br.com.andrewribeiro.ribrest.dao.CRUDCenterImpl;
import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.services.FlowContainer;
import br.com.andrewribeiro.ribrest.services.cdi.hk2.RequestContext;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ContainerRequest;
import br.com.andrewribeiro.ribrest.services.miner.interfaces.Miner;
import br.com.andrewribeiro.ribrest.dao.interfaces.PersistenceCenter;
import br.com.andrewribeiro.ribrest.services.command.Command;
import br.com.andrewribeiro.ribrest.services.dispatcher.Dispatcher;
import br.com.andrewribeiro.ribrest.services.miner.factory.interfaces.MinerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrew Ribeiro
 */
public class Facade {

    public Facade(String entity) {
        this.modelClassName = entity;
    }

    @Inject
    FlowContainer flowContainer;

    @Inject
    Dispatcher dispatcher;

    @Inject
    MinerFactory minerFactory;


    @Inject
    private ServiceLocator serviceLocator;

    ContainerRequest containterRequest;
    private final String modelClassName;
    private List<Command> beforeCommands, afterCommands;
    private Class currentDao;

    public Response process() {
        Miner miner = null;
        Response response = Response.serverError().build();
        try {
            Class classModel = Class.forName(modelClassName);
            miner = minerFactory.getMinerInstance(classModel);
            serviceLocator.inject(miner);
            flowContainer.setMiner(miner);
            flowContainer.initModelInstance(classModel);
            miner.extractDataFromRequest(containterRequest);
            runBeforeCommands();
            run();
            runAfterCommands();
        } catch (Exception e) {
            handleProcessExceptions(e);
        } finally {
            try {
                response = dispatcher.send();
            } catch (Exception e) {
                response = Response.serverError().build();
            } finally {
                serviceLocator.preDestroy(this);
            }
        }

        return response;
    }

    private void runBeforeCommands() {
        runCommands(beforeCommands);
    }

    private void runAfterCommands() {
        runCommands(afterCommands);
    }

    private void runCommands(List<Command> commands) {
        commands.forEach(command -> {
            try {
                serviceLocator.inject(command);
                command.execute();
            } catch (RibrestDefaultException rde) {
                setStatusWhenCommandsFail();
                throw new RuntimeException(rde.getError());
            } catch (Exception ex) {
                setStatusWhenCommandsFail();
                throw new RuntimeException(ex.getMessage());
            }
        });
    }

    private void run() throws RibrestDefaultException {
        PersistenceCenter pc = new CRUDCenterImpl();
        pc.setCurrentDAOClass(currentDao);
        serviceLocator.inject(pc);
        pc.perform();
    }

    private void setErrorOutput(String cause) {
        flowContainer.getResult().setStatus(flowContainer.getResult().getStatus().equals(Response.Status.OK)
                ? Response.Status.INTERNAL_SERVER_ERROR : flowContainer.getResult().getStatus());
        flowContainer.getResult().setCause(cause);
    }

    private void setStatusWhenCommandsFail() {
        flowContainer.getResult().setStatus(Response.Status.PRECONDITION_FAILED);
    }

    private void handleProcessExceptions(Exception e) {
        if (!(e instanceof RibrestDefaultException)) {
            e = new RibrestDefaultException(e.getMessage() != null ? e.getMessage() : "Unknown");
        }
        setErrorOutput(((RibrestDefaultException) e).getError());
    }
    
    public void setContainerRequest(ContainerRequest containerRequest) {
        this.containterRequest = containerRequest;
        flowContainer.setMethod(containterRequest.getMethod());
    }

    public void setBeforeCommandsToCurrentRequest(List beforeCommands) {
        this.beforeCommands = beforeCommands != null ? beforeCommands : new ArrayList();
    }

    public void setAfterCommandsToCurrentRequest(List afterCommands) {
        this.afterCommands = afterCommands != null ? afterCommands : new ArrayList();
    }
    
    public void setCurrentDAO(Class currentDao){
        this.currentDao = currentDao;
    }

    /**
     * The inject method will automatically start the RequestContext. This
     * method is also automatically called by the HK2 @PostConstruct mechanism.
     *
     * @return void.
     */
    @PostConstruct
    private void injected() {
        ((RequestContext) serviceLocator.getService(RequestContext.class)).startRequest();
    }

    /**
     * The destroyed method will automatically stop the RequestContext. This
     * method is also automatically called by the HK2 @PostConstruct mechanism.
     *
     * @return void.
     */
    @PreDestroy
    private void destroyed() {
        serviceLocator.getService(RequestContext.class).stopRequest();
    }

}
