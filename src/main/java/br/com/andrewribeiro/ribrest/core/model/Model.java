package br.com.andrewribeiro.ribrest.core.model;

import br.com.andrewribeiro.ribrest.core.annotations.RibrestIgnoreField;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Andrew Ribeiro
 */
public interface Model {

    public final static String PERSISTED_MODEL_KEY = UUID.randomUUID().toString();

    default public void merge(Model modelToMerge) {
        List<Field> thisInstanceAttributes = this.getAllAttributesExceptsId();
        List<Field> modelToMergeAttributes = modelToMerge.getAllAttributesExceptsId();
        IntStream.range(0, thisInstanceAttributes.size()).forEach((index) -> {
            Field thisIntanceField = thisInstanceAttributes.get(index);
            Field modelToMergeField = modelToMergeAttributes.get(index);
            thisIntanceField.setAccessible(true);
            modelToMergeField.setAccessible(true);
            try {
                Object attributeInstance = modelToMergeField.get(modelToMerge);
                attributeInstance = attributeInstance == null ? thisIntanceField.get(this) : attributeInstance;
                if (modelToMergeField.isAnnotationPresent(OneToOne.class)) {
                    attributeInstance = ((Model) attributeInstance).getId() == null ? thisIntanceField.get(this) : attributeInstance;
                }
                thisIntanceField.set(this, attributeInstance);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new RuntimeException(ex.getCause());
            }
        });
    }

    default public List<Field> getAllAttributes() {
        Class thisClass = this.getClass();
        List<Field> thisClassFields = new ArrayList();
        for (; thisClass != null; thisClass = thisClass.getSuperclass()) {
            Field[] fields = thisClass.getDeclaredFields();
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    thisClassFields.add(field);
                }
            }
        }
        return thisClassFields;
    }

    default public List<Field> getAllAttributesExceptsId() {
        return getAllAttributes().stream()
                .filter(attribute -> !attribute.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
    }

    default public List<Field> getAllAttributesExceptsBidirectionalModels() {
        return getAllAttributes().stream()
                .filter(attribute -> {
                    return !(attribute.isAnnotationPresent(OneToOne.class) && !attribute.getAnnotation(OneToOne.class).mappedBy().isEmpty());
                })
                .collect(Collectors.toList());
    }

    default public List<Field> getAllModelAttributes() {
        return getAllAttributes().stream()
                .filter(attribute -> Model.class.isAssignableFrom(attribute.getType()))
                .collect(Collectors.toList());
    }

    default public List<Field> getAllModelNonBidirectionalAttributes() {
        return getAllModelAttributes().stream().filter(attribute -> !(attribute.isAnnotationPresent(OneToOne.class) && "".equals(attribute.getAnnotation(OneToOne.class).mappedBy())))
                .collect(Collectors.toList());
    }
    
    default public List<Field> getAllCollectionModelAttributes(){
        return getAllAttributes().stream()
                .filter(attribute -> attribute.isAnnotationPresent(OneToMany.class))
                .collect(Collectors.toList());
    }
    
    default public List<Field> getAllInverseCollectionModelAttributes(){
        return getAllAttributes().stream()
                .filter(attribute -> attribute.isAnnotationPresent(ManyToOne.class))
                .collect(Collectors.toList());
    }

    default public List<Field> getIgnoredAttributes() {

        List<Field> ignoredFields, allFields;
        ignoredFields = new ArrayList();
        allFields = getAllAttributes();
        allFields.forEach((field) -> {
            if (field.isAnnotationPresent(RibrestIgnoreField.class)) {
                ignoredFields.add(field);
            }
        });
        return ignoredFields;
    }

    public Long getId();

    public void setId(Long id);

}
