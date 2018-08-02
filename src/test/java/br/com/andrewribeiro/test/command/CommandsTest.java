package br.com.andrewribeiro.test.command;

import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.test.RibrestTest;
import br.com.andrewribeiro.test.command.models.ModelWithAfterCommandsSucceed;
import br.com.andrewribeiro.test.command.models.ModelWithBeforeCommandsFailure;
import br.com.andrewribeiro.test.command.models.ModelWithBeforeCommandsSucceed;
import javax.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Andrew Ribeiro
 */
public class CommandsTest extends RibrestTest {

    @Test
    @Ignore
    public void beforeCommandSucceed() throws RibrestDefaultException {

        Response response = get(ModelWithBeforeCommandsSucceed.class, "/beforecommand1");

        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }
    
    @Test
    @Ignore
    public void beforeCommandFailure() {
        Response response = get(ModelWithBeforeCommandsFailure.class);
        
        Assert.assertEquals(Response.Status.PRECONDITION_FAILED.getStatusCode(), response.getStatus());
    }

    @Test
    @Ignore
    public void afterCommandSucceed(){
        Response response = get(ModelWithAfterCommandsSucceed.class);
        
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }
}
