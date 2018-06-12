package br.com.andrewribeiro.test.models;

import javax.persistence.Entity;

/**
 *
 * @author Andrew Ribeiro
 */
@Entity
public class NotAnnotatedWithRibrestModelAnnotationModel extends br.com.andrewribeiro.ribrest.model.abstracts.AbstractModel{
    
    String name;

    @Override
    public void merge() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
