package br.com.andrewribeiro.ribrest.services.command;

import br.com.andrewribeiro.ribrest.services.dtos.FlowContainer;
import javax.inject.Inject;

/**
 *
 * @author Andrew Ribeiro
 */
public abstract class AbstractCommand implements Command{
    
    @Inject
    protected FlowContainer flowContainer;
}
