package br.com.andrewribeiro.ribrest.dao;

import java.util.List;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author Andrew Ribeiro
 */
public class ConcreteCRUDDAO extends AbstractDAO implements ICRUD {

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void read() {        
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(m.getClass())); 
        List l = em.createQuery(cq).getResultList();        
        fc.getHolder().setModels(l);
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
