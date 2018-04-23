package br.com.andrewribeiro.ribrest.model;

import java.util.List;

/**
 *
 * @author Andrew Ribeiro
 */
public interface IModel {
    
    public void merge();
    public List getAllAttributes();
    public List getIgnoredAttributes();
    
    public Long getId();
    public void setId(Long id);
}
