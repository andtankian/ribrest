package br.com.andrewribeiro.ribrest.annotations;

import br.com.andrewribeiro.ribrest.dao.CRUDDAOImpl;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author Andrew Ribeiro
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RibrestEndpointConfigurator {
    public Class[] requestFiltersNameBindings() default {};
    public String path() default "";
    public String method() default "GET";
    public Class[] beforeCommands() default {};
    public Class[] afterCommands() default {};
    public Class dao() default CRUDDAOImpl.class;
}
