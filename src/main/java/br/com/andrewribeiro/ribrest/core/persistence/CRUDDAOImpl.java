package br.com.andrewribeiro.ribrest.core.persistence;

import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery cmodel = criteriaBuilder.createQuery();
        Root from = cmodel.from(model.getClass());
        cmodel.select(from);
        if(model.getId() != null){
            cmodel.where(criteriaBuilder.equal(from.get("id"), model.getId()));
        }
        flowContainer.getHolder().setModels(entityManager.createQuery(cmodel).setFirstResult(searchModel.getOffset()).setMaxResults(searchModel.getLimit()).getResultList());
        CriteriaQuery<Long> ccount = criteriaBuilder.createQuery(Long.class);
        ccount.select(criteriaBuilder.count(ccount.from(model.getClass())));
        flowContainer.getHolder().setTotal(entityManager.createQuery(ccount).getSingleResult());
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
    
    private void beginInactiveTransaction(){
        EntityTransaction t = entityManager.getTransaction();
        if(!t.isActive()){
            t.begin();
        }
    }
    
    private void setStatusToNoContentIfModelsEmpty(){
        if(isModelsEmpty()){
            flowContainer.getResult().setStatus(Response.Status.NO_CONTENT);
        }
    }
    
    private boolean isModelsEmpty(){
        return flowContainer.getHolder().getModels().isEmpty();
    }

}
