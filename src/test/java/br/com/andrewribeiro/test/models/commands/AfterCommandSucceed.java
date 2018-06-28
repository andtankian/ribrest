package br.com.andrewribeiro.test.models.commands;

import br.com.andrewribeiro.ribrest.model.interfaces.Model;
import br.com.andrewribeiro.ribrest.services.FlowContainer;
import br.com.andrewribeiro.ribrest.services.command.Command;

/**
 *
 * @author Andrew Ribeiro
 */
public class AfterCommandSucceed implements Command{

    @Override
    public FlowContainer execute(FlowContainer flowContainer) throws Exception {
        Model model = flowContainer.getModel(); 
        System.out.println("I'm running an AFTER command in " + model.getClass() + " class.");
        return flowContainer;
    }
    
    
}
