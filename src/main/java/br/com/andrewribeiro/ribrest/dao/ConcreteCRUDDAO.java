package br.com.andrewribeiro.ribrest.dao;

import br.com.andrewribeiro.ribrest.dao.abstracts.AbstractDAO;
import br.com.andrewribeiro.ribrest.dao.interfaces.CRUD;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author Andrew Ribeiro
 */
public class ConcreteCRUDDAO extends AbstractDAO implements CRUD {

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
        em.getTransaction().begin();
        em.persist(m);
        em.getTransaction().commit();
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
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
