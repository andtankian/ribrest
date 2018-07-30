package br.com.andrewribeiro.ribrest.dao;

import br.com.andrewribeiro.ribrest.dao.abstracts.AbstractPersistenceCenter;
import br.com.andrewribeiro.ribrest.dao.interfaces.DAO;

/**
 *
 * @author Andrew Ribeiro
 */
public class CRUDCenterImpl extends AbstractPersistenceCenter {

    @Override
    public DAO create() {
        if (daoClass == null) {
            throw new RuntimeException("");
        }
        try {
            return (DAO) daoClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

}
