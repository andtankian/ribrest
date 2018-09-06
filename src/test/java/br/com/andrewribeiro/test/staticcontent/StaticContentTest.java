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
package br.com.andrewribeiro.test.staticcontent;

import br.com.andrewribeiro.test.RibrestTest;
import javax.ws.rs.core.Response;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Andrew Ribeiro
 */
public class StaticContentTest extends RibrestTest{
    
    @Test
    public void getNonExistentImage(){
        Response response = get("http://localhost:2007/static/nonexistent.jpg");
        Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void getExistentImage(){
        Response response = get("http://localhost:2007/static/existent.jpg");
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
    
}