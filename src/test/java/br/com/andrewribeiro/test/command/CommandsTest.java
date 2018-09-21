package br.com.andrewribeiro.test.command;

import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultException;
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
//    @Ignore
    public void beforeCommandSucceed() {
        get(ModelWithBeforeCommandsSucceed.class, "/beforecommand1");
        wasNoContent();
        logResponse();
    }
    
    @Test
//    @Ignore
    public void beforeCommandFailure() {
        get(ModelWithBeforeCommandsFailure.class);
        wasPreConditionFailed();
        logResponse();
    }

    @Test
//    @Ignore
    public void afterCommandSucceed(){
       get(ModelWithAfterCommandsSucceed.class);
       wasNoContent();
       logResponse();
    }
}
