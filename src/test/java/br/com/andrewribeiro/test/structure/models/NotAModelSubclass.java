package br.com.andrewribeiro.test.structure.models;

import br.com.andrewribeiro.ribrest.core.annotations.RibrestModel;

/**
 *
 * @author Andrew Ribeiro
 */

@RibrestModel
public class NotAModelSubclass {
    
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
