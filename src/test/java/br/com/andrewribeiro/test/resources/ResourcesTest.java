package br.com.andrewribeiro.test.resources;

import br.com.andrewribeiro.ribrest.Ribrest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author ribeiro
 */
public class ResourcesTest {

    private final static String APP_URL = "http://localhost:2007/ribrestapp/";

    @Before
    public void before() {
        init();
    }

    @Test
    public void testNotAIModelSubClass() {
        Client c = ClientBuilder.newClient();

        WebTarget wt = c.target(APP_URL + (NotAIModelSubClass.class.getSimpleName() + "s").toLowerCase());

        Response r = wt.request(MediaType.APPLICATION_JSON).get(Response.class);

        assertEquals(417, r.getStatus());        
    }

    public void init() {
        Ribrest.getInstance().init();
    }

    @After
    public void after() {
        Ribrest.getInstance().shutdown();
    }
}
