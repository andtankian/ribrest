package br.com.andrewribeiro.test.models;

import br.com.andrewribeiro.ribrest.annotations.RibrestEndpointConfigurator;
import br.com.andrewribeiro.ribrest.annotations.RibrestModel;
import br.com.andrewribeiro.test.models.commands.AfterCommandSucceed;
import javax.persistence.Entity;

/**
 *
 * @author Andrew Ribeiro
 */
@Entity
@RibrestModel(defaultEndpointsConfigurators = {
    @RibrestEndpointConfigurator(afterCommands = AfterCommandSucceed.class)
})
public class ModelWithAfterCommandsSucceed extends br.com.andrewribeiro.ribrest.model.abstracts.AbstractModel{

    @Override
    public void merge() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
