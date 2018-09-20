package br.com.andrewribeiro.ribrest.core.persistence;

import br.com.andrewribeiro.ribrest.services.dtos.FlowContainer;
import br.com.andrewribeiro.ribrest.services.dtos.SearchModel;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import br.com.andrewribeiro.ribrest.core.model.Model;

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
