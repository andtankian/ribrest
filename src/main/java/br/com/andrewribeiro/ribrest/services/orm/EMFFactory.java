package br.com.andrewribeiro.ribrest.services.orm;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.glassfish.hk2.api.Factory;

/**
 *
 * @author Andrew Ribeiro
 */
public class EMFFactory implements Factory<EntityManagerFactory>{
    
    
    private EntityManagerFactory entityManagerFactory;
    
    @Inject
    PersistenceUnitWrapper puw;
    

    @Override
    public EntityManagerFactory provide() {
        entityManagerFactory = entityManagerFactory == null ? Persistence.createEntityManagerFactory(puw.getPersistenceUnitName()) : entityManagerFactory;
        return entityManagerFactory;
    }

    @Override
    public void dispose(EntityManagerFactory entityManagerFactory) {
        entityManagerFactory.close();
    }
    
}
