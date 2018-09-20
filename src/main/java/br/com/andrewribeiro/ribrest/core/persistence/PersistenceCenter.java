package br.com.andrewribeiro.ribrest.core.persistence;

import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultException;

/**
 *
 * @author Andrew Ribeiro
 */
public interface PersistenceCenter {
    
    public void perform() throws RibrestDefaultException;
    public DAO create();
    public void setCurrentDAOClass(Class daoClass);
}
