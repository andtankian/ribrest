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

package br.com.andrewribeiro.test.patterns;

import br.com.andrewribeiro.test.RibrestTest;
import br.com.andrewribeiro.test.models.AbstractModel;
import br.com.andrewribeiro.test.structure.models.ConcreteModelButNotAJPAEntity;
import br.com.andrewribeiro.test.structure.models.ModelNotImplementingAbstractMethods;
import br.com.andrewribeiro.test.structure.models.NotAModelSubclass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Andrew Ribeiro
 */
public class NotFollowingPatternTest extends RibrestTest{
    
    @Test
    public void notAModel(){
        get(NotAModelSubclass.class);
        wasInternalServerError();
        wasNotAModelError(NotAModelSubclass.class);
        logResponse();
    }
    
    @Test
    public void modelDoesNotImplementAbstractMethods(){
        get(ModelNotImplementingAbstractMethods.class);
        wasInternalServerError();
        wasModelDoestNotImplementAbstractMethods(ModelNotImplementingAbstractMethods.class);
        logResponse();
    }
    
    @Test
    public void abstractModel() {
        get(AbstractModel.class);
        wasInternalServerError();
        wasAbstractModel(AbstractModel.class);
        logResponse();
    }
    
    @Test
    public void notAJPAEntity() {
        get(ConcreteModelButNotAJPAEntity.class);
        wasInternalServerError();
        wasNotAnEntity(ConcreteModelButNotAJPAEntity.class);
        logResponse();
    }

}
