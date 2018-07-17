package br.com.andrewribeiro.test.structure.models;

import br.com.andrewribeiro.ribrest.annotations.RibrestModel;
import br.com.andrewribeiro.test.crud.models.ChildModel;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author Andrew Ribeiro
 */
@Entity
@RibrestModel
public class ModelWithOneToOneRelationship extends br.com.andrewribeiro.ribrest.model.abstracts.AbstractModel {

    @OneToOne(cascade = CascadeType.PERSIST)
    private ChildModel child;
    
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChildModel getChild() {
        return child;
    }

    public void setChild(ChildModel child) {
        this.child = child;
    }
}
