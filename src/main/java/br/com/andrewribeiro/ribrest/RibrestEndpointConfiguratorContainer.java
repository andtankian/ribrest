package br.com.andrewribeiro.ribrest;

import br.com.andrewribeiro.ribrest.core.annotations.RibrestEndpointConfigurator;

/**
 *
 * @author Andrew Ribeiro
 */
class RibrestEndpointConfiguratorContainer {

    String method;
    String path;

    RibrestEndpointConfiguratorContainer fromRibrestEndpointConfigurator(RibrestEndpointConfigurator endpointConfigurator) {
        this.method = endpointConfigurator.method();
        this.path = endpointConfigurator.path();
        return this;
    }
}
