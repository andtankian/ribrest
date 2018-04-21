package br.com.andrewribeiro.ribrest.services.miner;

/**
 *
 * @author Andrew Ribeiro
 */
public interface IMinerFactory {    
    public IMiner getMinerInstance(Class c);
}
