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
import br.com.andrewribeiro.ribrest.services.miner.factory.interfaces.MinerFactory;

/**
 *
 * @author Andrew Ribeiro
 */
public class Facade {
    
    /**
     * Facade Constructor. 
     * 
     * @param cr is the main object coming from the requester. It contains all data information about the request itself.
     * @param entity The name of the entity being requested.
     */
    public Facade(ContainerRequest cr, String entity) {
        this.cr = cr;
        this.entity = entity.substring("class ".length(), entity.length());
    }

    @Inject
    FlowContainer fc;
    
    @Inject
    MinerFactory mf;
    
    @Inject
    private ServiceLocator sl;  
             
    ContainerRequest cr;
    private final String entity;
    
    
    /**
     * Method that process the Ribrest core mechanism.
     * This method is called by resources to process an http request.
     * 
     * @return Response that will be processed by http grizzly server and returned to the requester.
     */
    public Response process() {
        Miner m = null;
        try {

            /**
             * Getting a class resource instance that will be used to process all
             * the code about it
             */
            Class c = Class.forName(entity);
            
            /**
             * mf must return a IMiner concrete instance that will be used to
             * extract all information of the ContainerRequest and populate
             * the model instance
             */
            m = mf.getMinerInstance(c);
            sl.inject(m); //Inject all the services to IMiner concrete instance.
            
            /**
             * Until here, we don't get any concrete instance of IModel subclass
             * Calling setup entity will verify if is possible to get a real instance
             * of the current IModel subclass or will throw an exception
             */
            fc.setupEntity(c);
            
            /**
             * Extracting all the information of ContainerRequest and populate to
             * real instance of IModel subclass (if there is one)
             */
            m.extract(cr);
            
            /**
             * Run the main flow
             */
            run();
        } catch (Exception e) {
            if(!(e instanceof RibrestDefaultException)){
                e = new RibrestDefaultException(e.getCause() != null ? e.getCause().toString() : "Unknown");
            }
            fc.setGo(false);
            fc.getResult().setStatus(Response.Status.EXPECTATION_FAILED);
            fc.getResult().setCause(((RibrestDefaultException)e).getError());
        } finally {
            Response r = m.send(fc);
            sl.preDestroy(this);
            return r;
        }
    }
    
    private void run() throws RibrestDefaultException{
        
        PersistenceCenter pc = new CRUDCenter();
        sl.inject(pc);
        pc.perform();
    }
    
    /**
     * The inject method will automatically start the RequestContext.
     * This method is also automatically called by the HK2 @PostConstruct mechanism.
     * 
     * @return void.
     */
    @PostConstruct
    private void injected(){
        ((RequestContext)sl.getService(RequestContext.class)).startRequest();
        fc.setMethod(cr.getMethod());
    }
    
    /**
     * The inject method will automatically stop the RequestContext.
     * This method is also automatically called by the HK2 @PostConstruct mechanism.
     * @return void.
     */
    @PreDestroy
    private void destroyed(){
        sl.getService(RequestContext.class).stopRequest();
    }
    

}
