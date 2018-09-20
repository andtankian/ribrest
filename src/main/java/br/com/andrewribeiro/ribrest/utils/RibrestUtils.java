package br.com.andrewribeiro.ribrest.utils;

import br.com.andrewribeiro.ribrest.core.annotations.RibrestModel;
import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Andrew Ribeiro
 */
public class RibrestUtils {

    public final static RibrestTokens RibrestTokens = new RibrestTokens();
    public final static RibrestJWT RibrestJWT = new RibrestJWT();
    public final static RibrestDefaultResponses RibrestDefaultResponses = new RibrestDefaultResponses();

    public static String getResourceName(Class c) throws RibrestDefaultException {

        if (c == null) {
            throw new RibrestDefaultException("Ribrest couldn't get an valid resource name by this class because it's null.");
        }

        RibrestModel ribrestModelAnnotation = (RibrestModel) c.getAnnotation(RibrestModel.class);

        if (ribrestModelAnnotation == null) {
            throw new RibrestDefaultException(
                    new StringBuilder("Ribrest could'nt get a valid resource name by the class: ")
                            .append(c.getName()).append("\nHave you annotated it with @RibrestModel?")
                            .toString()
            );
        }

        String resourceName = ribrestModelAnnotation.value();
        resourceName = resourceName.isEmpty() ? c.getSimpleName().toLowerCase().concat("s") : resourceName;
        return resourceName;
    }

    public static final Collection getCollectionInstance(Class collectionClass) {
        Collection collection = null;
        if (collectionIsSet(collectionClass)) {
            collection = new HashSet();
        } else if (collectionIsList(collectionClass)) {
            collection = new ArrayList();
        }

        return collection;
    }

    private static final boolean collectionIsSet(Class collectionClass) {
        return Set.class.isAssignableFrom(collectionClass);
    }

    private static final boolean collectionIsList(Class collectionClass) {
        return List.class.isAssignableFrom(collectionClass);
    }
    
    

}
