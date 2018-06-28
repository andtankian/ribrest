package br.com.andrewribeiro.ribrest.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author Andrew Ribeiro
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RibrestEndpointConfigurator {
    public String path() default "";
    public String method() default "GET";
    public Class[] beforeCommands() default {};
    public Class[] afterCommands() default {};
}
