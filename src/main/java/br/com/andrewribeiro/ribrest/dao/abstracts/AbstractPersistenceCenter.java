package br.com.andrewribeiro.ribrest.dao.abstracts;

import br.com.andrewribeiro.ribrest.dao.interfaces.DAO;
import br.com.andrewribeiro.ribrest.dao.interfaces.PersistenceCenter;
import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultExceptionConstants;
import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultExceptionFactory;
import br.com.andrewribeiro.ribrest.services.FlowContainer;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import javax.inject.Inject;
import org.glassfish.hk2.api.ServiceLocator;

/**
 *
 * @author Andrew Ribeiro
 */
public abstract class AbstractPersistenceCenter implements PersistenceCenter {

    
    private DAO dao;
    
    @Inject
    private ServiceLocator sl;
    
    @Inject
    FlowContainer fc;
    

    @Override
    public void perform() throws RibrestDefaultException{
        dao = this.create();
        sl.inject(dao);
        sl.postConstruct(dao);
        try {
            dao.perform();
        } catch(IllegalArgumentException e){
            throw RibrestDefaultExceptionFactory.getRibrestDefaultException(RibrestDefaultExceptionConstants.RESOURCE_ISNT_AN_ENTITY, RibrestUtils.getResourceName(fc.getModel().getClass()));
        }
    }

}
