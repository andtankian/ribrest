package br.com.andrewribeiro.test.crud.models;

import javax.persistence.Entity;

/**
 *
 * @author Andrew Ribeiro
 */
@Entity
public class ChildModel extends br.com.andrewribeiro.ribrest.model.abstracts.AbstractModel {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
