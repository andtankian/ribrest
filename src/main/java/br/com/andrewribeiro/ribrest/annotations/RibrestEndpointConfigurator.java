package br.com.andrewribeiro.ribrest.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author Andrew Ribeiro
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RibrestEndpointConfigurator {
    public String value() default "";
    public Class[] beforeCommands() default {};
    public Class[] afterCommands() default {};
}
