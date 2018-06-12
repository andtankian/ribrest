package br.com.andrewribeiro.ribrest.services.orm;

/**
 *
 * @author Andrew Ribeiro
 */
public class PersistenceUnitWrapper {
    
    private String persistenceUnitName;

    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    public void setPersistenceUnitName(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }
    
}
