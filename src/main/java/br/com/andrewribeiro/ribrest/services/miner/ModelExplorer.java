/*
 * The MIT License
 *
 * Copyright 2018 ribeiro.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.com.andrewribeiro.ribrest.services.miner;

import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.core.model.Model;
import br.com.andrewribeiro.ribrest.logs.RibrestLog;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Andrew Ribeiro
 */
public class ModelExplorer {

    Model model;
    RequestMaps requestMaps;

    public ModelExplorer(Model model) {
        this.model = model;
    }

    public void fillModelWithData(RequestMaps requestMaps) {
        this.requestMaps = requestMaps;
        fillModelIdFromPath();
        fillModel(model, null);
    }

    private void fillModelIdFromPath() {
        try {
            model.setId(Long.parseLong(requestMaps.getPathMap().getFirst("id")));
        } catch (NumberFormatException nfe) {
        }
    }

    private void fillModel(Model model, String attributeName) {
        model.getAllAttributes().forEach(attribute -> {
            String realAttributeName
                    = attributeName == null
                            ? ""
                            : attributeName.concat(".");
            fillAttribute(new RibrestAttributeContainer(attribute, model, realAttributeName.concat(attribute.getName())));
        });
    }

    private void fillAttribute(RibrestAttributeContainer attributeContainer) {
        fillString(attributeContainer);
        fillLong(attributeContainer);
        fillBoolean(attributeContainer);
        fillInteger(attributeContainer);
        fillDouble(attributeContainer);
        fillFloat(attributeContainer);
        fillTimestamp(attributeContainer);
        fillModelAttribute(attributeContainer);
        fillCollectionOfModelAttribute(attributeContainer);

    }

    private void fillString(RibrestAttributeContainer attributeContainer) {
        if (attributeContainer.getAttribute().getType() == String.class) {
            attributeContainer.setParameterValue(requestMaps.getFormMap().getFirst(attributeContainer.getParameterName()));
            fill(attributeContainer);
        }
    }

    private void fillLong(RibrestAttributeContainer attributeContainer) {
        if (checkClassType(attributeContainer, Long.class)) {
            try {
                attributeContainer.setParameterValue(Long.parseLong(getFirstFormByParameterName(attributeContainer.getParameterName())));
            } catch (NumberFormatException nfe) {
                RibrestLog.log(getInonffensiveExceptionMessageWhileFilling(attributeContainer.getAttribute()));
            }
            fill(attributeContainer);
        }
    }

    private void fillBoolean(RibrestAttributeContainer attributeContainer) {
        if (checkClassType(attributeContainer, Boolean.class)) {
            try {
                attributeContainer.setParameterValue(Boolean.parseBoolean(getFirstFormByParameterName(attributeContainer.getParameterName())));
            } catch (Exception e) {
            }

            fill(attributeContainer);
        }
    }

    private void fillInteger(RibrestAttributeContainer attributeContainer) {
        if (checkClassType(attributeContainer, Integer.class)) {
            try {
                attributeContainer.setParameterValue(Integer.parseInt(getFirstFormByParameterName(attributeContainer.getParameterName())));
            } catch (NumberFormatException nfe) {
            }

            fill(attributeContainer);
        }

    }

    private void fillDouble(RibrestAttributeContainer attributeContainer) {        
        if (checkClassType(attributeContainer, Double.class)) {
            try {
                attributeContainer.setParameterValue(Double.parseDouble(getFirstFormByParameterName(attributeContainer.getParameterName())));
            } catch (NumberFormatException nfe) {
            }

            fill(attributeContainer);
        }
    }
    
    

    private void fillFloat(RibrestAttributeContainer attributeContainer) {
        if (checkClassType(attributeContainer, Float.class)) {
            try {
                attributeContainer.setParameterValue(Float.parseFloat(getFirstFormByParameterName(attributeContainer.getParameterName())));
            } catch (NumberFormatException nfe) {
            }

            fill(attributeContainer);
        }
    }

    private void fillTimestamp(RibrestAttributeContainer attributeContainer) {
        if (attributeContainer.getAttribute().getType().equals(Timestamp.class)) {
            try {
                attributeContainer.setParameterValue(new Timestamp(Long.parseLong(requestMaps.getFormMap().getFirst(attributeContainer.getParameterName()))));
            } catch (NumberFormatException nfe) {
            }

            fill(attributeContainer);
        }
    }

    private void fillModelAttribute(RibrestAttributeContainer attributeContainer) {
        Field attribute = attributeContainer.getAttribute();
        if (attribute.isAnnotationPresent(OneToOne.class)
                && "".equals(attribute.getAnnotation(OneToOne.class).mappedBy())
                || attribute.isAnnotationPresent(ManyToOne.class)) {
            try {
                attribute.setAccessible(true);
                Object object = formContainsEntityModel(attribute.getName())
                        ? attribute.getType().newInstance() : null;
                if (object != null && object instanceof Model) {
                    fillModel((Model) object, attribute.getName());
                } else if (object != null && !(object instanceof Model)) {
                    RibrestLog.log(new StringBuilder(object.getClass().getSimpleName()).append(" is not a model. It won't be filled in.").toString());
                }
                attribute.set(attributeContainer.getParentInstance(), object);
            } catch (Exception e) {
                throw new RibrestDefaultException("error while filling model: " + e.getMessage());
            }
        }
    }

    private void fillCollectionOfModelAttribute(RibrestAttributeContainer attributeContainer) {
        Field attribute = attributeContainer.getAttribute();
        if ((attribute.isAnnotationPresent(OneToMany.class)
                && "".equals(attribute.getAnnotation(OneToMany.class).mappedBy()))
                || (attribute.isAnnotationPresent(ManyToMany.class)
                && "".equals(attribute.getAnnotation(ManyToMany.class).mappedBy()))) {
            Collection models = RibrestUtils.getCollectionInstance(attribute.getType());
            Class<Model> modelClass = RibrestUtils.extractCollectionTypedClassFromCollectionAttribute(attribute);
            List<String> modelIds = getAllChildIds(attributeContainer.getParameterName());
            modelIds = modelIds == null ? new ArrayList() : modelIds;
            modelIds.forEach(stringModelId -> {
                try {
                    Model modelInstance = modelClass.newInstance();
                    modelInstance.setId(Long.parseLong(stringModelId));
                    models.add(modelInstance);
                } catch (Exception e) {
                    throw new RibrestDefaultException(new StringBuilder("error while filling model collection element: ").append(e.getMessage()).toString());
                }
            });
            try {
                attribute.setAccessible(true);
                attribute.set(attributeContainer.getParentInstance(), models);
            } catch (Exception e) {
                throw new RibrestDefaultException(new StringBuilder("error while creating the collection attribute: ").append(e.getMessage()).toString());
            }
        }
    }

    private void fill(RibrestAttributeContainer attributeContainer) {
        Field attribute = attributeContainer.getAttribute();
        try {
            attribute.setAccessible(true);
            if (attribute.get(attributeContainer.getParentInstance()) == null) {

                attribute.set(attributeContainer.getParentInstance(), attributeContainer.getParameterValue());
            }
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException e) {
            throw new RibrestDefaultException("couldn't fill attribute");
        }
    }

    private boolean formContainsEntityModel(String parameterName) {
        return requestMaps.getFormMap().keySet().stream().anyMatch(key -> key.contains(parameterName));
    }

    private List<String> getAllChildIds(String childName) {
        return requestMaps.getFormMap().get(childName.concat(".").concat("id"));
    }

    private String getFirstFormByParameterName(String parameterName) {
        return requestMaps.getFormMap().getFirst(parameterName);
    }

    private boolean checkClassType(RibrestAttributeContainer attributeContainer, Class clazz) {
        return attributeContainer.getAttribute().getType().equals(clazz);
    }

    private String getInonffensiveExceptionMessageWhileFilling(Field field) {
        return new StringBuilder("An inoffensive exception occurred while filling ").append(field.getName()).append(" field in the ").append(model.getClass().getSimpleName()).append(" model.").toString();
    }

}
