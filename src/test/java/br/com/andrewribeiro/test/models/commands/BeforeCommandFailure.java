package br.com.andrewribeiro.test.models.commands;

import br.com.andrewribeiro.ribrest.services.command.AbstractCommand;

/**
 *
 * @author Andrew Ribeiro
 */
public class BeforeCommandFailure extends AbstractCommand{

    @Override
    public void execute() throws Exception {
        
        new Long("failure").longValue();
        
        
    }
    
}
