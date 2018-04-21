package br.com.andrewribeiro.ribrest.exceptions;

/**
 *
 * @author Andrew Ribeiro
 */
public class RibrestDefaultException extends Exception{

    public RibrestDefaultException(String error) {
        this.error = error;
    }
    
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    
    
}
