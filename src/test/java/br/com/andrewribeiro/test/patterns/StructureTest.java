package br.com.andrewribeiro.test.patterns;

import br.com.andrewribeiro.test.RibrestTest;
import org.junit.Test;

import br.com.andrewribeiro.test.crud.models.ModelCrud;
import javax.ws.rs.core.Form;
import org.json.JSONObject;
import org.junit.Assert;

/**
 *
 * @author Andrew Ribeiro
 */
public class StructureTest extends RibrestTest{

    @Test
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
    
}
