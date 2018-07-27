package br.com.andrewribeiro.test.crud;

import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.test.RibrestTest;
import br.com.andrewribeiro.test.crud.models.FatherModel;
import br.com.andrewribeiro.test.crud.models.SonModel;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import junit.framework.Assert;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Andrew Ribeiro
 */
public class GetOnlyWithOneToManyTest extends RibrestTest {

    @Test
//    @Ignore
    public void getFathersAndItsKids() throws JSONException {

        Response responseForKids;
        String responseForKidsString;
        MultivaluedMap<String, String> mvm = new MultivaluedHashMap<>();
        MultivaluedHashMap<String, String> kidsId = new MultivaluedHashMap<>();

        for (int i = 0; i < 5; i++) {
            mvm.putSingle("name", "Kid " + i);
            responseForKids = post(SonModel.class, new Form(mvm));
            Assert.assertEquals(Response.Status.CREATED.getStatusCode(), responseForKids.getStatus());
            responseForKidsString = responseForKids.readEntity(String.class);
            JSONObject kidsResponseJson = new JSONObject(responseForKidsString);
            kidsId.add("kids.id", kidsResponseJson.getJSONObject("holder").getJSONArray("models").getJSONObject(0).getString("id"));
        }

        Response responseForFather = post(FatherModel.class, new Form(kidsId));

        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), responseForFather.getStatus());

        responseForFather = get(FatherModel.class);

        String responseForFatherString = responseForFather.readEntity(String.class);

        System.out.println(responseForFatherString);

        Assert.assertEquals(Response.Status.OK.getStatusCode(), responseForFather.getStatus());

    }

    @Test
    public void getKidsAndItsFather() throws JSONException {

        getFathersAndItsKids();

        Response responseForKids = get(SonModel.class);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), responseForKids.getStatus());
        String responseForKidsString = responseForKids.readEntity(String.class);

        System.out.println(responseForKidsString);

    }

}
