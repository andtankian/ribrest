package br.com.andrewribeiro.ribrest.core.persistence;

import br.com.andrewribeiro.ribrest.services.dtos.FlowContainer;
import br.com.andrewribeiro.ribrest.services.dtos.SearchModel;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import br.com.andrewribeiro.ribrest.core.model.Model;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
    protected JPADataContainer jPADataContainer;
    
    @PostConstruct
    private void injected(){
        entityManager = flowContainer.getEntityManager();
        model = (Model) flowContainer.getModel();
        searchModel = flowContainer.getHolder().getSm();
        jPADataContainer = getNewJPADataContainer();
    }
    
    private JPADataContainer getNewJPADataContainer() {
        CriteriaBuilder criteriaBuilder = flowContainer.getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root root = criteriaQuery.from(model.getClass());
        
        return new JPADataContainer(criteriaBuilder, criteriaQuery, root);
    }

}
