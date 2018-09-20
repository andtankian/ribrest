package br.com.andrewribeiro.test.command.models;

import br.com.andrewribeiro.ribrest.core.annotations.RibrestEndpointConfigurator;
import br.com.andrewribeiro.ribrest.core.annotations.RibrestModel;
import br.com.andrewribeiro.ribrest.services.command.GetPersistentModelCommand;
import br.com.andrewribeiro.test.command.commands.BeforeCommandSucceed;
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
public class ModelWithBeforeCommandsSucceed extends br.com.andrewribeiro.ribrest.core.model.AbstractModel {

    private String modelName;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

}
