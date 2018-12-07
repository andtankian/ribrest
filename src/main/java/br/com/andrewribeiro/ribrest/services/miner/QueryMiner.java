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

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;

/**
 *
 * @author Andrew Ribeiro
 */
public class QueryMiner {
    
    public Map<String, Integer> extractLimitAndOffset(MultivaluedMap<String, String> query){
        Map<String, Integer> limitAndOffset = new HashMap<>();
        limitAndOffset.put("limit", getIntegerFromStringOrNumber(query.getFirst("limit"), 5));
        limitAndOffset.put("offset", getIntegerFromStringOrNumber(query.getFirst("offset"), 0));
        return limitAndOffset;
    }
    
    private Integer getIntegerFromStringOrNumber(String stringInteger, Integer number){
        Integer integer;
        try {
            integer = isInfinity(stringInteger) ? -1 : Integer.parseInt(stringInteger);
        }catch(Exception e){
            integer = number;
        }
        
        return integer;
    }
    
    private Boolean isInfinity(String stringInteger){
        return "infinity".equalsIgnoreCase(stringInteger);
    }
}
