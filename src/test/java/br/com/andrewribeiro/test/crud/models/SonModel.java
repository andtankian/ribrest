package br.com.andrewribeiro.test.crud.models;

import br.com.andrewribeiro.ribrest.annotations.RibrestModel;
import br.com.andrewribeiro.ribrest.model.abstracts.AbstractModel;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Andrew Ribeiro
 */
@Entity
@RibrestModel
public class SonModel extends AbstractModel{
    
    private String name;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "father_id")
    private FatherModel father;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FatherModel getFather() {
        return father;
    }

    public void setFather(FatherModel father) {
        this.father = father;
    }
}
