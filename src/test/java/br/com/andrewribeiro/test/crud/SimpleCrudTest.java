package br.com.andrewribeiro.test.crud;

import br.com.andrewribeiro.test.RibrestTest;
import br.com.andrewribeiro.test.structure.models.ConcreteModel;
import br.com.andrewribeiro.test.crud.models.ModelCrud;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;


/**
 *
 * @author Andrew Ribeiro
 */
public class SimpleCrudTest extends RibrestTest {

    @Test
    //@Ignore
    public void testGetModelMapped() throws JSONException {
        get(ConcreteModel.class);
        wasNoContent();
        logResponse();
    }

    @Test
    //@Ignore
    public void testCreateModel() throws JSONException {
        post(ModelCrud.class, new Form("name", "Andrew Ribeiro"));
        wasCreated();
        logResponse();
    }

    @Test
    //@Ignore
    public void testUpdateModel() throws JSONException {
        MultivaluedMap<String, String> mvm = new MultivaluedHashMap<>();
        mvm.add("name", "Andrew Ribeiro");
        post(ModelCrud.class, new Form(mvm));
        wasCreated();
        logResponse();
        
        JSONObject jsonObjectForPost = new JSONObject(responseText);
        String idModel = String.valueOf(jsonObjectForPost.getJSONObject("holder").getJSONArray("models").getJSONObject(0).getInt("id"));
        mvm.putSingle("name", "Andrew Ribeiro Santos");
        put(ModelCrud.class, "/" + idModel, new Form(mvm));
        wasOk();
        logResponse();        
    }

}
