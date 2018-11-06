package br.com.andrewribeiro.ribrest.services.miner;

import java.util.List;
import org.glassfish.jersey.server.ContainerRequest;

/**
 *
 * @author Andrew Ribeiro
 */
public interface Miner {
    public void mineRequest(ContainerRequest containerRequest);
    @Deprecated
    public List extractIgnoredFields();
}
