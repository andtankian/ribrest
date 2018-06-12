package br.com.andrewribeiro.test.models;

import br.com.andrewribeiro.ribrest.annotations.RibrestEndpointConfigurator;
import br.com.andrewribeiro.ribrest.annotations.RibrestModel;
import br.com.andrewribeiro.test.models.commands.CommandTest;
import javax.persistence.Entity;

/**
 *
 * @author Andrew Ribeiro
 */
@RibrestModel(ribrestConfigurators = {
    @RibrestEndpointConfigurator(value = "{id}", beforeCommands = {CommandTest.class})
})
@Entity
public class ModelWithCommands extends br.com.andrewribeiro.ribrest.model.abstracts.AbstractModel{
    
    private String modelName;

    @Override
    public void merge() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    
    

}
