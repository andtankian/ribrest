package br.com.andrewribeiro.ribrest.services.miner;

/**
 *
 * @author Andrew Ribeiro
 */
public class MinerFactory implements IMinerFactory{
    @Override
    public IMiner getMinerInstance(Class c){
        return new ConcreteMiner();
    }
    
}
