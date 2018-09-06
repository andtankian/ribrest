package br.com.andrewribeiro.ribrest.services.miner.abstracts;

import br.com.andrewribeiro.ribrest.services.miner.interfaces.Miner;
import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.services.FlowContainer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import org.glassfish.jersey.server.ContainerRequest;
import br.com.andrewribeiro.ribrest.model.interfaces.Model;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import br.com.andrewribeiro.ribrest.annotations.RibrestWontFill;

/**
 *
 * @author Andrew Ribeiro
 */
public abstract class AbstractMiner implements Miner {

    @Inject
    protected FlowContainer fc;

    protected MultivaluedMap<String, String> form;
    protected MultivaluedMap<String, String> query;
    protected MultivaluedMap<String, String> path;
    protected MultivaluedMap<String, String> header;

    protected List accepts;

    private List ignored;

    @Override
    public void extractDataFromRequest(ContainerRequest cr) throws RibrestDefaultException {
        prepareDataObjects(cr);
    }

    @Override
    public List extractIgnoredFields() {
        ignored = ignored != null ? ignored : new ArrayList();
        accepts = accepts != null ? accepts : new ArrayList();

        ignored.removeAll(accepts);

        return new ArrayList(ignored);
    }

    private void prepareDataObjects(ContainerRequest cr) {
        Form f = cr.readEntity(Form.class);
        form = f.asMap();

        UriInfo u = cr.getUriInfo();
        query = u.getQueryParameters();

        path = u.getPathParameters();

        header = cr.getHeaders();

        accepts = query != null ? query.get("accepts") : new ArrayList();
        accepts = accepts != null ? accepts : new ArrayList();
        
        Map<String, Integer> limitAndOffset = new QueryMiner().extractLimitAndOffset(query);
        
        fc.getHolder().getSm().setLimit(limitAndOffset.get("limit"));
        fc.getHolder().getSm().setOffset(limitAndOffset.get("offset"));
    }

    protected void fillModel(Model model) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
        try {
            model.setId(Long.parseLong(path.getFirst("id")));
        } catch (NumberFormatException nfe) {}

        for (Field attribute : model.getAllAttributesExceptsId()) {
            fillAttribute(new FieldHelper(attribute, model, attribute.getName()));
        }
    }

    private void fillAttribute(FieldHelper fieldHelper) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
        if (fieldHelper.attribute.getType() == String.class
                || fieldHelper.attribute.getType() == Long.class) {
            fieldHelper.fillNonEntityAttribute();
        } else if (fieldHelper.attribute.isAnnotationPresent(OneToOne.class)) {
            fieldHelper.fillEntityAttribute();
        } else if(fieldHelper.attribute.isAnnotationPresent(OneToMany.class)){
            fieldHelper.fillManyEntityAttribute();
        } else if(fieldHelper.attribute.isAnnotationPresent(ManyToOne.class) && !fieldHelper.attribute.isAnnotationPresent(RibrestWontFill.class)){
            fieldHelper.fillEntityAttribute();
        }
    }

    private void fillChildModel(Model childModel, String parentAttributeName) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
        for (Field attribute : childModel.getAllAttributesExceptsBidirectionalModels()) {
            fillAttribute(new FieldHelper(attribute, childModel, parentAttributeName + "." + attribute.getName()));
        }
    }

    class FieldHelper {

        public FieldHelper(Field attribute, Model model, String parameterName) {
            this.attribute = attribute;
            this.model = model;
            this.parameterName = parameterName;
        }

        Field attribute;
        Model model;
        String parameterName;
        Object parameterValue;

        void fillNonEntityAttribute() throws IllegalArgumentException, IllegalAccessException {
            parameterValue = form.getFirst(parameterName);
            fill();
        }

        void fillEntityAttribute() throws InstantiationException, IllegalAccessException {
            parameterValue = attribute.getType().newInstance();
            fillChildModel((Model) parameterValue, attribute.getName());
            fill();
        }
        
        void fillManyEntityAttribute() throws InstantiationException, IllegalAccessException{
            Collection collection = getCollectionInstance();
            Class collectionType = extractCollectionTypedClass();
            getChildrenIds().forEach(stringId -> {
                try {
                    Model model = (Model) collectionType.newInstance();
                    model.setId(Long.parseLong(stringId));
                    fillInverseAttributeInRelationship(model);
                    collection.add(model);
                } catch(Exception e){
                    e.toString();
                }
            });
            parameterValue = collection;
            fill();
        }
        
        void fillInverseAttributeInRelationship(Model model){
            model.getAllInverseCollectionModelAttributes()
                    .forEach(attribute -> {
                        if(attribute.getType().getSimpleName().equals(this.model.getClass().getSimpleName())){
                            attribute.setAccessible(true);
                            try {
                                attribute.set(model, this.model);
                            } catch (IllegalArgumentException | IllegalAccessException ex) {
                                throw new RuntimeException(ex.toString());
                            }
                        }
                    });
        }

        void fill() throws IllegalArgumentException, IllegalAccessException {
            attribute.setAccessible(true);
            attribute.set(model, parseBeforeSetParameterValue(parameterValue));
        }

        Object parseBeforeSetParameterValue(Object parameterValue) {
            if (attribute.getType() == Long.class && parameterValue != null) {
                parameterValue = Long.parseLong((String) parameterValue);
            }

            return parameterValue;
        }
        
        private Class extractCollectionTypedClass(){
            Class collectionType = null;
            if(attributeIsACollection()){
                ParameterizedType type = (ParameterizedType) attribute.getGenericType();
                collectionType = (Class) type.getActualTypeArguments()[0];
            }
            
            return collectionType;
        }
        
        private boolean attributeIsACollection(){
            return Collection.class.isAssignableFrom(attribute.getType());
        }
        
        private boolean attributeIsASet(){
            return Set.class.isAssignableFrom(attribute.getType());
        }
        
        private boolean attributeIsList(){
            return List.class.isAssignableFrom(attribute.getType());
        }
        
        private List<String> getChildrenIds(){
            List childrenId = form.get(parameterName + ".id");
            return childrenId == null ? new ArrayList() : childrenId;
        }
        
        private Collection getCollectionInstance(){
            Collection collection;
            if(attributeIsASet()){
                collection = new HashSet();
            } else if(attributeIsList()){
                collection = new ArrayList();
            } else {
                collection = null;
            }
            
            return collection;
        }
    }
}
