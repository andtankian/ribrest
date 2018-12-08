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

package br.com.andrewribeiro.test.structure.miners;

import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.logs.RibrestLog;
import br.com.andrewribeiro.ribrest.services.miner.Miner;
import java.util.List;
import org.glassfish.jersey.server.ContainerRequest;

/**
 *
 * @author Andrew Ribeiro
 */
public class CustomMiner implements Miner{

    @Override
    public void mineRequest(ContainerRequest containerRequest) {
        RibrestLog.log("Executing a custom miner.");
        throw new RibrestDefaultException("Stopping natural flow.");
        
    }

    @Override
    public List extractIgnoredFields() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
