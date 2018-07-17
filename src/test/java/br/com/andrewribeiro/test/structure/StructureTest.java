package br.com.andrewribeiro.test.structure;

import br.com.andrewribeiro.test.structure.models.NotAModelSubclass;
import br.com.andrewribeiro.ribrest.Ribrest;
import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import br.com.andrewribeiro.test.models.AbstractModel;
import br.com.andrewribeiro.test.structure.models.ConcreteModelButNotAJPAEntity;
import br.com.andrewribeiro.test.structure.models.ModelNotImplementingAbstractMethods;
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
import br.com.andrewribeiro.test.structure.models.NotAnnotatedWithRibrestModelAnnotation;
import org.junit.BeforeClass;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 *
 * @author Andrew Ribeiro
 */
public class StructureTest {

    private final static String APP_URL = "http://localhost:2007/ribrestapp/";

    private final Client c = ClientBuilder.newClient();

    @BeforeClass
    public static void before() {
        init();
    }

    @Test
    public void notAModelSubclass() throws JSONException, RibrestDefaultException {

        WebTarget wt = buildWebTarget(NotAModelSubclass.class);

        Response r = wt.request(MediaType.APPLICATION_JSON).get(Response.class);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), r.getStatus());

        JSONAssert.assertEquals("{\"cause\": \"The created resource: " + getResourceName(NotAModelSubclass.class) + " does not implement IModel.\nRibrest can't operate in this class (yet).\"}}", r.readEntity(String.class), JSONCompareMode.LENIENT);
    }

    @Test
    public void modelNotImplementingAbstractMethods() throws JSONException, RibrestDefaultException {
        WebTarget wt = buildWebTarget(ModelNotImplementingAbstractMethods.class);

        Response r = wt.request(MediaType.APPLICATION_JSON).get(Response.class);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), r.getStatus());

        JSONAssert.assertEquals("{\"cause\": \"The created resource: " + getResourceName(ModelNotImplementingAbstractMethods.class) + " is a IModel subclass but not implements its abstract methods.\"}}", r.readEntity(String.class), JSONCompareMode.LENIENT);
    }

    @Test
    public void abstractModel() throws JSONException, RibrestDefaultException {
        WebTarget wt = buildWebTarget(AbstractModel.class);

        Response r = wt.request(MediaType.APPLICATION_JSON).get(Response.class);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), r.getStatus());

        JSONAssert.assertEquals("{\"cause\": \"The created resource: " + getResourceName(AbstractModel.class) + " can't be an abstract class.\"}}", r.readEntity(String.class), JSONCompareMode.LENIENT);
    }

    @Test
    public void ConcreteModelButNotAJPAEntity() throws JSONException, RibrestDefaultException {
        WebTarget wt = buildWebTarget(ConcreteModelButNotAJPAEntity.class);

        Response r = wt.request(MediaType.APPLICATION_JSON).get(Response.class);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), r.getStatus());

        JSONAssert.assertEquals("{\"cause\": \"The created resource: " + getResourceName(ConcreteModelButNotAJPAEntity.class) + " isn't an entity. Try to annotate it with @Entity.\"}}", r.readEntity(String.class), JSONCompareMode.LENIENT);
    }
    
     @Test
    public void notAnnotatedWithRibrestModelAnnotation() {
        try {
            WebTarget wt = buildWebTarget(NotAnnotatedWithRibrestModelAnnotation.class);
        }catch(RuntimeException rte){
            assertEquals("Ribrest could'nt get a valid resource name by the class: " + NotAnnotatedWithRibrestModelAnnotation.class.getName() + "\nHave you annotated it with @RibrestModel?",
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
