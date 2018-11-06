package br.com.andrewribeiro.ribrest.services.dispatcher;

import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.core.model.Model;
import br.com.andrewribeiro.ribrest.services.dtos.FlowContainer;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Andrew Ribeiro
 */
public class BidirectionalModelsExclusionStrategy implements ExclusionStrategy {

    public BidirectionalModelsExclusionStrategy(FlowContainer flowContainer) {
        models = flowContainer.getHolder().getModels() != null ? flowContainer.getHolder().getModels() : new ArrayList();
        rejectedFields = flowContainer.getRequestMaps().getQueryMap().get("rejects");
        rejectedFields = rejectedFields == null ? new ArrayList() : rejectedFields;
        acceptedFields = flowContainer.getRequestMaps().getQueryMap().get("accepts");
        acceptedFields = acceptedFields == null ? new ArrayList() : acceptedFields;
    }

    private final List<Model> models;
    private List<String> rejectedFields;
    private List<String> acceptedFields;
    private final Set repetitiveReferences = new HashSet();

    public void removeCircularReferences() {
        models.forEach(model -> {
            repetitiveReferences.clear();
            clearAllModelCircularReferences(model);
        });
    }

    private void clearAllModelCircularReferences(Model model) {
        if (isModelNull(model)) {
            return;
        }
        repetitiveReferences.add(model);
        clearDirectModelCircularReferences(model);
        clearCollectionModelCircularReferences(model);
    }

    private void clearDirectModelCircularReferences(Model model) {
        model.getAllModelAttributes().forEach(attribute -> {
            attribute.setAccessible(true);
            try {
                Model modelInstance = (Model) attribute.get(model);
                if ((repetitiveReferences.contains(modelInstance) || modelInstance == null) && !acceptedFields.contains(attribute.getName().toLowerCase())) {
                    attribute.set(model, null);
                } else {
                    clearAllModelCircularReferences(modelInstance);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void clearCollectionModelCircularReferences(Model model) {
        model.getAllModelOneToManyAttributes().forEach(attribute -> {
            clearAnyCollection(model, attribute);
        });
        model.getAllModelManyToManyAttributes().forEach(attribute -> {
            clearAnyCollection(model, attribute);
        });

    }

    private void clearAnyCollection(Model model, Field attribute) {
        attribute.setAccessible(true);
        try {
            Collection<Model> collectionInstance = (Collection) attribute.get(model);
            if (collectionInstance != null) {
                if (repetitiveReferences.contains(collectionInstance) || !Collections.disjoint(collectionInstance, repetitiveReferences)) {
                    try {
                        attribute.set(model, null);
                    } catch (Exception e) {
                        throw new RibrestDefaultException(e.getMessage());
                    }
                } else {
                    repetitiveReferences.add(collectionInstance);
                    collectionInstance.forEach(modelInstance -> {
                        clearAllModelCircularReferences(modelInstance);
                    });
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean isModelNull(Model model) {
        return model == null;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes fa) {
        System.out.println(fa.getName() +", " + rejectedFields.contains(fa.getName()));
        return rejectedFields.contains(fa.getName());
    }

    @Override
    public boolean shouldSkipClass(Class<?> type) {
        return false;
    }

}
