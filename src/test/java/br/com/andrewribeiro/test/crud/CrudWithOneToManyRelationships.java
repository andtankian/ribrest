package br.com.andrewribeiro.test.crud;

import br.com.andrewribeiro.test.RibrestTest;
import br.com.andrewribeiro.test.crud.models.ModelWithOneToManyRelationship;
import br.com.andrewribeiro.test.structure.models.ModelWithOneToOneRelationship;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import junit.framework.Assert;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

/**
 *
 * @author Andrew Ribeiro
 */
public class CrudWithOneToManyRelationships extends RibrestTest {

    @Test
//    @Ignore
    public void postingModelWithOneToManyRelationshipButMissingChildren() {

        MultivaluedMap mvm = new MultivaluedHashMap();
        mvm.add("name", "Parent Name");

        Response response = post(ModelWithOneToManyRelationship.class, new Form(mvm));

        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
//    @Ignore
    public void postingModelWithOneToManyRelationshipWithChildren() {

        MultivaluedMap mvm = new MultivaluedHashMap();
        mvm.add("name", "Parent Name");
        mvm.add("children.name", "First Child"); //Will not be inserted
        mvm.add("children.name", "Second Child"); //will not be inserted
        mvm.add("children.third", "Third Child"); //will not be inserted

        Response response = post(ModelWithOneToManyRelationship.class, new Form(mvm));

        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
//    @Ignore
    public void postingModelWithOneToManyRelationshipWithInexistentChildren() {
        MultivaluedMap mvm = new MultivaluedHashMap();
        mvm.add("name", "Parent Name that will have inexistent children");
        mvm.add("children.id", "99999");

        Response response = post(ModelWithOneToManyRelationship.class, new Form(mvm));

        Assert.assertEquals(Response.Status.PRECONDITION_FAILED.getStatusCode(), response.getStatus());
    }

    @Test
    public void postingModelWithOneToManyRelationshipWithExistentChildren() throws JSONException {

        MultivaluedMap mvm = new MultivaluedHashMap();
        mvm.add("child.name", "Child Name");
        Response response = post(ModelWithOneToOneRelationship.class, new Form(mvm));
        
        String responseText = response.readEntity(String.class);
        
        JSONObject jsonModel = new JSONObject(responseText);
        String childId = jsonModel.getJSONObject("holder").getJSONArray("models").getJSONObject(0).getJSONObject("child").getString("id");
        
        mvm = new MultivaluedHashMap();
        mvm.add("name", "Parent Name that will have existent children");
        mvm.add("children.id", childId);

        response = post(ModelWithOneToManyRelationship.class, new Form(mvm));

        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }
}
