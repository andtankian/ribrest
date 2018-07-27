package br.com.andrewribeiro.test.crud.models;

import br.com.andrewribeiro.ribrest.annotations.RibrestModel;
import br.com.andrewribeiro.ribrest.model.abstracts.AbstractModel;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author Andrew Ribeiro
 */
@Entity
@RibrestModel
public class FatherModel extends AbstractModel{
    
    @OneToMany
    private Set<SonModel> kids;

    public Set<SonModel> getKids() {
        return kids;
    }

    public void setKids(Set<SonModel> kids) {
        this.kids = kids;
    }
    
    
    
}
