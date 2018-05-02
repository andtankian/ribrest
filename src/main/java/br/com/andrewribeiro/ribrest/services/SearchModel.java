package br.com.andrewribeiro.ribrest.services;

/**
 *
 * @author Andrew Ribeiro
 */
public class SearchModel {

    public SearchModel() {
    }

    public SearchModel(Integer limit, Integer offset) {
        this.limit = limit;
        this.offset = offset;
    }
    
    
    
    private Integer limit;
    private Integer offset;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
