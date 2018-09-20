package br.com.andrewribeiro.ribrest.core.annotations;

import br.com.andrewribeiro.ribrest.core.persistence.CRUDDAOImpl;
import br.com.andrewribeiro.ribrest.services.dispatcher.DispatcherImpl;
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
    public Class[] requestFiltersNameBindings() default {};
    public Class[] beforeCommands() default {};
    public Class dao() default CRUDDAOImpl.class;
    public Class[] afterCommands() default {};
    public Class dispatcher() default DispatcherImpl.class;
    public Class[] responseFiltersNameBindings()default {};
}
