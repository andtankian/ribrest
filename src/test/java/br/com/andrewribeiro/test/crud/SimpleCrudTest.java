package br.com.andrewribeiro.test.crud;

import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import br.com.andrewribeiro.test.RibrestTest;
import br.com.andrewribeiro.test.structure.models.ConcreteModel;
import br.com.andrewribeiro.test.crud.models.ModelCrud;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Andrew Ribeiro
 */
public class SimpleCrudTest extends RibrestTest {

    @Test
    //@Ignore
    public void testGetModelMapped() throws JSONException {

        Response r = get(ConcreteModel.class);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), r.getStatus());
    }

    @Test
    //@Ignore
    public void testCreateModel() throws JSONException {

        Response r = post(ModelCrud.class, new Form("name", "Andrew Ribeiro"));

        assertEquals(201, r.getStatus());
    }

    @Test
    //@Ignore
    public void testUpdateModel() throws JSONException, RibrestDefaultException {
        MultivaluedMap<String, String> mvm = new MultivaluedHashMap<>();
        mvm.add("name", "Andrew Ribeiro");
        Response responseForPost = post(ModelCrud.class, new Form(mvm));
        
        assertEquals(Response.Status.CREATED.getStatusCode(), responseForPost.getStatus());
        
        String responseForPostString = responseForPost.readEntity(String.class);
        
        JSONObject jsonObjectForPost = new JSONObject(responseForPostString);
        String idModel = String.valueOf(jsonObjectForPost.getJSONObject("holder").getJSONArray("models").getJSONObject(0).getInt("id"));
        mvm.putSingle("name", "Andrew Ribeiro Santos");
        Response r = put(RibrestUtils.getResourceName(ModelCrud.class) + "/" + idModel, new Form(mvm));
        
        assertEquals(200, r.getStatus());
    }

}
