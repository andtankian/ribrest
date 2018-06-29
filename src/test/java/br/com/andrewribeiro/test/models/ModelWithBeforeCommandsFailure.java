package br.com.andrewribeiro.test.models;

import br.com.andrewribeiro.ribrest.annotations.RibrestEndpointConfigurator;
import br.com.andrewribeiro.ribrest.annotations.RibrestModel;
import br.com.andrewribeiro.test.models.commands.BeforeCommandFailure;
import javax.persistence.Entity;

/**
 *
 * @author Andrew Ribeiro
 */
@Entity
@RibrestModel(defaultEndpointsConfigurators = {
    @RibrestEndpointConfigurator(beforeCommands = BeforeCommandFailure.class)
})
public class ModelWithBeforeCommandsFailure extends br.com.andrewribeiro.ribrest.model.abstracts.AbstractModel{

}
