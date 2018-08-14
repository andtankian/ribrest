package br.com.andrewribeiro.ribrest.exceptions;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andrew Ribeiro
 */
public class RibrestDefaultExceptionFactory {

    private static final Map<String, String> map = new HashMap<>();

    static {
        map.put(RibrestDefaultExceptionConstants.RESOURCE_DOESNT_IMPLEMENTS_ABSTRACT_METHODS, new StringBuilder("The created resource: ")
                .append("{{rn}}")
                .append(" is a IModel subclass but not implements its abstract methods.")
                .toString()
        );
        map.put(RibrestDefaultExceptionConstants.RESOURCE_IS_ABSTRACT, new StringBuilder("The created resource: ")
                .append("{{rn}}")
                .append(" can't be an abstract class.")
                .toString()
        );
        map.put(RibrestDefaultExceptionConstants.RESOURCE_IS_NOT_IMODEL_SUBCLASS, new StringBuilder("The created resource: ")
                .append("{{rn}}")
                .append(" does not implement Model interface.")
                .toString()
        );
        map.put(RibrestDefaultExceptionConstants.RESOURCE_ISNT_AN_ENTITY, new StringBuilder("The created resource: ")
                .append("{{rn}}")
                .append(" isn't an entity. Try to annotate it with @Entity.")
                .toString()
        );
    }

    public static RibrestDefaultException getRibrestDefaultException(String type, String rn) {

        String error = map.get(type);
        
        error = error.replace("{{rn}}", rn);
        
        return new RibrestDefaultException(error);
    }

}
