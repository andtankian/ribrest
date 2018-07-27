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
    protected FlowContainer flowContainer;

    protected EntityManager entityManager;
    protected Model model;
    protected SearchModel searchModel;
    
    @PostConstruct
    private void injected(){
        entityManager = flowContainer.getEntityManager();
        model = (Model) flowContainer.getModel();
        searchModel = flowContainer.getHolder().getSm();
    }

}
