package br.com.andrewribeiro.test.command.commands;

import br.com.andrewribeiro.ribrest.core.model.Model;
import br.com.andrewribeiro.ribrest.services.command.AbstractCommand;

/**
 *
 * @author Andrew Ribeiro
 */
public class BeforeCommandSucceed extends AbstractCommand {

    @Override
    public void execute() throws Exception {
        Model model = flowContainer.getModel();
        System.out.println("I'm running a before command in " + model.getClass() + " class.");
    }

}
