/*
 * The MIT License
 *
 * Copyright 2019 ribeiro.
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
package br.com.andrewribeiro.ribrest.core.applisteners;

import br.com.andrewribeiro.ribrest.Ribrest;
import br.com.andrewribeiro.ribrest.core.annotations.RibrestAppListener;
import br.com.andrewribeiro.ribrest.logs.RibrestLog;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Andrew Ribeiro
 */
@RibrestAppListener(order = -1)
public class RibrestAlarm extends AbstractAppListener{
    
    private final Timer timer = new Timer();
    @Override
    public void init() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println(new StringBuilder(ribrestInstance.getCompleteAppUrl()).append("sleep").toString());
                ClientBuilder.newClient().target(new StringBuilder(ribrestInstance.getCompleteAppUrl()).append("sleep").toString()).request(MediaType.APPLICATION_JSON).get();
                RibrestLog.log("Alarming Ribrest to not fall in sleep.");
            }
        }, 1000*60, 1000*60*20);
    }

    @Override
    public void destroy() {
        RibrestLog.log("Destroying Ribrest Alarm...");
        timer.cancel();
    }
    
}
