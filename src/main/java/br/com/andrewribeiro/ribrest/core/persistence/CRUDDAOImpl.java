package br.com.andrewribeiro.ribrest.core.persistence;

import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.ws.rs.core.Response;

/**
 *
 * @author Andrew Ribeiro
 */
public class CRUDDAOImpl extends AbstractDAO implements CRUD {

    @Override
    public void perform() {
        String method = flowContainer.getMethod();
        if (method.equalsIgnoreCase("post")) {
            create();
        } else if (method.equalsIgnoreCase("get")) {
            read();
        } else if (method.equalsIgnoreCase("put")) {
            update();
        } else {
            delete();
        }
    }

    @Override
    public void create() {
        beginInactiveTransaction();
        entityManager.persist(model);
        entityManager.getTransaction().commit();
        flowContainer.getResult().setStatus(Response.Status.CREATED);
        flowContainer.getHolder().getModels().add(model);
    }

    @Override
    public void read() {
        setupGenericJPASelect();
        CriteriaQuery criteriaQuery = jPADataContainer.getCriteriaQuery();
        CriteriaBuilder criteriaBuilder = jPADataContainer.getCriteriaBuilder();
        if (model.getId() != null) {
            criteriaQuery.where(criteriaBuilder.equal(
                    jPADataContainer.getRoot().get("id"), model.getId()
            ));
        }
        getModels();
        getTotalEntities();
        setStatusToNoContentIfModelsEmpty();
    }

    @Override
    public void update() {
        beginInactiveTransaction();
        entityManager.getTransaction().commit();
        flowContainer.getResult().setStatus(Response.Status.OK);
        flowContainer.getHolder().getModels().add(model);
    }

    @Override
    public void delete() {
        update();
    }

    private void beginInactiveTransaction() {
        EntityTransaction t = entityManager.getTransaction();
        if (!t.isActive()) {
            t.begin();
        }
    }

    protected void setStatusToNoContentIfModelsEmpty() {
        if (isModelsEmpty()) {
            flowContainer.getResult().setStatus(Response.Status.NO_CONTENT);
        }
    }

    protected boolean isModelsEmpty() {
        return flowContainer.getHolder().getModels().isEmpty();
    }

    protected void setupGenericJPASelect() {
        jPADataContainer.getCriteriaQuery().select(
                jPADataContainer.getRoot()
        );

    }

    protected void getTotalEntities() {
        CriteriaQuery<Long> ccount = jPADataContainer.getCriteriaBuilder().createQuery(Long.class);
        ccount.select(jPADataContainer.getCriteriaBuilder().count(ccount.from(model.getClass())));
        flowContainer.getHolder().setTotal(entityManager.createQuery(ccount).getSingleResult());
    }
    
    protected void getModels() {
        flowContainer.getHolder().setModels(entityManager.createQuery(jPADataContainer.getCriteriaQuery()).setFirstResult(searchModel.getOffset()).setMaxResults(searchModel.getLimit()).getResultList());
    }

}
