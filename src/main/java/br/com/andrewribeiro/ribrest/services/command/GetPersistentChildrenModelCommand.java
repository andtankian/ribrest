package br.com.andrewribeiro.ribrest.services.command;

import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.core.model.Model;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 *
 * @author Andrew Ribeiro
 */
public class GetPersistentChildrenModelCommand extends AbstractCommand {

    Model model;

    @Override
    public void execute() throws RibrestDefaultException, Exception {

        model = flowContainer.getModel();
        getAllOneToManyPersistentChild();
        getAllManyToOnePersistentChild();
        getAllOneToOnePersistentChild();
        getAllManyToManyPersistentChild();

    }

    private void getAllOneToManyPersistentChild() {
        model.getAllModelOneToManyAttributes()
                .forEach(attribute -> {
                    getPersistentCollection(attribute);
                });
    }

    private void getPersistentCollection(Field attribute) {
        try {
            attribute.setAccessible(true);
            Collection detachedModels = (Collection) attribute.get(model);
            Collection newPersistedModels = RibrestUtils.getCollectionInstance(attribute.getType());
            if (detachedModels != null) {
                detachedModels.forEach(detachedModel -> {
                    Object persistedModel = flowContainer.getEntityManager().find(detachedModel.getClass(), ((Model) detachedModel).getId());
                    if (persistedModel == null) {
                        throw new RuntimeException(new StringBuilder("The child model ")
                                .append(detachedModel.getClass().getSimpleName())
                                .append(" identified by ")
                                .append(((Model) detachedModel).getId())
                                .append(" was not found.").toString());

                    } else {
                        ((Model) persistedModel).merge((Model) detachedModel);
                        newPersistedModels.add(persistedModel);
                    }
                });
            }
            attribute.set(model, newPersistedModels);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void getAllManyToOnePersistentChild() {
        model.getAllModelManyToOneAttributes().forEach(attribute -> {
            try {
                attribute.setAccessible(true);
                Model detachedModel = (Model) attribute.get(model);
                if (detachedModel != null && detachedModel.getId() != null) {
                    Model persistedModel = flowContainer.getEntityManager().find(detachedModel.getClass(), detachedModel.getId());
                    if (persistedModel == null) {
                        throw new RuntimeException(new StringBuilder("The child ").append(attribute.getName())
                                .append(" identified by ").append(detachedModel.getId()).append(" wasn't found.")
                                .toString());
                    } else {
                        persistedModel.merge(detachedModel);
                        attribute.set(model, persistedModel);
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        });
    }

    private void getAllOneToOnePersistentChild() {
        model.getAllModelOneToOneNotMappedByAttributes().forEach(attribute -> {
            try {
                attribute.setAccessible(true);
                Model detachedModel = (Model) attribute.get(model);
                if (detachedModel != null && detachedModel.getId() != null) {
                    Model persistedModel = flowContainer.getEntityManager().find(detachedModel.getClass(), detachedModel.getId());
                    if (persistedModel == null) {
                        throw new RibrestDefaultException(new StringBuilder("The child ").append(attribute.getName()).append(" wasn't found.").toString());
                    } else {
                        attribute.set(model, persistedModel);
                    }
                }
            } catch (Exception ex) {
                if (ex instanceof RibrestDefaultException) {
                    throw (RibrestDefaultException) ex;
                }

                throw new RibrestDefaultException("Error while getting one to one persistent child.");
            }
        });
    }

    private void getAllManyToManyPersistentChild() {
        model.getAllModelManyToManyNotMappedAttributes().forEach(attribute -> {
            getPersistentCollection(attribute);
        });
    }

}
