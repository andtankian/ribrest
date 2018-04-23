package br.com.andrewribeiro.ribrest.services.holder;

import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultExceptionConstants;
import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultExceptionFactory;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrew Ribeiro
 */
public abstract class AbstractHolder implements IHolder {

    public AbstractHolder() {

        models = new ArrayList();
    }

    private List models;
    
    @Override
    public List getModels() {
        return models;
    }

    @Override
    public void setModels(List models) {
        this.models = models;
    }

    @Override
    public void setupEntity(Class classType) throws Exception{
        if(Modifier.isAbstract(classType.getModifiers())){
            throw RibrestDefaultExceptionFactory.getRibrestDefaultException(RibrestDefaultExceptionConstants.RESOURCE_IS_ABSTRACT, classType.getSimpleName().toLowerCase());
        }
        this.models.add(classType.newInstance());
    }
    
}
