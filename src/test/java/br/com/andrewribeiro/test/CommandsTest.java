package br.com.andrewribeiro.test;

import br.com.andrewribeiro.test.models.ModelWithCommands;
import javax.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Andrew Ribeiro
 */
public class CommandsTest extends RibrestTest{
    
    @Test
    public void beforeCommands(){
        
        Response response = get(ModelWithCommands.class);
        
        Assert.assertEquals(200, response.getStatus());
    }
    
}
