package br.com.andrewribeiro.ribrest.services.dispatcher;

import br.com.andrewribeiro.ribrest.model.interfaces.Model;
import br.com.andrewribeiro.ribrest.services.FlowContainer;
import br.com.andrewribeiro.ribrest.services.Result;
import br.com.andrewribeiro.ribrest.services.miner.interfaces.Miner;
import br.com.andrewribeiro.ribrest.services.miner.util.BidirectionalModelsExclusionStrategy;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

/**
 *
 * @author Andrew Ribeiro
 */
public class DispatcherImpl implements Dispatcher{
    
    @Inject
    FlowContainer flowContainer;
    
    GsonBuilder jsonBuilder = new GsonBuilder();
    
    @Override
    public Response send() {
        Miner currentMiner = flowContainer.getMiner();
        setModelsToResult();
        setupSerializationStrategy(currentMiner.extractIgnoredFields());
        return buildResultResponse();
    }
    
    private Response buildResultResponse(){
        return Response.status(flowContainer.getResult().getStatus()).entity(jsonBuilder.create().toJson(flowContainer.getResult())).build();
    }
    
    private void setupSerializationStrategy(List ignoreFields){
        BidirectionalModelsExclusionStrategy exclusionStrategy = new BidirectionalModelsExclusionStrategy(alwaysGetListOfModels(flowContainer.getResult()), ignoreFields);
        jsonBuilder.addDeserializationExclusionStrategy(exclusionStrategy);
    }
    
    private void setModelsToResult(){
        flowContainer.getResult().setHolder(flowContainer.getHolder());
    }
    
    private List<Model> alwaysGetListOfModels(Result result){
        return result.getHolder() == null ? new ArrayList() : result.getHolder().getModels();
    }
}
