package br.com.andrewribeiro.test.models;

import br.com.andrewribeiro.ribrest.annotations.RibrestModel;

/**
 *
 * @author Andrew Ribeiro
 */

@RibrestModel
public class NotAIModelSubClass {
    
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
