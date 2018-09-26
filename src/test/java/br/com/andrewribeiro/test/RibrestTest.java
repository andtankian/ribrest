package br.com.andrewribeiro.test;

import br.com.andrewribeiro.ribrest.Ribrest;
import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.logs.RibrestLog;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 *
 * @author Andrew Ribeiro
 */
public class RibrestTest {

    protected final static String BASE_URL = "http://localhost:2007/";
    protected final static String APP_NAME = "ribrestapp/";
    protected final static String APP_URL = BASE_URL + APP_NAME;

    protected final Client c = ClientBuilder.newClient();
    protected Response response;
    protected String responseText;

    public Response getResponse(Class resource) {
        WebTarget wt = buildWebTarget(resource);
        return wt.request(MediaType.APPLICATION_JSON).get();
    }

    public void get(Class resource) {
        response = getResponse(resource);
        getResponseText();
    }

    public void get(String completePath) {
        WebTarget wt = buildWebTarget(completePath);
        response = wt.request(MediaType.APPLICATION_JSON).get();
    }

    public void get(Class resourceClass, String subpath) {
        WebTarget webTarget = buildWebTarget(resourceClass, subpath);
        response = webTarget.request(MediaType.APPLICATION_JSON).get();
        getResponseText();
    }

    public void getWithQueryParameters(Class resource, String queryParameters) {
        WebTarget wt = buildWebTarget(APP_URL + RibrestUtils.getResourceName(resource) + "?" + queryParameters);
        response = wt.request(MediaType.APPLICATION_JSON).get();
        getResponseText();
    }

    public void getWithHeaders(String path, MultivaluedMap headers) {
        WebTarget wt = buildWebTarget(path);
        response = wt.request(MediaType.APPLICATION_JSON).headers(headers).get();
        getResponseText();
    }

    public void post(Class resourceClass, Form form) {
        response = postResponse(resourceClass, form);
        getResponseText();
    }

    public Response postResponse(Class resource, Form form) {
        WebTarget wt = buildWebTarget(resource);
        return wt.request(MediaType.APPLICATION_JSON).post(Entity.form(form));
    }

    public Response putResponse(String path, Form form) throws RibrestDefaultException {
        WebTarget wt = buildWebTarget(APP_URL + path);
        return wt.request(MediaType.APPLICATION_JSON).put(Entity.form(form));
    }
    
    public void put(Class classResource,String subpath, Form form) {
        WebTarget wt = buildWebTarget(classResource, subpath);
        response = wt.request(MediaType.APPLICATION_JSON).put(Entity.form(form));
        getResponseText();
    }

    public Response delete(Class resource, Form form) {
        WebTarget wt = buildWebTarget(resource);
        return wt.request(MediaType.APPLICATION_JSON).delete();
    }

    protected void logResponse() {
        RibrestLog.logForced("RESPONSE TEXT:" + responseText);
    }

    protected void wasOk() {
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    protected void wasNoContent() {
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    protected void wasCreated() {
        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    protected void wasNotFound() {
        Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    protected void wasPreConditionFailed() {
        Assert.assertEquals(Response.Status.PRECONDITION_FAILED.getStatusCode(), response.getStatus());
    }

    protected void wasUnauthorized() {
        Assert.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    protected void wasInternalServerError() {
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    protected void wasNotAModelError(Class resourceClass) {
        assertContainsPieceOfJson("The created resource: " + RibrestUtils.getResourceName(resourceClass) + " does not implement Model interface.");
    }

    protected void wasModelDoestNotImplementAbstractMethods(Class resourceClass) {
        assertContainsPieceOfJson("The created resource: " + RibrestUtils.getResourceName(resourceClass) + " is a Model but it doesn't implement abstract methods.");
    }

    protected void wasAbstractModel(Class resourceClass) {
        assertContainsPieceOfJson("The created resource: " + RibrestUtils.getResourceName(resourceClass) + " can't be an abstract class.");
    }

    protected void wasNotAnEntity(Class resourceClass) {
        assertContainsPieceOfJson("The created resource: " + RibrestUtils.getResourceName(resourceClass) + " isn't an entity. Try to annotate it with @Entity.");
    }

    protected void assertContainsPieceOfJson(String piece) {
        JSONAssert.assertEquals("{\"cause\":\"" + piece + "\"}", responseText, JSONCompareMode.LENIENT);
    }

    private void getResponseText() {
        responseText = response.readEntity(String.class);
    }

    @BeforeClass
    public static void before() {
        Ribrest.getInstance().debug(true).appBaseUrl(BASE_URL).appName(APP_NAME).init();
    }

//    @AfterClass
//    public static void after() {
//        Ribrest.getInstance().shutdown();
//    }
    private WebTarget buildWebTarget(Class sub) {
        WebTarget wt = null;
        try {
            wt = c.target(APP_URL + RibrestUtils.getResourceName(sub));
        } catch (RibrestDefaultException e) {
            if (e instanceof RibrestDefaultException) {
                throw new RuntimeException(((RibrestDefaultException) e).getError());
            }
        }
        return wt;
    }

    private WebTarget buildWebTarget(Class resourceClass, String subPath) {
        WebTarget wt = null;
        try {
            wt = c.target(APP_URL + RibrestUtils.getResourceName(resourceClass) + subPath);
        } catch (RibrestDefaultException e) {
            if (e instanceof RibrestDefaultException) {
                throw new RuntimeException(((RibrestDefaultException) e).getError());
            }
        }
        return wt;
    }

    private WebTarget buildWebTarget(String path) {
        WebTarget wt = null;
        wt = c.target(path);
        return wt;
    }

}
