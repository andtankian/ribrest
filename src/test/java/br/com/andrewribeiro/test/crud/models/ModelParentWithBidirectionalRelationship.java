package br.com.andrewribeiro.test.crud.models;

import br.com.andrewribeiro.ribrest.annotations.RibrestModel;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author Andrew Ribeiro
 */
@RibrestModel
@Entity
public class ModelParentWithBidirectionalRelationship extends br.com.andrewribeiro.ribrest.model.abstracts.AbstractModel{
    
    @OneToOne(cascade = CascadeType.PERSIST)
    ModelChildWithBidirectionalRelationship child;
    
}
