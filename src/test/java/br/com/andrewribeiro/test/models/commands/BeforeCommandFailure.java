package br.com.andrewribeiro.test.models.commands;

import br.com.andrewribeiro.ribrest.services.FlowContainer;
import br.com.andrewribeiro.ribrest.services.command.Command;

/**
 *
 * @author Andrew Ribeiro
 */
public class BeforeCommandFailure implements Command{

    @Override
    public FlowContainer execute(FlowContainer flowContainer) throws Exception {
        
        new Long("failure").longValue();
        
        
        return flowContainer;
    }
    
}
