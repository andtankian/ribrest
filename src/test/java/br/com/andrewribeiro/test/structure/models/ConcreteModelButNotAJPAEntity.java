package br.com.andrewribeiro.test.structure.models;

import br.com.andrewribeiro.ribrest.annotations.RibrestModel;
import br.com.andrewribeiro.ribrest.model.abstracts.AbstractModel;
import javax.persistence.Column;

/**
 *
 * @author Andrew Ribeiro
 */
@RibrestModel
public class ConcreteModelButNotAJPAEntity extends AbstractModel{
    
    private String myname;

    @Column
    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }
    
}
