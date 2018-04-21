package br.com.andrewribeiro.ribrest.model;

import java.lang.reflect.Field;
import java.util.List;

/**
 *
 * @author Andrew Ribeiro
 */
public interface IModel {
    
    public void merge();
    public List<Field> getAllAttributes();
    public List<Field> getIgnoredAttributes();
    
    public Long getId();
    public void setId(Long id);
}
