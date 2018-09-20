package br.com.andrewribeiro.test.crud.models;

import br.com.andrewribeiro.ribrest.core.annotations.RibrestModel;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author Andrew Ribeiro
 */
@RibrestModel
@Entity
public class ModelParentWithBidirectionalRelationship extends br.com.andrewribeiro.ribrest.core.model.AbstractModel{
    
    @OneToOne(cascade = CascadeType.PERSIST)
    ModelChildWithBidirectionalRelationship child;
    
}
