package br.com.andrewribeiro.test.command.models;

import br.com.andrewribeiro.ribrest.core.annotations.RibrestEndpointConfigurator;
import br.com.andrewribeiro.ribrest.core.annotations.RibrestModel;
import br.com.andrewribeiro.test.command.commands.BeforeCommandFailure;
import javax.persistence.Entity;

/**
 *
 * @author Andrew Ribeiro
 */
@Entity
@RibrestModel(defaultEndpointsConfigurators = {
    @RibrestEndpointConfigurator(beforeCommands = BeforeCommandFailure.class)
})
public class ModelWithBeforeCommandsFailure extends br.com.andrewribeiro.ribrest.core.model.AbstractModel{

}
