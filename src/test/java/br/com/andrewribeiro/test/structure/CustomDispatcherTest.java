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
package br.com.andrewribeiro.test.structure;

import br.com.andrewribeiro.test.RibrestTest;
import br.com.andrewribeiro.test.structure.dispatchers.CustomDispatcher;
import br.com.andrewribeiro.test.structure.models.ModelWithCustomDispatcher;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 *
 * @author Andrew Ribeiro
 */
public class CustomDispatcherTest extends RibrestTest{
    
    @Test
    public void customDispatcherReturn(){
        
        Response response = get(ModelWithCustomDispatcher.class);
        
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        String responseText = response.readEntity(String.class);
        
        JSONAssert.assertEquals("{\"message\":\"" + CustomDispatcher.CUSTOM_RETURN_MESSAGE + "\"}", responseText, JSONCompareMode.STRICT);
    }
  
    
}
