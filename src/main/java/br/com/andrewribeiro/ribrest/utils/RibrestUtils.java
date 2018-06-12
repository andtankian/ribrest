package br.com.andrewribeiro.ribrest.utils;

import br.com.andrewribeiro.ribrest.annotations.RibrestModel;
import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;

/**
 *
 * @author Andrew Ribeiro
 */
public class RibrestUtils {

    /**
     * This static method returns a resource name given a classe instance. This
     * static method works by getting @RibrestModel annotation of a class
     * instance. If the class instance is null or isn't annotated with
     *
     * @RibrestModel it throws an RibrestDefaultException It it contains the
     * annotation, this method try to get a valid value from this annotation. If
     * a default value is not provided, so the resource name is the name of the
     * current class instance plus an "s". If a default value is provided so the
     * resource name is this default value.
     *
     * @param c Class instance of the resource being requested.
     * @return String that is the resource name of the resource being requested.
     * @throws RibrestDefaultException in case of the requested resource is not
     * annotated with @RibrestModel
     */
    public static String getResourceName(Class c) throws RibrestDefaultException {
        
        //Test if class instance passed by parameter is null
        if (c == null) {
            //class instance is null
            
            //Throw an exception saying the we can't get a valid resource name by a null instance
            throw new RibrestDefaultException("Ribrest couldn't get an valid resource name by this class because it's null.");
        }
        
        //Get the annotation of class instance passed by parameter
        RibrestModel ribrestModelAnnotation = (RibrestModel) c.getAnnotation(RibrestModel.class);
        
        //Test if the retrieved annotation is null
        if (ribrestModelAnnotation == null) {
            //Restrieved annotation is null or the class instance isn't annotated with expected annotation
            
            //Throw an exception saying that the current class instance isn't annotated with @RibrestModel
            throw new RibrestDefaultException(
                    new StringBuilder("Ribrest could'nt get a valid resource name by the class: ")
                            .append(c.getName()).append("\nHave you annotated it with @RibrestModel?")
                            .toString()
            );
        }
        
        //Get the default value of the annotation.
        String resourceName = ribrestModelAnnotation.value(); 
        
        /*
        Test if default value is empty.
        If it is empty, so the resource name will be assigned to the name of the current class instance plus "s"
        If it is not empty, so resource name will be assigned to the default value.
        */
        resourceName = resourceName.isEmpty() ? c.getSimpleName().toLowerCase().concat("s") : resourceName;
        
        //Return the resource name of the current class instance.
        return resourceName;
    }
}
