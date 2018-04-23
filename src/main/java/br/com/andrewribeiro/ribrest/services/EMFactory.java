package br.com.andrewribeiro.ribrest.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.glassfish.hk2.api.Factory;

/**
 *
 * @author Andrew Ribeiro
 */
public class EMFactory implements Factory<EntityManager>{
    
    @Inject
    EMFFactory emf;

    @Override
    public EntityManager provide() {
        return emf.provide().createEntityManager();
    }

    @Override
    public void dispose(EntityManager t) {
        t.close();
    }
    
}
