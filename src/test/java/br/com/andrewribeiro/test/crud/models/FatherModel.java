package br.com.andrewribeiro.test.crud.models;

import br.com.andrewribeiro.ribrest.core.annotations.RibrestEndpointConfigurator;
import br.com.andrewribeiro.ribrest.core.annotations.RibrestModel;
import br.com.andrewribeiro.ribrest.core.annotations.RibrestWontFill;
import br.com.andrewribeiro.ribrest.core.model.AbstractModel;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author Andrew Ribeiro
 */
@Entity
@RibrestModel(endpointsConfigurators = @RibrestEndpointConfigurator(path = "{id}"))
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
