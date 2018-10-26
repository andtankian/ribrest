package br.com.andrewribeiro.ribrest.services.dtos;

import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultExceptionConstants;
import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultExceptionFactory;
import br.com.andrewribeiro.ribrest.services.holder.HolderImpl;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import java.lang.reflect.Modifier;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.container.ContainerRequestContext;
import br.com.andrewribeiro.ribrest.services.miner.Miner;
import br.com.andrewribeiro.ribrest.core.model.Model;
import br.com.andrewribeiro.ribrest.services.holder.Holder;
import br.com.andrewribeiro.ribrest.services.miner.RequestMaps;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andrew Ribeiro
 */
public class FlowContainer {

    public FlowContainer() {
        holder = new HolderImpl();
        result = new Result();
    }

    private Miner miner;
    private RequestMaps requestMaps;
    private Holder holder;
    private Model model;

    private Result result;
    private String method;
    private final Map extraObjects = new HashMap();

    @Inject
    private EntityManager entityManager;

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

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setModelFromClass(Class modelClass) throws Exception {
        if (Modifier.isAbstract(modelClass.getModifiers())) {
            throw RibrestDefaultExceptionFactory.getRibrestDefaultException(RibrestDefaultExceptionConstants.RESOURCE_IS_ABSTRACT, RibrestUtils.getResourceName(modelClass));
        }
        Object tempInstance = modelClass.newInstance();
        if (!(tempInstance instanceof Model)) {
            throw RibrestDefaultExceptionFactory.getRibrestDefaultException(RibrestDefaultExceptionConstants.RESOURCE_IS_NOT_IMODEL_SUBCLASS, RibrestUtils.getResourceName(modelClass));
        }
        this.model = (Model) tempInstance;
    }

    public Object getExtraObject(String key) {
        return this.extraObjects.get(key);
    }

    public void addExtraObject(String key, Object object) {
        this.extraObjects.put(key, object);
    }

    public void removeExtraObject(String key) {
        this.extraObjects.remove(key);
    }

    public RequestMaps getRequestMaps() {
        return requestMaps;
    }

    public void setRequestMaps(RequestMaps requestMaps) {
        this.requestMaps = requestMaps;
    }

}
