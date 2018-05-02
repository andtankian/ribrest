package br.com.andrewribeiro.test.models;

import br.com.andrewribeiro.ribrest.annotations.RibrestModel;
import br.com.andrewribeiro.ribrest.model.abstracts.AbstractModel;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;

/**
 *
 * @author Andrew Ribeiro
 */
@Entity
@RibrestModel
public class InsertConcreteModel extends AbstractModel{
    
    private String name;

    @Override
    public void merge() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List getAllAttributes() {
        return new ArrayList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
