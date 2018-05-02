package br.com.andrewribeiro.ribrest.services.holder.interfaces;

import br.com.andrewribeiro.ribrest.services.SearchModel;
import java.util.List;

/**
 *
 * @author Andrew Ribeiro
 */
public interface Holder {

    public List getModels();

    public void setModels(List models);
    
    public SearchModel getSm();
    
    public void setSm(SearchModel sm);
    
    public Long getTotal();
    
    public void setTotal(Long total);
}
