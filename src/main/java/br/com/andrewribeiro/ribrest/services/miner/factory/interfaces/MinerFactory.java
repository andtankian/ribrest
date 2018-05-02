package br.com.andrewribeiro.ribrest.services.miner.factory.interfaces;

import br.com.andrewribeiro.ribrest.services.miner.interfaces.Miner;

/**
 *
 * @author Andrew Ribeiro
 */
public interface MinerFactory {    
    public Miner getMinerInstance(Class c);
}
