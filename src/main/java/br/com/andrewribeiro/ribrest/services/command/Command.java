package br.com.andrewribeiro.ribrest.services.command;

import br.com.andrewribeiro.ribrest.services.FlowContainer;

/**
 *
 * @author Andrew Ribeiro
 */
public interface Command {
    
    public FlowContainer execute(FlowContainer flowContainer) throws Exception;
    
}
