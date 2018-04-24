package br.com.andrewribeiro.ribrest.dao;

import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;

/**
 *
 * @author Andrew Ribeiro
 */
public interface IPersistenceCenter {
    
    public void perform() throws RibrestDefaultException;
    public IDAO create();
    
}
