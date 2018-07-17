package br.com.andrewribeiro.ribrest.annotations;

import br.com.andrewribeiro.ribrest.services.command.GetPersistentChildrenModelCommand;
import br.com.andrewribeiro.ribrest.services.command.GetPersistentModelCommand;
import br.com.andrewribeiro.ribrest.services.command.MergeModelToPersistedModelCommand;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author Andrew Ribeiro
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RibrestModel {
    public String value() default "";
    public RibrestEndpointConfigurator[] endpointsConfigurators() default {};
    public RibrestEndpointConfigurator[] defaultEndpointsConfigurators() default {
        @RibrestEndpointConfigurator(method = "POST", beforeCommands = {GetPersistentChildrenModelCommand.class}),
        @RibrestEndpointConfigurator(method = "GET"),
        @RibrestEndpointConfigurator(method = "PUT", path = "{id}", beforeCommands = {GetPersistentModelCommand.class, MergeModelToPersistedModelCommand.class}),
        @RibrestEndpointConfigurator(method = "DELETE", path = "{id}", beforeCommands = {GetPersistentModelCommand.class, MergeModelToPersistedModelCommand.class})
    };
}
