package br.com.andrewribeiro.ribrest.services.miner.util;

import br.com.andrewribeiro.ribrest.model.interfaces.Model;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Andrew Ribeiro
 */
public class BidirectionalModelsExclusionStrategy implements ExclusionStrategy {

    public BidirectionalModelsExclusionStrategy(List<Model> models, List<String> ignoredFields) {
        this.models = models;
        this.ignoredFields = ignoredFields;
    }

    private List<Model> models;
    private List<String> ignoredFields;
    private Set<Model> repetitiveModels = new HashSet<>();

    public void removeCircularReferences() {
        models.forEach(model -> {
            clearAllModelCircularReferences(model);
        });
    }

    private void clearAllModelCircularReferences(Model model) {
        if (isModelNull(model)) {
            return;
        }
        repetitiveModels.add(model);
        clearDirectModelCircularReferences(model);
        clearCollectionModelCircularReferences(model);
    }

    private void clearDirectModelCircularReferences(Model model) {
        model.getAllModelAttributes().forEach(attribute -> {
            attribute.setAccessible(true);
            try {
                Model modelInstance = (Model) attribute.get(model);
                if (repetitiveModels.contains(modelInstance)) {
                    attribute.set(model, null);
                } else {
                    populateRepetiveModels(modelInstance);
                    clearAllModelCircularReferences(modelInstance);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void clearCollectionModelCircularReferences(Model model) {
        model.getAllCollectionModelAttributes().forEach(attribute -> {
            attribute.setAccessible(true);
            try {
                Collection<Model> collectionInstance = (Collection) attribute.get(model);
                collectionInstance.forEach(modelInstance -> {
                    if (repetitiveModels.contains(modelInstance)) {
                        try {
                            attribute.set(model, null);
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            throw new RuntimeException(ex.getMessage());
                        }
                    } else {
                        populateRepetiveModels(modelInstance);
                        clearAllModelCircularReferences(modelInstance);
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    private void populateRepetiveModels(Model model) {

        addAllModelAttributesToRepetitiveModels(model);
        addAllModelCollectionAttributesToRepetitiveModels(model);
    }

    private void addAllModelAttributesToRepetitiveModels(Model model) {
        repetitiveModels.addAll(model.getAllModelAttributes().stream()
                .map((attribute) -> {
                    attribute.setAccessible(true);
                    Model attributeInstance;
                    try {
                        attributeInstance = (Model) attribute.get(model);
                        return attributeInstance;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toSet()));

    }

    private void addAllModelCollectionAttributesToRepetitiveModels(Model model) {
        model.getAllCollectionModelAttributes().stream()
                .forEach(attribute -> {
                    Collection tempCollection = null;
                    if (Collection.class.isAssignableFrom(attribute.getType())) {
                        attribute.setAccessible(true);
                        try {
                            tempCollection = (Collection) attribute.get(model);
                            repetitiveModels.addAll((Set) tempCollection.stream().map(modelInstance -> {
                                return modelInstance;
                            }).collect(Collectors.toSet()));
                        } catch (Exception e) {
                            throw new RuntimeException();
                        }
                    }
                });
    }

    private boolean isModelNull(Model model) {
        return model == null;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes fa) {
        return ignoredFields.contains(fa.getName());
    }

    @Override
    public boolean shouldSkipClass(Class<?> type) {
        return false;
    }

}
