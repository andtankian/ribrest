package br.com.andrewribeiro.test.crud.models;

import br.com.andrewribeiro.ribrest.annotations.RibrestModel;
import br.com.andrewribeiro.ribrest.model.abstracts.AbstractModel;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author Andrew Ribeiro
 */
@RibrestModel
@Entity
public class ModelWithOneToManyRelationship extends AbstractModel{
    
    String name;
    
    @OneToMany(cascade = CascadeType.PERSIST)
    Set<ChildModel> children;
}
