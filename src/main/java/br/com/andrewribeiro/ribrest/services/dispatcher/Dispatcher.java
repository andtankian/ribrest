package br.com.andrewribeiro.ribrest.services.dispatcher;

import br.com.andrewribeiro.ribrest.services.dtos.FlowContainer;
import javax.ws.rs.core.Response;

/**
 *
 * @author Andrew Ribeiro
 */
public interface Dispatcher {
    public Response send();
}
