package br.com.andrewribeiro.ribrest.annotations;

import br.com.andrewribeiro.ribrest.services.command.GetPersistentModelCommand;
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
        @RibrestEndpointConfigurator(method = "POST"),
        @RibrestEndpointConfigurator(method = "GET"),
        @RibrestEndpointConfigurator(method = "PUT", path = "{id}", beforeCommands = {GetPersistentModelCommand.class}),
        @RibrestEndpointConfigurator(method = "DELETE", path = "{id}", beforeCommands = {GetPersistentModelCommand.class})
    };
}
