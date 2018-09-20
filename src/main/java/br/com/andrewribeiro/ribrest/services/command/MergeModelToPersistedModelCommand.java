package br.com.andrewribeiro.ribrest.services.command;

import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.core.model.Model;

/**
 *
 * @author Andrew Ribeiro
 */
public class MergeModelToPersistedModelCommand extends AbstractCommand{

    @Override
    public void execute() throws RibrestDefaultException, Exception {
        Model model = flowContainer.getModel();
        
        Model persistedModel = (Model) flowContainer.getExtraObject(Model.PERSISTED_MODEL_KEY);
        flowContainer.removeExtraObject(Model.PERSISTED_MODEL_KEY);
        
        persistedModel.merge(model);
        
        flowContainer.setModel(persistedModel);
        
    }
    
}
