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

package br.com.andrewribeiro.test.crud;

import br.com.andrewribeiro.test.RibrestTest;
import br.com.andrewribeiro.test.crud.models.ModelChildWithManyToManyRelationship;
import br.com.andrewribeiro.test.crud.models.ModelParentWithManyToManyRelationship;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Andrew Ribeiro
 */
public class CrudWithManyToManyRelationship extends RibrestTest{
    MultivaluedMap<String, String> formParameters;

    @Test
//    @Ignore
    public void postManyToManyModelNotMappedBy(){        
        post(ModelParentWithManyToManyRelationship.class, new Form());
        wasCreated();
        logResponse();
    }
    
    @Test
//    @Ignore
    public void postManyToManyModelWithInexistentChildren(){
        MultivaluedMap<String, String> mvm = new MultivaluedHashMap<>();
        mvm.add("children.id", "9999999");
        post(ModelParentWithManyToManyRelationship.class, new Form(mvm));
        wasPreConditionFailed();
        logResponse();
    }
    
    @Test
//    @Ignore
    public void postManyToManyModelWithExistentChildren(){
        formParameters = new MultivaluedHashMap<>();
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            formParameters.putSingle("name", sBuilder.append("Model Many To Many ").append(i).toString());
            sBuilder.delete(0, sBuilder.length());
            post(ModelChildWithManyToManyRelationship.class, new Form(formParameters));
            wasCreated();
            logResponse();
            formParameters.add("children.id", String.valueOf(new JSONObject(responseText).getJSONObject("holder").getJSONArray("models").getJSONObject(0).getLong("id")));
        }
        formParameters.remove("name");
        
        post(ModelParentWithManyToManyRelationship.class, new Form(formParameters));
        wasCreated();
        logResponse();
    }
}
