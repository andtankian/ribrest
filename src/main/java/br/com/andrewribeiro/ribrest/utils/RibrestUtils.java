package br.com.andrewribeiro.ribrest.utils;

import br.com.andrewribeiro.ribrest.annotations.RibrestModel;

/**
 *
 * @author Andrew Ribeiro
 */
public class RibrestUtils {
    
    public static String getResourceName(Class c) {
        RibrestModel rm = (RibrestModel) c.getAnnotation(RibrestModel.class);
        String rn = rm.value();
        rn = rn.isEmpty() ? c.getSimpleName().toLowerCase().concat("s") : rn;
        return rn;
    }
}
