package br.com.andrewribeiro.test.command.commands;

import br.com.andrewribeiro.ribrest.model.interfaces.Model;
import br.com.andrewribeiro.ribrest.services.command.AbstractCommand;

/**
 *
 * @author Andrew Ribeiro
 */
public class AfterCommandSucceed extends AbstractCommand{

    @Override
    public void execute() throws Exception {
        Model model = flowContainer.getModel(); 
        System.out.println("I'm running an AFTER command in " + model.getClass() + " class.");
    }
    
    
}
