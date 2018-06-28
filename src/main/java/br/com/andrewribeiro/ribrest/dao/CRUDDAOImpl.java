package br.com.andrewribeiro.ribrest.dao;

import br.com.andrewribeiro.ribrest.dao.abstracts.AbstractDAO;
import br.com.andrewribeiro.ribrest.dao.interfaces.CRUD;
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
        String method = fc.getMethod();
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
        em.persist(m);
        em.getTransaction().commit();
        fc.getResult().setStatus(Response.Status.CREATED);
        fc.getHolder().getModels().add(m);
    }

    @Override
    public void read() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cmodel = cb.createQuery();
        cmodel.select(cmodel.from(m.getClass()));
        fc.getHolder().setModels(em.createQuery(cmodel).setFirstResult(sm.getOffset()).setMaxResults(sm.getLimit()).getResultList());
        CriteriaQuery<Long> ccount = cb.createQuery(Long.class);
        ccount.select(cb.count(ccount.from(m.getClass())));
        fc.getHolder().setTotal(em.createQuery(ccount).getSingleResult());
        setStatusToNoContentIfModelsEmpty();
    }

    @Override
    public void update() {
        beginInactiveTransaction();
        em.getTransaction().commit();
        fc.getResult().setStatus(Response.Status.OK);
        fc.getHolder().getModels().add(m);
    }

    @Override
    public void delete() {
        update();
    }
    
    private void beginInactiveTransaction(){
        EntityTransaction t = em.getTransaction();
        if(!t.isActive()){
            t.begin();
        }
    }
    
    private void setStatusToNoContentIfModelsEmpty(){
        if(isModelsEmpty()){
            fc.getResult().setStatus(Response.Status.NO_CONTENT);
        }
    }
    
    private boolean isModelsEmpty(){
        return fc.getHolder().getModels().isEmpty();
    }

}
