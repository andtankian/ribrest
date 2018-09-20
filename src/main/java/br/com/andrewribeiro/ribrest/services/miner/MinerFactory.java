package br.com.andrewribeiro.ribrest.services.miner;

/**
 *
 * @author Andrew Ribeiro
 */
public interface MinerFactory {    
    public Miner getMinerInstance(Class c);
}
