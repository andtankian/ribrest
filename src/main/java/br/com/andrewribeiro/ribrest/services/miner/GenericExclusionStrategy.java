package br.com.andrewribeiro.ribrest.services.miner;

import com.google.gson.ExclusionStrategy;

/**
 *
 * @author Andrew Ribeiro
 */
public abstract class GenericExclusionStrategy implements ExclusionStrategy{

    @Override
    public boolean shouldSkipClass(Class<?> type) {
        return false;
    }
    
}
