package br.com.andrewribeiro.ribrest.core.controller;

import br.com.andrewribeiro.ribrest.core.persistence.center.CRUDCenterImpl;
import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.services.dtos.FlowContainer;
import br.com.andrewribeiro.ribrest.services.cdi.RequestContext;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ContainerRequest;
import br.com.andrewribeiro.ribrest.services.miner.Miner;
import br.com.andrewribeiro.ribrest.core.persistence.PersistenceCenter;
import br.com.andrewribeiro.ribrest.services.command.Command;
import br.com.andrewribeiro.ribrest.services.dispatcher.Dispatcher;
import br.com.andrewribeiro.ribrest.services.miner.MinerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrew Ribeiro
 */
public class Facade {

    @Inject
    private FlowContainer flowContainer;

    @Inject
    private MinerFactory minerFactory;

    @Inject
    private ServiceLocator serviceLocator;

    private ContainerRequest containterRequest;
    private final String modelClassName;
    private Miner miner;
    private List<Command> beforeCommands, afterCommands;
    private Class currentDaoClass;
    private Class<Dispatcher> currentDispatcherClass;
    private Dispatcher dispatcher;
    private Response response = Response.serverError().build();

    public Facade(String entity) {
        this.modelClassName = entity;
    }

    public Response processRequest() {
        try {
            prepareForMining();
            mine();
            runBeforeCommands();
            hitDatabase();
            runAfterCommands();
        } catch (Exception e) {
            handleProcessExceptions(e);
        } finally {
            dispatch();
        }

        return response;
    }

    private void prepareForMining() throws ClassNotFoundException, Exception {
        Class currentModelClass = Class.forName(modelClassName);
        miner = minerFactory.getMinerInstance(currentModelClass);
        injectDependency(miner);
        flowContainer.setMiner(miner);
        flowContainer.setModelFromClass(currentModelClass);
    }

    private void mine() throws RibrestDefaultException {
        miner.mineRequest(containterRequest);
    }

    private void injectDependency(Object dependency) {
        serviceLocator.inject(dependency);
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

    private void hitDatabase() throws RibrestDefaultException {
        PersistenceCenter persistenceCenter = new CRUDCenterImpl();
        persistenceCenter.setCurrentDAOClass(currentDaoClass);
        injectDependency(persistenceCenter);
        persistenceCenter.perform();
    }

    private void dispatch() {
        getDispatcherInstance(currentDispatcherClass);
        try {
            response = dispatcher.send();
        } catch (Exception exception) {
            response = Response.serverError().build();
        } finally {
            serviceLocator.preDestroy(this);
        }
    }

    private void getDispatcherInstance(Class<Dispatcher> dispatcherClass) {
        if (dispatcherClass == null) {
            throw new RuntimeException("Ribrest couldn't get a dispatcher instance of a null class.");
        }
        try {
            dispatcher = dispatcherClass.newInstance();
            serviceLocator.inject(dispatcher);
        } catch (Exception exception) {
            throw runtimeException(exception.getMessage());
        }
    }

    private RuntimeException runtimeException(String cause) {
        return new RuntimeException(cause);
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

    public void setCurrentDAO(Class currentDao) {
        this.currentDaoClass = currentDao;
    }

    public void setCurrentDispatcherClass(Class dispatcherClass) {
        currentDispatcherClass = dispatcherClass;
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
