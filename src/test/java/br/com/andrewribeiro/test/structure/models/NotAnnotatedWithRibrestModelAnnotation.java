package br.com.andrewribeiro.test.structure.models;

import javax.persistence.Entity;

/**
 *
 * @author Andrew Ribeiro
 */
@Entity
public class NotAnnotatedWithRibrestModelAnnotation extends br.com.andrewribeiro.ribrest.model.abstracts.AbstractModel{
    
    String name;

}
