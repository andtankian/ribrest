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

import javax.ws.rs.core.MultivaluedMap;

/**
 *
 * @author Andrew Ribeiro
 */
public class RequestMaps {

    public RequestMaps(MultivaluedMap<String, String> formMap, MultivaluedMap<String, String> queryMap, MultivaluedMap<String, String> pathMap, MultivaluedMap<String, String> headerMap) {
        this.formMap = formMap;
        this.queryMap = queryMap;
        this.pathMap = pathMap;
        this.headerMap = headerMap;
    }
    
    

    private MultivaluedMap<String, String> formMap;
    private MultivaluedMap<String, String> queryMap;
    private MultivaluedMap<String, String> pathMap;
    private MultivaluedMap<String, String> headerMap;

    public MultivaluedMap<String, String> getFormMap() {
        return formMap;
    }

    public void setFormMap(MultivaluedMap<String, String> formMap) {
        this.formMap = formMap;
    }

    public MultivaluedMap<String, String> getQueryMap() {
        return queryMap;
    }

    public void setQueryMap(MultivaluedMap<String, String> queryMap) {
        this.queryMap = queryMap;
    }

    public MultivaluedMap<String, String> getPathMap() {
        return pathMap;
    }

    public void setPathMap(MultivaluedMap<String, String> pathMap) {
        this.pathMap = pathMap;
    }

    public MultivaluedMap<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(MultivaluedMap<String, String> headerMap) {
        this.headerMap = headerMap;
    }
}
