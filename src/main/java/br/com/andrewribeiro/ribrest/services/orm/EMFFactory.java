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
    
    private EntityManagerFactory emf;
    
    @Inject
    PersistenceUnitWrapper puw;
    

    @Override
    public EntityManagerFactory provide() {
        emf = emf != null ? emf : Persistence.createEntityManagerFactory(puw.getPersistenceUnitName());
        return emf;
    }

    @Override
    public void dispose(EntityManagerFactory t) {
        emf.close();
    }
    
}
