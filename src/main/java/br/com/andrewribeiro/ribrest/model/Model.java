package br.com.andrewribeiro.ribrest.model;

import br.com.andrewribeiro.ribrest.annotations.RibrestIgnoreField;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrew Ribeiro
 */

public abstract class Model implements IModel, Serializable{

    private Long id;

    public Model() {}
    public Model(Long id){
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public List getIgnoredAttributes() {
        Class c = this.getClass();
        List lfields = new ArrayList();
        for (; c != null; c = c.getSuperclass()) {
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                if(field.isAnnotationPresent(RibrestIgnoreField.class)) lfields.add(field.getName());
            }
        }

        return lfields;
    }
    
    
    
    
    
    
}
