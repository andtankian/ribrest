package br.com.andrewribeiro.ribrest.services.miner.factory;

import br.com.andrewribeiro.ribrest.services.miner.MinerImpl;
import br.com.andrewribeiro.ribrest.services.miner.interfaces.Miner;
import br.com.andrewribeiro.ribrest.services.miner.factory.interfaces.MinerFactory;

/**
 *
 * @author Andrew Ribeiro
 */
public class MinerFactoryImpl implements MinerFactory{
    @Override
    public Miner getMinerInstance(Class c){
        return new MinerImpl();
    }
    
}
