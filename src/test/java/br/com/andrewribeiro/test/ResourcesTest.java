package br.com.andrewribeiro.test;

import br.com.andrewribeiro.test.models.NotAIModelSubClass;
import br.com.andrewribeiro.ribrest.Ribrest;
import br.com.andrewribeiro.test.models.AbstractModel;
import br.com.andrewribeiro.test.models.NotImplementsIModelAbstractMethods;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 *
 * @author ribeiro
 */
public class ResourcesTest {

    private final static String APP_URL = "http://localhost:2007/ribrestapp/";

    private final Client c = ClientBuilder.newClient();

    @BeforeClass
    public static void before() {
        init();
    }

    @Test
    public void testNotAIModelSubClass() throws JSONException {

        WebTarget wt = buildWebTarget(NotAIModelSubClass.class);

        Response r = wt.request(MediaType.APPLICATION_JSON).get(Response.class);

        assertEquals(417, r.getStatus());

        JSONAssert.assertEquals("{\"cause\": \"The created resource: " + NotAIModelSubClass.class.getSimpleName().toLowerCase() + " does not implement IModel.\nRibrest can't operate in this class (yet).\"}}", r.readEntity(String.class), JSONCompareMode.LENIENT);
    }

    @Test
    public void testNotImplementesIModelAbstractMethods() throws JSONException {
        WebTarget wt = buildWebTarget(NotImplementsIModelAbstractMethods.class);

        Response r = wt.request(MediaType.APPLICATION_JSON).get(Response.class);

        assertEquals(417, r.getStatus());
        
        JSONAssert.assertEquals("{\"cause\": \"The created resource: " + NotImplementsIModelAbstractMethods.class.getSimpleName().toLowerCase() + " is a IModel subclass but not implements its abstract methods.\"}}", r.readEntity(String.class), JSONCompareMode.LENIENT);
    }
    
    @Test
    public void testAbstractModel() throws JSONException {
        WebTarget wt = buildWebTarget(AbstractModel.class);

        Response r = wt.request(MediaType.APPLICATION_JSON).get(Response.class);

        assertEquals(417, r.getStatus());
        
        JSONAssert.assertEquals("{\"cause\": \"The created resource: " + AbstractModel.class.getSimpleName().toLowerCase() + " can't be an abstract class.\"}}", r.readEntity(String.class), JSONCompareMode.LENIENT);
    }
    
    

    private static void init() {
        Ribrest.getInstance().init();
    }

    private WebTarget buildWebTarget(Class sub) {
        return c.target(APP_URL + (sub.getSimpleName() + "s").toLowerCase());
    }

    @AfterClass
    public static void after() {
        Ribrest.getInstance().shutdown();
    }
}
