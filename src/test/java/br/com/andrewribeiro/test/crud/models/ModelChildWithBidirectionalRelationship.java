package br.com.andrewribeiro.test.crud.models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author Andrew Ribeiro
 */
@Entity
public class ModelChildWithBidirectionalRelationship extends br.com.andrewribeiro.ribrest.core.model.AbstractModel{
    
    @OneToOne(mappedBy = "child")
    ModelParentWithBidirectionalRelationship parent;
    
}
