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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.security.Key;
import java.sql.Timestamp;

public class RibrestJWT {

    private static final Key API_SECRET_KEY = RibrestUtils.RibrestTokens.getNewSecretKey();

    public String create(RibrestJWTPayload payload) {
        return Jwts.builder()
                .setIssuedAt(new Timestamp(System.currentTimeMillis()))
                .setExpiration(payload.getExpiration())
                .setAudience(payload.getAudience())
                .setIssuer(payload.getIssuer())
                .setSubject(payload.getSubject())
                .signWith(API_SECRET_KEY)
                .compact();
    }

    public Jws<Claims> decode(String token) throws Exception {
        return Jwts.parser().setSigningKey(API_SECRET_KEY).parseClaimsJws(token);
    }
    
    public final Key getCurrentAPISecretKey(){
        return API_SECRET_KEY;
    }

}
