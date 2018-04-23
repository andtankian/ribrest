package br.com.andrewribeiro.ribrest.services;

/**
 *
 * @author Andrew Ribeiro
 */
public class PersistenceUnitWrapper {
    
    public PersistenceUnitWrapper(){
        System.out.println("PersistenceUnitWrapper created");
    }
    
    private String pu;

    public String getPu() {
        return pu;
    }

    public void setPu(String pu) {
        this.pu = pu;
    }
    
}
