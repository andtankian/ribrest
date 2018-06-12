package br.com.andrewribeiro.ribrest.dao;

import br.com.andrewribeiro.ribrest.dao.abstracts.AbstractPersistenceCenter;
import br.com.andrewribeiro.ribrest.dao.interfaces.DAO;


/**
 *
 * @author Andrew Ribeiro
 */
public class CRUDCenter extends AbstractPersistenceCenter {

   
    @Override
    public DAO create() {
        return new CRUDDAOImpl();
    }

}
