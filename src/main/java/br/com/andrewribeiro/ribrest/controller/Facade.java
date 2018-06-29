package br.com.andrewribeiro.ribrest.controller;

import br.com.andrewribeiro.ribrest.dao.CRUDCenter;
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
        this.entity = entity;
    }

    @Inject
    FlowContainer fc;

    @Inject
    Dispatcher dispatcher;

    @Inject
    MinerFactory mf;

    @Inject
    private ServiceLocator sl;

    ContainerRequest cr;
    private final String entity;
    private List<Command> beforeCommands, afterCommands;

    public Response process() {
        Miner miner = null;
        Response response;
        try {
            Class classInstance = Class.forName(entity);
            miner = mf.getMinerInstance(classInstance);
            sl.inject(miner); //Inject all the HK2 services to IMiner concrete instance.
            fc.setMiner(miner);
            fc.initModelInstance(classInstance);
            miner.extractDataFromRequest(cr);
            runBeforeCommands();
            run();
            runAfterCommands();
        } catch (Exception e) {
            if (!(e instanceof RibrestDefaultException)) {
                e = new RibrestDefaultException(e.getCause() != null ? e.getCause().toString() : "Unknown");
            }
            setErrorOutput(((RibrestDefaultException)e).getError());
        } finally {
            response = dispatcher.send(fc);
            sl.preDestroy(this);
        }
        return response;
    }

    private void runBeforeCommands() {
        runCommands(beforeCommands);
    }
    
    private void runAfterCommands(){
        runCommands(afterCommands);
    }
    
    private void runCommands(List<Command> commands){
        commands.forEach(command -> {
            try {
                sl.inject(command);
                command.execute();
            } catch (Exception ex) {
                fc.getResult().setStatus(Response.Status.PRECONDITION_FAILED);
                throw new RuntimeException(ex.getMessage());
            }
        });
    }

    private void run() throws RibrestDefaultException {
        PersistenceCenter pc = new CRUDCenter();
        sl.inject(pc);
        pc.perform();
    }
    
    private void setErrorOutput(String cause){
        fc.getResult().setStatus(fc.getResult().getStatus().equals(Response.Status.OK) ? 
                Response.Status.INTERNAL_SERVER_ERROR : fc.getResult().getStatus());
        fc.getResult().setCause(cause);
    }

    public void setContainerRequest(ContainerRequest containerRequest) {
        this.cr = containerRequest;
        fc.setMethod(cr.getMethod());
    }

    public void setBeforeCommandsToCurrentRequest(List beforeCommands) {
        this.beforeCommands = beforeCommands != null ? beforeCommands : new ArrayList();
    }

    public void setAfterCommandsToCurrentRequest(List afterCommands) {
        this.afterCommands = afterCommands != null ? afterCommands : new ArrayList();
    }

    /**
     * The inject method will automatically start the RequestContext. This
     * method is also automatically called by the HK2 @PostConstruct mechanism.
     *
     * @return void.
     */
    @PostConstruct
    private void injected() {
        ((RequestContext) sl.getService(RequestContext.class)).startRequest();
    }

    /**
     * The destroyed method will automatically stop the RequestContext. This
     * method is also automatically called by the HK2 @PostConstruct mechanism.
     *
     * @return void.
     */
    @PreDestroy
    private void destroyed() {
        sl.getService(RequestContext.class).stopRequest();
    }

}
