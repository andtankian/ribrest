package br.com.andrewribeiro.ribrest.services.miner.interfaces;

import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import java.util.List;
import org.glassfish.jersey.server.ContainerRequest;

/**
 *
 * @author Andrew Ribeiro
 */
public interface Miner {
    public void extractDataFromRequest(ContainerRequest cr) throws RibrestDefaultException;
    public List extractIgnoredFields();
}
