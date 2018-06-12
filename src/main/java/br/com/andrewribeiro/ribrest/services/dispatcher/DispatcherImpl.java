package br.com.andrewribeiro.ribrest.services.dispatcher;

import br.com.andrewribeiro.ribrest.services.FlowContainer;
import br.com.andrewribeiro.ribrest.services.Result;
import br.com.andrewribeiro.ribrest.services.miner.interfaces.Miner;
import br.com.andrewribeiro.ribrest.services.miner.util.GenericExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import java.util.List;
import javax.ws.rs.core.Response;

/**
 *
 * @author Andrew Ribeiro
 */
public class DispatcherImpl implements Dispatcher{
    
    GsonBuilder jsonBuilder = new GsonBuilder();

    @Override
    public Response send(FlowContainer fc) {
        Miner currentMiner = fc.getMiner();
        setupSerializationStrategy(currentMiner.extractIgnoredFields());
        return buildResultResponse(fc.getResult());
    }
    
    public Response buildResultResponse(Result result){
        return Response.status(result.getStatus()).entity(jsonBuilder.create().toJson(result))
                .build();
    }
    
    public void setupSerializationStrategy(List ignoreFields){
        jsonBuilder.addDeserializationExclusionStrategy(new GenericExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes fa) {
                    return ignoreFields.contains(fa.getName());
                }
            });
    }
    
    
    
}
