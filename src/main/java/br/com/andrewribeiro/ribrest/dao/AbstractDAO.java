package br.com.andrewribeiro.ribrest.dao;

import br.com.andrewribeiro.ribrest.model.IModel;
import br.com.andrewribeiro.ribrest.services.FlowContainer;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author Andrew Ribeiro
 */
public abstract class AbstractDAO implements IDAO {

    @Inject
    protected FlowContainer fc;

    protected EntityManager em;
    protected IModel m;
    
    @PostConstruct
    private void injected(){
        em = fc.getEm();
        m = (IModel) fc.getModel();
    }

}
