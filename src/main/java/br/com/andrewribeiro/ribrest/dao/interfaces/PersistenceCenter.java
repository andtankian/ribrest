package br.com.andrewribeiro.ribrest.dao.interfaces;

import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;

/**
 *
 * @author Andrew Ribeiro
 */
public interface PersistenceCenter {
    
    public void perform() throws RibrestDefaultException;
    public DAO create();
    public void setCurrentDAOClass(Class daoClass);
}
