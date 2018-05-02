package br.com.andrewribeiro.ribrest.dao.abstracts;

import br.com.andrewribeiro.ribrest.dao.interfaces.DAO;
import br.com.andrewribeiro.ribrest.services.FlowContainer;
import br.com.andrewribeiro.ribrest.services.SearchModel;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import br.com.andrewribeiro.ribrest.model.interfaces.Model;

/**
 *
 * @author Andrew Ribeiro
 */
public abstract class AbstractDAO implements DAO {

    @Inject
    protected FlowContainer fc;

    protected EntityManager em;
    protected Model m;
    protected SearchModel sm;
    
    @PostConstruct
    private void injected(){
        em = fc.getEm();
        m = (Model) fc.getModel();
        sm = fc.getHolder().getSm();
    }

}
