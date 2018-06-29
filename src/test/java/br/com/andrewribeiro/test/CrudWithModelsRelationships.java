package br.com.andrewribeiro.test;

import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import br.com.andrewribeiro.test.models.ModelWithOneToOneRelationship;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Andrew Ribeiro
 */
public class CrudWithModelsRelationships extends RibrestTest{
    @Test
    @Ignore
    public void postingModelWithOneToOneRelationship(){
        
        MultivaluedMap mvm = new MultivaluedHashMap();
        mvm.add("child.name", "Child Name");
        Response response = post(ModelWithOneToOneRelationship.class, new Form(mvm));
        
        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }
    
    /*THIS TEST WILL WORK AS EXPECTED IF HBM2DLL IS IN UPDATE MODE*/
    @Test
    public void postingAndPutingModelWithOneToOneRelationship() throws RibrestDefaultException{
        MultivaluedMap mvm = new MultivaluedHashMap();
        mvm.add("name", "Parent Name");
        mvm.add("child.name", "Child Name");
        Response response = post(ModelWithOneToOneRelationship.class, new Form(mvm));        
        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        mvm = new MultivaluedHashMap();
        mvm.add("name", "New Parent Name");
        mvm.add("child.id", "4");
        response = put(RibrestUtils.getResourceName(ModelWithOneToOneRelationship.class) + "/1", new Form(mvm));
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
    }
}
