package br.com.andrewribeiro.ribrest.services.miner;

import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.services.FlowContainer;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.ContainerRequest;

/**
 *
 * @author Andrew Ribeiro
 */
public interface IMiner {
    public Response send(FlowContainer fc);
    public void extract(ContainerRequest cr) throws RibrestDefaultException;
}
