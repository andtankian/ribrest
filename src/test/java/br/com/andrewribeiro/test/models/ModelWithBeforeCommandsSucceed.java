package br.com.andrewribeiro.test.models;

import br.com.andrewribeiro.ribrest.annotations.RibrestEndpointConfigurator;
import br.com.andrewribeiro.ribrest.annotations.RibrestModel;
import br.com.andrewribeiro.ribrest.services.command.GetPersistentModelCommand;
import br.com.andrewribeiro.test.models.commands.BeforeCommandSucceed;
import javax.persistence.Entity;

/**
 *
 * @author Andrew Ribeiro
 */
@RibrestModel(defaultEndpointsConfigurators = {
    @RibrestEndpointConfigurator(beforeCommands = GetPersistentModelCommand.class)
},
        endpointsConfigurators = {
            @RibrestEndpointConfigurator(beforeCommands = BeforeCommandSucceed.class, path = "beforecommand1")
        })
@Entity
public class ModelWithBeforeCommandsSucceed extends br.com.andrewribeiro.ribrest.model.abstracts.AbstractModel {

    private String modelName;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

}
