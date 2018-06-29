package br.com.andrewribeiro.ribrest.services.command;

import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.model.interfaces.Model;

/**
 *
 * @author Andrew Ribeiro
 */
public class MergeModelToPersistedModelCommand extends AbstractCommand{

    @Override
    public void execute() throws RibrestDefaultException, Exception {
        Model model = flowContainer.getModel();
        
        Model persistedModel = (Model) flowContainer.getExtraObject(Model.LOADED_MODEL_KEY);
        
        persistedModel.merge(model);
        
    }
    
}
