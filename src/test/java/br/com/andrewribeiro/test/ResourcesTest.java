package br.com.andrewribeiro.test;

import br.com.andrewribeiro.test.models.NotAIModelSubClass;
import br.com.andrewribeiro.ribrest.Ribrest;
import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import br.com.andrewribeiro.test.models.AbstractModel;
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
import br.com.andrewribeiro.test.models.NotAnnotatedWithRibrestModelAnnotationModel;
import org.junit.BeforeClass;
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
    public void testNotAIModelSubClass() throws JSONException, RibrestDefaultException {

        WebTarget wt = buildWebTarget(NotAIModelSubClass.class);

        Response r = wt.request(MediaType.APPLICATION_JSON).get(Response.class);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), r.getStatus());

        JSONAssert.assertEquals("{\"cause\": \"The created resource: " + getResourceName(NotAIModelSubClass.class) + " does not implement IModel.\nRibrest can't operate in this class (yet).\"}}", r.readEntity(String.class), JSONCompareMode.LENIENT);
    }

    @Test
    public void testNotImplementesIModelAbstractMethods() throws JSONException, RibrestDefaultException {
        WebTarget wt = buildWebTarget(NotImplementsIModelAbstractMethods.class);

        Response r = wt.request(MediaType.APPLICATION_JSON).get(Response.class);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), r.getStatus());

        JSONAssert.assertEquals("{\"cause\": \"The created resource: " + getResourceName(NotImplementsIModelAbstractMethods.class) + " is a IModel subclass but not implements its abstract methods.\"}}", r.readEntity(String.class), JSONCompareMode.LENIENT);
    }

    @Test
    public void testAbstractModel() throws JSONException, RibrestDefaultException {
        WebTarget wt = buildWebTarget(AbstractModel.class);

        Response r = wt.request(MediaType.APPLICATION_JSON).get(Response.class);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), r.getStatus());

        JSONAssert.assertEquals("{\"cause\": \"The created resource: " + getResourceName(AbstractModel.class) + " can't be an abstract class.\"}}", r.readEntity(String.class), JSONCompareMode.LENIENT);
    }

    @Test
    public void testConcreteModelNotMapped() throws JSONException, RibrestDefaultException {
        WebTarget wt = buildWebTarget(ConcreteModelNotMapped.class);

        Response r = wt.request(MediaType.APPLICATION_JSON).get(Response.class);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), r.getStatus());

        JSONAssert.assertEquals("{\"cause\": \"The created resource: " + getResourceName(ConcreteModelNotMapped.class) + " isn't an entity. Try to annotate it with @Entity.\"}}", r.readEntity(String.class), JSONCompareMode.LENIENT);
    }
    
     @Test
    public void testNotAnnotatedWithRibrestModelAnnotationModel() {
        try {
            WebTarget wt = buildWebTarget(NotAnnotatedWithRibrestModelAnnotationModel.class);
        }catch(RuntimeException rte){
            assertEquals("Ribrest could'nt get a valid resource name by the class: " + NotAnnotatedWithRibrestModelAnnotationModel.class.getName() + "\nHave you annotated it with @RibrestModel?",
                    rte.getMessage());
        }
    }
   

    private static void init() {
        Ribrest.getInstance().debug(true).init();
    }

    private WebTarget buildWebTarget(Class sub) {
        WebTarget wt = null;
        try {
            wt = c.target(APP_URL + RibrestUtils.getResourceName(sub));
        } catch (Exception e) {
            if (e instanceof RibrestDefaultException) {
                throw new RuntimeException(((RibrestDefaultException) e).getError());
            }
        }
        return wt;
    }

    @AfterClass
    public static void after() {
        Ribrest.getInstance().shutdown();
    }
}
