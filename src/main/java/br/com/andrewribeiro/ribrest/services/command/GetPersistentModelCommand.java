package br.com.andrewribeiro.ribrest.services.command;

import br.com.andrewribeiro.ribrest.model.interfaces.Model;
import br.com.andrewribeiro.ribrest.services.FlowContainer;

/**
 *
 * @author Andrew Ribeiro
 */
public class GetPersistentModelCommand implements Command{

    @Override
    public FlowContainer execute(FlowContainer flowContainer) {
        Model model = flowContainer.getModel();
        
        return flowContainer;
    }
    
}
