package br.com.andrewribeiro.ribrest.services;

import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultExceptionConstants;
import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultExceptionFactory;
import br.com.andrewribeiro.ribrest.model.IModel;
import br.com.andrewribeiro.ribrest.services.miner.IMiner;
import br.com.andrewribeiro.ribrest.services.holder.IHolder;
import br.com.andrewribeiro.ribrest.services.holder.ConcreteHolder;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import java.lang.reflect.Modifier;
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
    private Object model;

    private Result result;
    private String method;

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
    
    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    
    public void setupEntity(Class classType) throws Exception{
        if(Modifier.isAbstract(classType.getModifiers())){
            throw RibrestDefaultExceptionFactory.getRibrestDefaultException(RibrestDefaultExceptionConstants.RESOURCE_IS_ABSTRACT, RibrestUtils.getResourceName(classType));
        }
        this.model = classType.newInstance();
    }

}
