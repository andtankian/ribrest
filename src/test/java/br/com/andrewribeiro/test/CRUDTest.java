package br.com.andrewribeiro.test;

import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import br.com.andrewribeiro.test.models.ConcreteModelMapped;
import br.com.andrewribeiro.test.models.ModelCrud;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Andrew Ribeiro
 */
public class CRUDTest extends RibrestTest {

    @Test
    public void testGetModelMapped() throws JSONException {

        Response r = get(ConcreteModelMapped.class);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), r.getStatus());
    }

    @Test
    public void testCreateModel() throws JSONException {

        Response r = post(ModelCrud.class, new Form("name", "Andrew Ribeiro"));

        assertEquals(201, r.getStatus());
    }

    @Test
    public void testUpdateModel() throws JSONException, RibrestDefaultException {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<>();
        parameters.add("name", "Andrew Ribeiro Santos");
        Response r = put(RibrestUtils.getResourceName(ModelCrud.class) + "/1", new Form(parameters));
        
        assertEquals(200, r.getStatus());
    }

}
