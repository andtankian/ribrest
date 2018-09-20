package br.com.andrewribeiro.test.crud.models;

import br.com.andrewribeiro.ribrest.core.annotations.RibrestModel;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author Andrew Ribeiro
 */
@RibrestModel
@Entity
public class ModelWithBidirectionalRelationshipAndSameChildClassType extends br.com.andrewribeiro.ribrest.core.model.AbstractModel{
    
    @Column(length = 20)
    private String name;
    
    @OneToOne(cascade = CascadeType.PERSIST)
    private ModelWithBidirectionalRelationshipAndSameChildClassType child1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ModelWithBidirectionalRelationshipAndSameChildClassType getChild1() {
        return child1;
    }

    public void setChild1(ModelWithBidirectionalRelationshipAndSameChildClassType child1) {
        this.child1 = child1;
    }

}
