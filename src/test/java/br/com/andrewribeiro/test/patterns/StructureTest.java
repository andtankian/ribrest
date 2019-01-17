package br.com.andrewribeiro.test.patterns;

import br.com.andrewribeiro.test.RibrestTest;
import org.junit.Test;

import br.com.andrewribeiro.test.crud.models.ModelCrud;
import javax.ws.rs.core.Form;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Ignore;

/**
 *
 * @author Andrew Ribeiro
 */
public class StructureTest extends RibrestTest{

    @Test
    @Ignore
    public void limitAndOffset(){
        post(ModelCrud.class,new Form());
        wasCreated();     
        logResponse();
        getWithQueryParameters(ModelCrud.class, "limit=999&offset=0");
        wasOk();
        JSONObject jsonObject = new JSONObject(responseText);
        int limit, offset;
        limit = jsonObject.getJSONObject("holder").getJSONObject("sm").getInt("limit");
        offset = jsonObject.getJSONObject("holder").getJSONObject("sm").getInt("offset");
        Assert.assertEquals(999, limit);
        Assert.assertEquals(0, offset);
        logResponse();
    }
    
    @Test
    @Ignore
    public void zeroLimitAndOffset() {
        getWithQueryParameters(ModelCrud.class, "limit=0&offset=0");
        wasNoContent();
        logResponse();
    }
    
    @Test
    @Ignore
    public void infinityLimit() {
        getWithQueryParameters(ModelCrud.class, "limit=infinity");
        wasOk();
        logResponse();
    }
    
    @Test
    public void negativeLimit() {
        getWithQueryParameters(ModelCrud.class, "limit=-1");
        wasOk();
        logResponse();
        
        getWithQueryParameters(ModelCrud.class, "limit=-50");
        wasOk();
        logResponse();
        
    }
}
