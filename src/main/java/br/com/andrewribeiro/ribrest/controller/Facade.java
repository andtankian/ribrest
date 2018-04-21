package br.com.andrewribeiro.ribrest.controller;

import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.services.miner.IMiner;
import br.com.andrewribeiro.ribrest.services.FlowContainer;
import br.com.andrewribeiro.ribrest.services.Result;
import br.com.andrewribeiro.ribrest.services.cdi.hk2.RequestContext;
import br.com.andrewribeiro.ribrest.services.miner.IMinerFactory;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ContainerRequest;

/**
 *
 * @author Andrew Ribeiro
 */
public class Facade {
    
    public Facade(ContainerRequest cr, String entity) {
        this.cr = cr;
        this.entity = entity.substring("class ".length(), entity.length());
    }

    @Inject
    FlowContainer fc;
    
    @Inject
    IMinerFactory mf;
    
    @Inject
    private ServiceLocator sl;  
             
    ContainerRequest cr;
    private final String entity;
    
    public Response process() {
        IMiner m = null;
        try {            
            Class c = Class.forName(entity);
            fc.getHolder().setupEntity(c);
            m = mf.getMinerInstance(c);
            sl.inject(m);
            m.extract(cr);
        } catch (Exception e) {
            if(!(e instanceof RibrestDefaultException)){
                e = new RibrestDefaultException(e.getCause().getMessage());
            }
            
            fc.getResult().setStatus(Response.Status.EXPECTATION_FAILED);
            fc.getResult().setCause(((RibrestDefaultException)e).getError());
        } finally {
            Response r = m.send(fc);
            sl.preDestroy(this);
            return r;
        }
    }
    
    @PostConstruct
    private void injected(){
        ((RequestContext)sl.getService(RequestContext.class)).startRequest();
    }
    
    @PreDestroy
    private void destroyed(){
        ((RequestContext)sl.getService(RequestContext.class)).stopRequest();
    }
    

}
