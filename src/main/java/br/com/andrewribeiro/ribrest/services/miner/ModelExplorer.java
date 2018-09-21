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
import java.lang.reflect.Field;
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

    private void fillModel(Model model, String attributeName) {
        model.getAllAttributes().forEach(attribute -> {
            String realAttributeName = 
                    attributeName == null ?
                    "" :
                    attributeName.concat(".");
            fillAttribute(new RibrestAttributeContainer(attribute, model, realAttributeName.concat(attribute.getName())));
        });
    }

    private void fillModelIdFromPath() {
        try {
            model.setId(Long.parseLong(requestMaps.getPathMap().getFirst("id")));
        } catch (NumberFormatException nfe) {
        }
    }

    private void fillAttribute(RibrestAttributeContainer attributeContainer) {
        Field attribute = attributeContainer.getAttribute();
        if (attribute.getType() == String.class) {
            fill(attributeContainer);
        } else if (attribute.isAnnotationPresent(OneToOne.class) &&
                "".equals(attribute.getAnnotation(OneToOne.class).mappedBy())) {
            fillEntityAttribute(attributeContainer);
            
        }
    }

    private void fill(RibrestAttributeContainer attributeContainer) {
        Field attribute = attributeContainer.getAttribute();
        try {
            attribute.setAccessible(true);
            if (attribute.get(attributeContainer.getParentInstance()) == null) {
                
                attribute.set(attributeContainer.getParentInstance(), requestMaps.getFormMap().getFirst(attributeContainer.getParameterName()));
            }
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException e) {
            throw new RibrestDefaultException("couldn't fill attribute");
        }
    }
    
    private void fillEntityAttribute(RibrestAttributeContainer attributeContainer){
        Field attribute = attributeContainer.getAttribute();
        try {
            attribute.setAccessible(true);
            Object object = attribute.getType().newInstance();
            if(object instanceof Model){
                fillModel((Model) object, attribute.getName());
                return;
            }
            RibrestLog.log(new StringBuilder(object.getClass().getSimpleName()).append(" is not a model. It won't be filled in.").toString());
        }catch(Exception e){
            throw new RibrestDefaultException("error while filling model: " + e.getMessage());
        }
    }

}
