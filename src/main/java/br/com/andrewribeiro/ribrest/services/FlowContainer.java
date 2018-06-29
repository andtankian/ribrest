package br.com.andrewribeiro.ribrest.services;

import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultExceptionConstants;
import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultExceptionFactory;
import br.com.andrewribeiro.ribrest.services.holder.HolderImpl;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import java.lang.reflect.Modifier;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.container.ContainerRequestContext;
import br.com.andrewribeiro.ribrest.services.miner.interfaces.Miner;
import br.com.andrewribeiro.ribrest.model.interfaces.Model;
import br.com.andrewribeiro.ribrest.services.holder.interfaces.Holder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andrew Ribeiro
 */
public class FlowContainer {

    public FlowContainer() {
        go = true;
        holder = new HolderImpl();
        result = new Result();
    }

    private Miner miner;
    private Holder holder;
    private Model model;

    private Result result;
    private String method;
    private final Map extraObjects = new HashMap();

    @Inject
    private EntityManager em;

    private boolean go;

    ContainerRequestContext cr;

    public Miner getMiner() {
        return miner;
    }

    public void setMiner(Miner miner) {
        this.miner = miner;
    }

    public Holder getHolder() {
        return holder;
    }

    public void setHolder(Holder holder) {
        this.holder = holder;
    }

    public boolean shouldGo() {
        return go;
    }

    public void setGo(boolean go) {
        this.go = go;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
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

    public void initModelInstance(Class classType) throws Exception {
        if (Modifier.isAbstract(classType.getModifiers())) {
            throw RibrestDefaultExceptionFactory.getRibrestDefaultException(RibrestDefaultExceptionConstants.RESOURCE_IS_ABSTRACT, RibrestUtils.getResourceName(classType));
        }
        Object tempInstance = classType.newInstance();
        if (!(tempInstance instanceof Model)) {
            throw RibrestDefaultExceptionFactory.getRibrestDefaultException(RibrestDefaultExceptionConstants.RESOURCE_IS_NOT_IMODEL_SUBCLASS, RibrestUtils.getResourceName(classType));
        }
        this.model = (Model) tempInstance;
    }

    public Object getExtraObject(String key) {
        return this.extraObjects.get(key);
    }
    
    public void addExtraObject(String key, Object object){
        this.extraObjects.put(key, object);
    }

}
