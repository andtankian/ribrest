package br.com.andrewribeiro.test.crud;

import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.test.RibrestTest;
import br.com.andrewribeiro.test.crud.models.ModelChildWithOneToOneRelationship;
import br.com.andrewribeiro.test.crud.models.ModelParentWithBidirectionalRelationship;
import br.com.andrewribeiro.test.crud.models.ModelParentWithOneToOneRelationship;
import br.com.andrewribeiro.test.crud.models.ModelWithBidirectionalRelationshipAndSameChildClassType;
import br.com.andrewribeiro.test.structure.models.ModelWithOneToOneRelationship;
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
public class CrudWithOneToOneRelationships extends RibrestTest{
    
    @Test
//    @Ignore
    public void postingModelWithOneToOneRelationship(){
        
        MultivaluedMap mvm = new MultivaluedHashMap();
        mvm.add("child.name", "Child Name");
        post(ModelWithOneToOneRelationship.class, new Form(mvm));
        wasCreated();
        logResponse();
    }
    
    @Test
//    @Ignore
    public void puttingAnInexistentModel() throws RibrestDefaultException, JSONException{
        MultivaluedMap mvm = new MultivaluedHashMap();
        mvm.add("name", "New Parent Name");
        put(ModelWithOneToOneRelationship.class, "/99999", new Form(mvm));
        wasPreConditionFailed();        
        assertContainsPieceOfJson("The model 99999 was not found.");
        logResponse();
        
    }
    
    @Test
//    @Ignore
    public void postingModelWithBidirectionalRelationshipButMissingChild() {
        MultivaluedMap mvm = new MultivaluedHashMap();
        mvm.add("name", "I'm Parent Model");
        post(ModelParentWithBidirectionalRelationship.class, new Form(mvm));
        wasCreated();
        logResponse();
        
    }
    
    @Test
//    @Ignore
    public void gettingModelsWithBidirectionalRelationship(){
        
        for (int i = 0; i < 10; i++) {
            postingModelWithBidirectionalRelationshipButMissingChild();            
        }
        
        get(ModelParentWithBidirectionalRelationship.class);
        wasOk();
        logResponse();
    }
    
    @Test
//    @Ignore
    public void postingModelWithBidirectionWithChild(){
        MultivaluedMap<String, String> mvm = new MultivaluedHashMap<>();
        mvm.add("child", "child");
        post(ModelParentWithBidirectionalRelationship.class,new Form(mvm));
        wasCreated();
        logResponse();
    }
    
    @Test
//    @Ignore
    public void postingModelWithBidirectionalInexistentChild(){
        
        MultivaluedMap<String, String> mvm = new MultivaluedHashMap<>();
        mvm.add("child.id", "99999999");
        post(ModelParentWithBidirectionalRelationship.class, new Form(mvm));
        wasPreConditionFailed();
        logResponse();
    }
    
    @Test
//    @Ignore
    public void postingModelWithBidirectionalRelatinshipWithExistentChild(){
        MultivaluedMap<String, String> mvm = new MultivaluedHashMap<>();
        mvm.add("anyname", "Child Model");
        post(ModelChildWithOneToOneRelationship.class, new Form(mvm));
        wasCreated();
        logResponse();
        mvm.clear();
        JSONObject jsonObject = new JSONObject(responseText);
        long childId = jsonObject.getJSONObject("holder").getJSONArray("models").getJSONObject(0).getLong("id");
        String stringChildId = String.valueOf(childId);
        mvm.add("child.id", stringChildId);
        post(ModelParentWithOneToOneRelationship.class, new Form(mvm));
        wasCreated();
        logResponse();
    }
    @Test
//    @Ignore
    public void postingModelWithBidirectionalRelationshipAndSameClassType(){
        MultivaluedMap mvm = new MultivaluedHashMap();
        
        mvm.add("name", "Parent");
        mvm.add("child1.name", "My name is child 1");
        
        post(ModelWithBidirectionalRelationshipAndSameChildClassType.class, new Form(mvm));
        
        wasInternalServerError();
        logResponse();
    }
}
