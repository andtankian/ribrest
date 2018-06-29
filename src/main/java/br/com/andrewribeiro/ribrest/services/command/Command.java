package br.com.andrewribeiro.ribrest.services.command;

import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;

/**
 *
 * @author Andrew Ribeiro
 */
public interface Command {
    
    public void execute() throws RibrestDefaultException, Exception;
    
}
