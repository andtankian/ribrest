package br.com.andrewribeiro.test.models;

import br.com.andrewribeiro.ribrest.annotations.RibrestModel;
import br.com.andrewribeiro.ribrest.model.Model;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;

/**
 *
 * @author Andrew Ribeiro
 */
@RibrestModel
public class ConcreteModelNotMapped extends Model{
    
    private String myname;

    @Override
    public void merge() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List getAllAttributes() {
        return new ArrayList();
    }

    @Column
    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }
    
}
