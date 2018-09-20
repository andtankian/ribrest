package br.com.andrewribeiro.test.crud.models;

import br.com.andrewribeiro.ribrest.core.annotations.RibrestModel;
import javax.persistence.Entity;

/**
 *
 * @author Andrew Ribeiro
 */
@RibrestModel
@Entity
public class ModelCrud extends br.com.andrewribeiro.ribrest.core.model.AbstractModel{
    
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
