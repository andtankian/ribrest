package br.com.andrewribeiro.ribrest;

import br.com.andrewribeiro.ribrest.core.controller.Facade;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.ContainerRequest;

/**
 *
 * @author Andrew Ribeiro
 */
class RibrestInflector implements Inflector<ContainerRequestContext, Response>{
    
    private Facade facade;
    private Ribrest ribrestInstance;

    public RibrestInflector(Ribrest ribrestInstance, Facade facade) {
        this.facade = facade;
        this.ribrestInstance = ribrestInstance;
    }

    @Override
    public Response apply(ContainerRequestContext containerRequestContainer) {
        ribrestInstance.getServiceLocator().inject(facade);
        ribrestInstance.getServiceLocator().postConstruct(facade);
        facade.setContainerRequest((ContainerRequest) containerRequestContainer);
        return facade.processRequest();
    }
    
}
