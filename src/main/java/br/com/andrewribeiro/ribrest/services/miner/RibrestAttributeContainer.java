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

import br.com.andrewribeiro.ribrest.core.model.Model;
import java.lang.reflect.Field;

/**
 *
 * @author Andrew Ribeiro
 */
public class RibrestAttributeContainer {

    public RibrestAttributeContainer(Field attribute, Model parentInstance, String parameterName) {
        this.attribute = attribute;
        this.parentInstance = parentInstance;
        this.parameterName = parameterName;
    }
    
    
    
    private Field attribute;
    private Model parentInstance;
    private String parameterName;

    public Field getAttribute() {
        return attribute;
    }

    public void setAttribute(Field attribute) {
        this.attribute = attribute;
    }

    public Model getParentInstance() {
        return parentInstance;
    }

    public void setParentInstance(Model parentInstance) {
        this.parentInstance = parentInstance;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

}
