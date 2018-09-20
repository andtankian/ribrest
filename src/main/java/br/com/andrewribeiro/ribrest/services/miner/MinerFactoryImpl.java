package br.com.andrewribeiro.ribrest.services.miner;

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
