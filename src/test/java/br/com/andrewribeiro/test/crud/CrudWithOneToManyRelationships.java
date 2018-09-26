package br.com.andrewribeiro.test.crud;

import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.test.RibrestTest;
import br.com.andrewribeiro.test.crud.models.FatherModel;
import br.com.andrewribeiro.test.crud.models.ModelWithOneToManyRelationship;
import br.com.andrewribeiro.test.crud.models.SonModel;
import br.com.andrewribeiro.test.structure.models.ModelWithOneToOneRelationship;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Ignore;
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
        post(ModelWithOneToManyRelationship.class, new Form(mvm));
        wasCreated();
        logResponse();
    }

    @Test
//    @Ignore
    public void postingModelWithOneToManyRelationshipWithChildren() {
        MultivaluedMap mvm = new MultivaluedHashMap();
        mvm.add("name", "Parent Name");
        mvm.add("children.name", "First Child"); //Will not be inserted
        mvm.add("children.name", "Second Child"); //will not be inserted
        mvm.add("children.third", "Third Child"); //will not be inserted

        post(ModelWithOneToManyRelationship.class, new Form(mvm));
        wasCreated();
        logResponse();
    }

    @Test
//    @Ignore
    public void postingModelWithOneToManyRelationshipWithInexistentChildren() {
        MultivaluedMap mvm = new MultivaluedHashMap();
        mvm.add("name", "Parent Name that will have inexistent children");
        mvm.add("children.id", "99999");
        post(ModelWithOneToManyRelationship.class, new Form(mvm));
        wasPreConditionFailed();
        assertContainsPieceOfJson("The child model ChildModel identified by 99999 was not found.");
        logResponse();

    }

    @Test
//    @Ignore
    public void postingModelWithOneToManyRelationshipWithExistentChildren() throws JSONException {

        MultivaluedMap mvm = new MultivaluedHashMap();
        mvm.add("child.name", "Child Name");
        post(ModelWithOneToOneRelationship.class, new Form(mvm));
        wasCreated();
        logResponse();
        JSONObject jsonModel = new JSONObject(responseText);
        long childId = jsonModel.getJSONObject("holder").getJSONArray("models").getJSONObject(0).getJSONObject("child").getLong("id");
        String stringChildId = String.valueOf(childId);
        mvm = new MultivaluedHashMap();
        mvm.add("name", "Parent Name that will have existent children");
        mvm.add("children.id", stringChildId);

        post(ModelWithOneToManyRelationship.class, new Form(mvm));
        wasCreated();
        logResponse();
    }
    
    @Test
//    @Ignore
    public void getFathersAndItsKids() throws JSONException {

        MultivaluedMap<String, String> mvm = new MultivaluedHashMap<>();
        MultivaluedHashMap<String, String> kidsId = new MultivaluedHashMap<>();

        for (int i = 0; i < 5; i++) {
            mvm.putSingle("name", "Kid " + i);
            post(SonModel.class, new Form(mvm));
            wasCreated();
            logResponse();
            JSONObject kidsResponseJson = new JSONObject(responseText);
            kidsId.add("kids.id", String.valueOf(kidsResponseJson.getJSONObject("holder").getJSONArray("models").getJSONObject(0).getInt("id")));
        }

        post(FatherModel.class, new Form(kidsId));
        wasCreated();
        logResponse();
        
        get(FatherModel.class);
        wasOk();
        logResponse();

    }

    @Test
//    @Ignore
    public void getKidsAndItsFather() throws JSONException {

        getFathersAndItsKids();

        get(SonModel.class);
        wasOk();
        logResponse();
    }
    
    @Test
//    @Ignore
    public void getSingleInexistentFather() throws RibrestDefaultException{        
        
        get(FatherModel.class, "/99999");
        wasNoContent();        
        logResponse();
    }
    
    @Test
//    @Ignore
    public void getSingleExistentFather() throws JSONException, RibrestDefaultException{
        
        post(FatherModel.class, new Form());
        wasCreated();
        logResponse();
        String fatherId = String.valueOf(new JSONObject(responseText).getJSONObject("holder").getJSONArray("models").getJSONObject(0).getLong("id"));
        
        get(FatherModel.class, "/" + fatherId);
        wasOk();
        logResponse();
    }
}
