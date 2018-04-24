package br.com.andrewribeiro.ribrest.dao;


/**
 *
 * @author Andrew Ribeiro
 */
public class CRUDCenter extends AbstractPersistenceCenter {

   
    @Override
    public IDAO create() {
        return new ConcreteCRUDDAO();
    }

}
