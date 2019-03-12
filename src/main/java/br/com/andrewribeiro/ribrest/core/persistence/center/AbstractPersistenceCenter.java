package br.com.andrewribeiro.ribrest.core.persistence.center;

import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultExceptionConstants;
import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultExceptionFactory;
import br.com.andrewribeiro.ribrest.core.persistence.DAO;
import br.com.andrewribeiro.ribrest.core.persistence.PersistenceCenter;
import br.com.andrewribeiro.ribrest.services.dtos.FlowContainer;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import javax.inject.Inject;
import org.glassfish.hk2.api.ServiceLocator;

/**
 *
 * @author Andrew Ribeiro
 */
public abstract class AbstractPersistenceCenter implements PersistenceCenter {

    
    private DAO dao;
    protected Class daoClass;
    
    @Inject
    private ServiceLocator sl;
    
    @Inject
    FlowContainer fc;
    

    @Override
    public void perform() throws RibrestDefaultException{
        dao = this.create();
        try {
            sl.inject(dao);
            sl.postConstruct(dao);
            dao.perform();
        } catch(Exception e){
            throw RibrestDefaultExceptionFactory.getRibrestDefaultException(RibrestDefaultExceptionConstants.RESOURCE_ISNT_AN_ENTITY, RibrestUtils.getResourceName(fc.getModel().getClass()));
        }
    }

    @Override
    public void setCurrentDAOClass(Class daoClass) {
        this.daoClass = daoClass;
    }
    
    

}
