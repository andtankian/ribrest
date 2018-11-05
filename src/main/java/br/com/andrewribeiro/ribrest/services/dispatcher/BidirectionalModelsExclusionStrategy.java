package br.com.andrewribeiro.ribrest.services.dispatcher;

import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.core.model.Model;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
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
    private Set repetitiveReferences = new HashSet();

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
                if (repetitiveReferences.contains(modelInstance) || modelInstance == null) {
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

    private void addAllModelAttributesToRepetitiveModels(Model model) {
        repetitiveReferences.addAll(model.getAllModelAttributes().stream()
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
        model.getAllModelOneToManyAttributes().stream()
                .forEach(attribute -> {
                    Collection tempCollection = null;
                    if (Collection.class.isAssignableFrom(attribute.getType())) {
                        attribute.setAccessible(true);
                        try {
                            tempCollection = (Collection) attribute.get(model);
                            if (tempCollection != null) {
                                repetitiveReferences.addAll((Set) tempCollection.stream().map(modelInstance -> {
                                    return modelInstance;
                                }).collect(Collectors.toSet()));
                            }
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
