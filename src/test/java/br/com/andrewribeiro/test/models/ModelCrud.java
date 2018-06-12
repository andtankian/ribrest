package br.com.andrewribeiro.test.models;

import br.com.andrewribeiro.ribrest.annotations.RibrestModel;
import javax.persistence.Entity;

/**
 *
 * @author Andrew Ribeiro
 */
@RibrestModel
@Entity
public class ModelCrud extends br.com.andrewribeiro.ribrest.model.abstracts.AbstractModel{
    
    private String name;

    @Override
    public void merge() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
