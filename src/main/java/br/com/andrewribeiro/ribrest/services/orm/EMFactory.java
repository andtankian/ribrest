package br.com.andrewribeiro.ribrest.services.orm;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.glassfish.hk2.api.Factory;

/**
 *
 * @author Andrew Ribeiro
 */
public class EMFactory implements Factory<EntityManager>{
    
    @Inject
    EntityManagerFactory entityManagerFactory;

    @Override
    public EntityManager provide() {
        return entityManagerFactory.createEntityManager();
    }

    @Override
    public void dispose(EntityManager entityManager) {
        entityManager.close();
    }
    
}
