package br.com.andrewribeiro.ribrest;

/**
 *
 * @author Andrew Ribeiro
 */
abstract class AbstractRibrestConfigurator implements RibrestConfigurator {
    
    Ribrest ribrest;

    @Override
    public void setRibrestInstance(Ribrest ribrest) {
        this.ribrest = ribrest;
    }
}
