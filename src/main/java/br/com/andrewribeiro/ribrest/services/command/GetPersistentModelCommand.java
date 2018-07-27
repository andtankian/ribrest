package br.com.andrewribeiro.ribrest.services.command;

import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.model.interfaces.Model;
import javax.persistence.OneToOne;

/**
 *
 * @author Andrew Ribeiro
 */
public class GetPersistentModelCommand extends AbstractCommand {

    @Override
    public void execute() throws RibrestDefaultException, Exception {
        Model model = flowContainer.getModel();
        Model persistedModel = flowContainer.getEntityManager().find(model.getClass(), model.getId());
        if (persistedModel == null) {
            throw new RibrestDefaultException(new StringBuilder("The model ").append(model.getId()).append(" was not found.").toString());
        }
        loadAllModelChildrenAttributes(model);
        flowContainer.addExtraObject(Model.PERSISTED_MODEL_KEY, persistedModel);
    }

    private void loadAllModelChildrenAttributes(Model model) {
        model.getAllAttributes().stream()
                .filter(attribute -> attribute.isAnnotationPresent(OneToOne.class))
                .forEach(attribute -> {
                    try {
                        attribute.setAccessible(true);
                        Model currentAttributeModel = (Model) attribute.get(model);
                        if (currentAttributeModel.getId() != null) {
                            Model persistedAttributeModel = flowContainer.getEntityManager().getReference(currentAttributeModel.getClass(), currentAttributeModel.getId());
                            attribute.set(model, persistedAttributeModel);
                        }
                    } catch (IllegalAccessException | IllegalArgumentException | SecurityException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
