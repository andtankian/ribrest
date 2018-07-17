package br.com.andrewribeiro.ribrest.services.command;

import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.model.interfaces.Model;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import java.util.Collection;

/**
 *
 * @author Andrew Ribeiro
 */
public class GetPersistentChildrenModelCommand extends AbstractCommand {

    @Override
    public void execute() throws RibrestDefaultException, Exception {

        Model model = flowContainer.getModel();

        model.getAllCollectionModelAttributes()
                .forEach(attribute -> {
                    try {
                        attribute.setAccessible(true);
                        Collection detachedModels = (Collection) attribute.get(model);
                        Collection newPersistedModels = RibrestUtils.getCollectionInstance(attribute.getType());
                        detachedModels.forEach(detachedModel -> {
                            Object persistedModel = flowContainer.getEm().find(detachedModel.getClass(), ((Model) detachedModel).getId());
                            if (persistedModel == null) {
                                throw new RuntimeException(new StringBuilder("The child model ")
                                        .append(detachedModel.getClass().getSimpleName())
                                        .append(" identified by ")
                                        .append(((Model) detachedModel).getId())
                                        .append(" was not found.").toString());

                            } else {
                                newPersistedModels.add(persistedModel);
                            }
                        });
                        
                        attribute.set(model, newPersistedModels);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                });
    }

}
