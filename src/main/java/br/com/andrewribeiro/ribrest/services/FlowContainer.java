package br.com.andrewribeiro.ribrest.services;

import br.com.andrewribeiro.ribrest.services.miner.IMiner;
import br.com.andrewribeiro.ribrest.services.holder.IHolder;
import br.com.andrewribeiro.ribrest.services.holder.ConcreteHolder;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.container.ContainerRequestContext;

/**
 *
 * @author Andrew Ribeiro
 */
public class FlowContainer {

    public FlowContainer() {
        go = true;
        holder = new ConcreteHolder();
        result = new Result();
    }

    private IMiner miner;
    private IHolder holder;
    private Result result;
    
    @Inject
    private EntityManager em;

    private boolean go;

    ContainerRequestContext cr;

    public IMiner getMiner() {
        return miner;
    }

    public void setMiner(IMiner miner) {
        this.miner = miner;
    }

    public IHolder getHolder() {
        return holder;
    }

    public void setHolder(IHolder holder) {
        this.holder = holder;
    }

    public boolean shouldGo() {
        return go;
    }

    public void setGo(boolean go) {
        this.go = go;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
    
    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

}
