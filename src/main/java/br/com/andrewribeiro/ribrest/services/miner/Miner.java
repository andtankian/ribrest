package br.com.andrewribeiro.ribrest.services.miner;

import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultException;
import java.util.List;
import org.glassfish.jersey.server.ContainerRequest;

/**
 *
 * @author Andrew Ribeiro
 */
public interface Miner {
    @Deprecated
    public void extractDataFromRequest(ContainerRequest cr) throws RibrestDefaultException;
    public void mineRequest(ContainerRequest containerRequest);
    public List extractIgnoredFields();
}
