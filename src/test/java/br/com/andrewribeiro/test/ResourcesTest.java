package br.com.andrewribeiro.test;

import br.com.andrewribeiro.test.models.NotAIModelSubClass;
import br.com.andrewribeiro.ribrest.Ribrest;
import br.com.andrewribeiro.test.models.AbstractModel;
import br.com.andrewribeiro.test.models.ConcreteModelMapped;
import br.com.andrewribeiro.test.models.ConcreteModelNotMapped;
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
import static br.com.andrewribeiro.ribrest.utils.RibrestUtils.*;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 *
 * @author Andrew Ribeiro
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

        JSONAssert.assertEquals("{\"cause\": \"The created resource: " + getResourceName(NotAIModelSubClass.class) + " does not implement IModel.\nRibrest can't operate in this class (yet).\"}}", r.readEntity(String.class), JSONCompareMode.LENIENT);
    }

    @Test
    public void testNotImplementesIModelAbstractMethods() throws JSONException {
        WebTarget wt = buildWebTarget(NotImplementsIModelAbstractMethods.class);

        Response r = wt.request(MediaType.APPLICATION_JSON).get(Response.class);

        assertEquals(417, r.getStatus());

        JSONAssert.assertEquals("{\"cause\": \"The created resource: " + getResourceName(NotImplementsIModelAbstractMethods.class) + " is a IModel subclass but not implements its abstract methods.\"}}", r.readEntity(String.class), JSONCompareMode.LENIENT);
    }

    @Test
    public void testAbstractModel() throws JSONException {
        WebTarget wt = buildWebTarget(AbstractModel.class);

        Response r = wt.request(MediaType.APPLICATION_JSON).get(Response.class);

        assertEquals(417, r.getStatus());

        JSONAssert.assertEquals("{\"cause\": \"The created resource: " + getResourceName(AbstractModel.class) + " can't be an abstract class.\"}}", r.readEntity(String.class), JSONCompareMode.LENIENT);
    }

    @Test
    public void testConcreteModelNotMapped() throws JSONException {
        WebTarget wt = buildWebTarget(ConcreteModelNotMapped.class);

        Response r = wt.request(MediaType.APPLICATION_JSON).get(Response.class);

        assertEquals(417, r.getStatus());
        
        JSONAssert.assertEquals("{\"cause\": \"The created resource: " + getResourceName(ConcreteModelNotMapped.class) + " isn't an entity. Try to annotate it with @Entity.\"}}", r.readEntity(String.class), JSONCompareMode.LENIENT);
    }
    
    @Test
    public void testConcreteModelMapped() throws JSONException {
        WebTarget wt = buildWebTarget(ConcreteModelMapped.class);

        Response r = wt.request(MediaType.APPLICATION_JSON).get(Response.class);

        assertEquals(200, r.getStatus());
    }

    private static void init() {
        Ribrest.getInstance().setDebug(true).init();
    }

    private WebTarget buildWebTarget(Class sub) {
        return c.target(APP_URL + (sub.getSimpleName() + "s").toLowerCase());
    }

    @AfterClass
    public static void after() {
        Ribrest.getInstance().shutdown();
    }
}
