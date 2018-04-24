package br.com.andrewribeiro.ribrest.services.holder;

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
    
}
