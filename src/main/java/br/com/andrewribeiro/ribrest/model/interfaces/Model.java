package br.com.andrewribeiro.ribrest.model.interfaces;

import br.com.andrewribeiro.ribrest.annotations.RibrestIgnoreField;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrew Ribeiro
 */
public interface Model {
    
    public void merge();
    /**
     * Method that returns a list of all not static attributes of the current model class.
     * This method works recursively getting the parent class of the current instance class.
     * For each super class, this method get all the declared fields and test each field for being not static field.
     * If the field is not static, it's added to the list and the list is returned at the end.
     * 
     * @return List of fields objects of the current model class.
     */
    default public List<Field> getAllAttributes() {
        //Get the current class instance
        Class thisClass = this.getClass();
        
        //Create a new List that will be the containter for not static fields
        List<Field> lfields = new ArrayList();
        
        //Loop to recursively get the parent of the class until it's different of null (Or Object)
        for (; thisClass != null; thisClass = thisClass.getSuperclass()) {
            
            //Get declared field of the current node
            Field[] fields = thisClass.getDeclaredFields();
            
            //Loop over each field of the current node
            for (Field field : fields) {
                
                //Test if the current field is NOT a static variable
                if(!Modifier.isStatic(field.getModifiers())){
                    //If it's not a static variable
                    
                    //Add to the list of fields that will be returned
                    lfields.add(field);
                }
            }
        }
        
        //Return the list of fields
        return lfields;
    }
    
    /**
     * Method to return a list of fields that are annotated with @RibrestIgnoreField.
     * This method works by invoking the getAllAttributes method to return a list of all
     * attributes and then, filtering those that are annotated with @RibrestIgnoreField annotation.
     * Those who are annotated are added to a list. This list if ignored fields are returned.
     * 
     * @return List<Field> of field that are annotated with @RibrestIgnoreField
     */
    default public List<Field> getIgnoredAttributes() {
        
        //List of ignored fields and all fields.
       List<Field> ignoredFields, allFields;
       
       //Initialize an empty list to be the container for ignored fields
       ignoredFields = new ArrayList();
       
       //Get all fields by invoking getAllAttributes method
       allFields = getAllAttributes();
       
       //Iterate over each field returned by getAllAttributes method.
       allFields.forEach((field)->{
           
           //Test if the current field has the annotation @RibrestIgnoreField present
           if(field.isAnnotationPresent(RibrestIgnoreField.class)){
               //Has @RibrestIgnoreField annotation presente
               
               //Add this field to ignored fields list
               ignoredFields.add(field);
           }
       });
       
       //Return the ignored field list
       return ignoredFields;
    }
    
    public Long getId();
    public void setId(Long id);
}
