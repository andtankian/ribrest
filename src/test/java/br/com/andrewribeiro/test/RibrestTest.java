package br.com.andrewribeiro.test;

import br.com.andrewribeiro.ribrest.Ribrest;
import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 *
 * @author Andrew Ribeiro
 */
public class RibrestTest {

    private final static String APP_URL = "http://localhost:2007/crudtest/";

    protected final Client c = ClientBuilder.newClient();

    public Response get(Class resource) {
        WebTarget wt = buildWebTarget(resource);
        return wt.request(MediaType.APPLICATION_JSON).get();
    }
    
    public Response get(Class resource, String path) throws RibrestDefaultException{
        WebTarget wt = buildWebTarget(APP_URL + RibrestUtils.getResourceName(resource) + path);
        return wt.request(MediaType.APPLICATION_JSON).get();
    }

    public Response post(Class resource, Form form) {
        WebTarget wt = buildWebTarget(resource);
        return wt.request(MediaType.APPLICATION_JSON).post(Entity.form(form));
    }

    public Response put(String path, Form form) throws RibrestDefaultException {
        WebTarget wt = buildWebTarget(APP_URL + path);
        return wt.request(MediaType.APPLICATION_JSON).put(Entity.form(form));
    }

    public Response delete(Class resource, Form form) {
        WebTarget wt = buildWebTarget(resource);
        return wt.request(MediaType.APPLICATION_JSON).delete();
    }

    @BeforeClass
    public static void before() {
        Ribrest.getInstance().debug(true).appBaseUrl(APP_URL).init();
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

    private WebTarget buildWebTarget(String path) {
        WebTarget wt = null;
        wt = c.target(path);
        return wt;
    }

}
