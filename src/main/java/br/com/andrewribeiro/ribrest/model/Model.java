package br.com.andrewribeiro.ribrest.model;

import java.io.Serializable;

/**
 *
 * @author Andrew Ribeiro
 */

public abstract class Model implements IModel, Serializable{

    private Long id;

    public Model() {}
    public Model(Long id){
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
    
    
    
    
}
