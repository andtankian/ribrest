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
package br.com.andrewribeiro.test.security;

import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import br.com.andrewribeiro.test.security.models.SecureModel;
import br.com.andrewribeiro.test.RibrestTest;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 *
 * @author Andrew Ribeiro
 */
public class SecuringEndpointTest extends RibrestTest {

    @Test
    public void missingToken() throws RibrestDefaultException {

        Response response = get(SecureModel.class, "/secure");

        Assert.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());

        String responseText = response.readEntity(String.class);

        JSONAssert.assertEquals("{\"cause\":\"" + RibrestUtils.RibrestDefaultResponses.UNAUTHORIZED_MISSING_TOKEN + "\"}", responseText, JSONCompareMode.STRICT);
    }
    
    @Test
    public void invalidToken() throws RibrestDefaultException {
        MultivaluedMap mvm = new MultivaluedHashMap();
        mvm.add("Authorization", "Bearer HDSHAJK432BABK4K2NKNM.4Y3892NJKNFKDSLBFHDB.YGYAOBCNXMAH3782");
        Response response = getWithHeaders(APP_URL + "securemodels/secure", mvm);

        Assert.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());

        String responseText = response.readEntity(String.class);

        JSONAssert.assertEquals("{\"cause\":\"" + RibrestUtils.RibrestDefaultResponses.UNAUTHORIZED_INVALID_TOKEN + "\"}", responseText, JSONCompareMode.STRICT);
    }

}
