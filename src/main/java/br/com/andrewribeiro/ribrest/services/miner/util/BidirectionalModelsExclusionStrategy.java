package br.com.andrewribeiro.ribrest.services.miner.util;

import br.com.andrewribeiro.ribrest.model.interfaces.Model;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import java.util.List;

/**
 *
 * @author Andrew Ribeiro
 */
public class BidirectionalModelsExclusionStrategy implements ExclusionStrategy {

    public BidirectionalModelsExclusionStrategy(List<Model> models, List<String> ignoredFields) {
        this.models = models;
        this.ignoredFields = ignoredFields;
        excludeBidirectionalObjects();
    }
    
    

    private List<Model> models;
    private List<String> ignoredFields;
    private final Integer expectedDepth = 5;

    public final void excludeBidirectionalObjects() {
        models.forEach(model -> {
            goUnderModelAttributes(model, 1);
        });
    }

    private void goUnderModelAttributes(Model model, final Integer currentDepth) {
        if(modelIsNull(model)) return;
        model.getAllModelAttributes().forEach(attribute -> {
            Model attributeValue;
            attribute.setAccessible(true);
            try {
                attributeValue = (Model) attribute.get(model);
                if (currentDepth.equals(expectedDepth)) {
                    setAttributesToNull(attributeValue);
                } else {
                    goUnderModelAttributes(attributeValue, (currentDepth + 1));
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void setAttributesToNull(Model model) {
        if (modelIsNull(model)) {
            return;
        }
        model.getAllModelAttributes()
                .forEach(attribute -> {
                    attribute.setAccessible(true);
                    try {
                        attribute.set(model, null);
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }
    
    private boolean modelIsNull(Model model){
        return model == null;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes fa) {
        return ignoredFields.contains(fa.getName());
    }

    @Override
    public boolean shouldSkipClass(Class<?> type) {
        return false;
    }

}
