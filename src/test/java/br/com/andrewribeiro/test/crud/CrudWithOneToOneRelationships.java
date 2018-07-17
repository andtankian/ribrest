package br.com.andrewribeiro.test.crud;

import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import br.com.andrewribeiro.test.RibrestTest;
import br.com.andrewribeiro.test.crud.models.ModelParentWithBidirectionalRelationship;
import br.com.andrewribeiro.test.crud.models.ModelWithBidirectionalRelationshipAndSameChildClassType;
import br.com.andrewribeiro.test.structure.models.ModelWithOneToOneRelationship;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 *
 * @author Andrew Ribeiro
 */
public class CrudWithOneToOneRelationships extends RibrestTest{
    
    @Test
    @Ignore
    public void postingModelWithOneToOneRelationship(){
        
        MultivaluedMap mvm = new MultivaluedHashMap();
        mvm.add("child.name", "Child Name");
        Response response = post(ModelWithOneToOneRelationship.class, new Form(mvm));
        
        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }
    
    @Test
    @Ignore
    public void puttingAnInexistentModel() throws RibrestDefaultException, JSONException{
        MultivaluedMap mvm = new MultivaluedHashMap();
        mvm.add("name", "New Parent Name");
        Response response = put(RibrestUtils.getResourceName(ModelWithOneToOneRelationship.class) + "/99999", new Form(mvm));
        
        Assert.assertEquals(Response.Status.PRECONDITION_FAILED.getStatusCode(), response.getStatus());
        
        String json = response.readEntity(String.class);
        
        JSONAssert.assertEquals("{\"cause\":\"The model 99999 was not found.\"}", json, JSONCompareMode.LENIENT);
        
    }
    
    @Test
    @Ignore
    public void postingModelWithBidirectionalRelationshipButMissingChild() {
        MultivaluedMap mvm = new MultivaluedHashMap();
        mvm.add("name", "I'm Parent Model");
        Response response = post(ModelParentWithBidirectionalRelationship.class, new Form(mvm));
        
        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }
    
    @Test
    @Ignore
    public void gettingModelsWithBidirectionalRelationship(){
        
        for (int i = 0; i < 10; i++) {
            postingModelWithBidirectionalRelationshipButMissingChild();            
        }
        
        Response response = get(ModelParentWithBidirectionalRelationship.class);
        
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        String json = response.readEntity(String.class);
    }
    
    @Test
    public void postingModelWithBidirectionalRelationshipAndSameClassType(){
        MultivaluedMap mvm = new MultivaluedHashMap();
        
        mvm.add("name", "Parent");
        mvm.add("child1.name", "My name is child 1");
        
        Response response = post(ModelWithBidirectionalRelationshipAndSameChildClassType.class, new Form(mvm));
        
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }
}
