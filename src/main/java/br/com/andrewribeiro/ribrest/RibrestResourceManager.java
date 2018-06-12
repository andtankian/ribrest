package br.com.andrewribeiro.ribrest;

import br.com.andrewribeiro.ribrest.controller.Facade;
import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.logs.RibrestLog;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.model.Resource;

/**
 *
 * @author Andrew Ribeiro
 */
class RibrestResourceManager extends AbstractRibrestConfigurator{
    
    Set createResources(List classes) {
        RibrestLog.log("Creating resources based on scanned models...");
        Set resources = new HashSet();
        classes = classes != null ? classes : new ArrayList();
        for (final Object clazz : classes) {
            try {
                Resource.Builder rb = Resource.builder(RibrestUtils.getResourceName((Class) clazz));
                rb.addMethod("GET")
                        .produces(MediaType.APPLICATION_JSON).handledBy(new Inflector<ContainerRequestContext, Response>() {
                    @Override
                    public Response apply(ContainerRequestContext data) {
                        return produceValidFacade((ContainerRequest) data, clazz.toString()).process();
                    }
                });
                rb.addMethod("POST")
                        .produces(MediaType.APPLICATION_JSON).handledBy(new Inflector<ContainerRequestContext, Response>() {
                    @Override
                    public Response apply(ContainerRequestContext data) {
                        return produceValidFacade((ContainerRequest) data, clazz.toString()).process();
                    }
                });
                rb.addMethod("PUT")
                        .produces(MediaType.APPLICATION_JSON).handledBy(new Inflector<ContainerRequestContext, Response>() {
                    @Override
                    public Response apply(ContainerRequestContext data) {
                        return produceValidFacade((ContainerRequest) data, clazz.toString()).process();
                    }
                });
                rb.addMethod("DELETE")
                        .produces(MediaType.APPLICATION_JSON).handledBy(new Inflector<ContainerRequestContext, Response>() {
                    @Override
                    public Response apply(ContainerRequestContext data) {
                        return produceValidFacade((ContainerRequest) data, clazz.toString()).process();
                    }
                });
                resources.add(rb.build());
                RibrestLog.log(new StringBuilder("Resource created at: ").append(ribrest.getBaseUrl()).append(((Class) clazz).getSimpleName().toLowerCase()).append("s").toString());
            } catch (RibrestDefaultException ex) {
                throw new RuntimeException(ex.getError());
            }
        }
        return resources;
    }
    
    private Facade produceValidFacade(ContainerRequest cr, String className) {
        Facade f = new Facade(cr, className);
        ribrest.getServiceLocator().inject(f);
        ribrest.getServiceLocator().postConstruct(f);
        return f;
    }
    
}
