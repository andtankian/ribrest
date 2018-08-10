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
package br.com.andrewribeiro.ribrest.utils;

import org.json.JSONObject;
/**
 *
 * @author Andrew Ribeiro
 */
public class RibrestDefaultResponses {
    
    public final String UNAUTHORIZED_INVALID_TOKEN = "This token is invalid.";
    public final String UNAUTHORIZED_MISSING_TOKEN = "Token is missing.";
    
    public String getUnauthorizedInvalidToken(){
        return getBuiltJsonString(UNAUTHORIZED_INVALID_TOKEN);
    }
    
    public String getUnauthorizedMissingToken(){
        return getBuiltJsonString(UNAUTHORIZED_MISSING_TOKEN);
    }
    
    private String getBuiltJsonString(String cause){
        return new JSONObject().put("cause", cause).toString();
    }
}
