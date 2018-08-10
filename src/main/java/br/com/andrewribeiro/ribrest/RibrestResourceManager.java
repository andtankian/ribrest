package br.com.andrewribeiro.ribrest;

import br.com.andrewribeiro.ribrest.annotations.RibrestEndpointConfigurator;
import br.com.andrewribeiro.ribrest.annotations.RibrestModel;
import br.com.andrewribeiro.ribrest.controller.Facade;
import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.logs.RibrestLog;
import br.com.andrewribeiro.ribrest.services.command.Command;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.server.model.Resource;

/**
 *
 * @author Andrew Ribeiro
 */
class RibrestResourceManager extends AbstractRibrestConfigurator {

    Resource.Builder resourceBuilder;
    Class currentClassResource;
    Class currentDao;
    List requestFiltersNameBindings;
    List responseFiltersNameBindings;
    List beforeCommands;
    List afterCommands;

    Set getProgrammaticallyResources(List classes) {
        RibrestLog.log("Creating resources based on scanned models...");
        Set resources = new HashSet();
        classes = classes != null ? classes : new ArrayList();
        for (Object clazz : classes) {
            try {
                resources.add(createResource((Class) clazz));
                RibrestLog.log(new StringBuilder("Resource created at: ").append(ribrest.getCompleteAppUrl()).append(((Class) clazz).getSimpleName().toLowerCase()).append("s").toString());
            } catch (RibrestDefaultException ex) {
                throw new RuntimeException(ex.getError());
            }
        }
        return resources;
    }

    private Facade produceValidFacade(String className) {
        Facade f = new Facade(className);
        f.setBeforeCommandsToCurrentRequest(beforeCommands);
        f.setAfterCommandsToCurrentRequest(afterCommands);
        f.setCurrentDAO(currentDao);
        clearCommands();
        return f;
    }

    private Resource.Builder createResourceBuilder(String resourceName) {
        return Resource.builder(resourceName);
    }

    private Resource createResource(Class currentClassResource) throws RibrestDefaultException {
        this.currentClassResource = currentClassResource;
        resourceBuilder = createResourceBuilder(RibrestUtils.getResourceName(currentClassResource));
        createAllEndpoints();
        return resourceBuilder.build();
    }

    private void createAllEndpoints() {
        createAllDefaultEndpoints();
        createEndpointFromConfigurators(getRibrestEndpointConfiguratorsByMethod("POST"));
        createEndpointFromConfigurators(getRibrestEndpointConfiguratorsByMethod("GET"));
        createEndpointFromConfigurators(getRibrestEndpointConfiguratorsByMethod("PUT"));
        createEndpointFromConfigurators(getRibrestEndpointConfiguratorsByMethod("DELETE"));
    }

    private void createAllDefaultEndpoints() {
        createEndpointFromConfigurators(getRibrestDefaultEndpointConfigurators());
    }

    private void createEndpoint(Resource.Builder resourceBuilder, RibrestEndpointConfiguratorContainer endpointConfiguratorContainer) {
        Resource.Builder tempResourceBuilder;
        if (isSubresource(endpointConfiguratorContainer)) {
            tempResourceBuilder = resourceBuilder.addChildResource(endpointConfiguratorContainer.path);
        } else {
            tempResourceBuilder = resourceBuilder;
        }
        buildEndpoint(tempResourceBuilder, endpointConfiguratorContainer.method);
    }

    private boolean isSubresource(RibrestEndpointConfiguratorContainer endpointConfiguratorContainer) {
        return !("".equals(endpointConfiguratorContainer.path));
    }

    private void buildEndpoint(Resource.Builder resourceBuilder, String method) {
        resourceBuilder.addMethod(method).consumes(MediaType.APPLICATION_FORM_URLENCODED)
                .produces(MediaType.APPLICATION_JSON)
                .nameBindings(getCurrentNameBindings())
                .handledBy(new RibrestInflector(ribrest, produceValidFacade(currentClassResource.getCanonicalName())));
    }

    private void createEndpointFromConfigurators(List<RibrestEndpointConfigurator> endpointConfigurators) {
        endpointConfigurators.stream()
                .forEach((endpointConfigurator) -> {
                    getDataFromEndpointConfiguratorAnnotation(endpointConfigurator);
                    createEndpoint(resourceBuilder,
                            new RibrestEndpointConfiguratorContainer().fromRibrestEndpointConfigurator(endpointConfigurator));
                });
    }

    private List getRibrestEndpointConfiguratorsByMethod(String method) {
        RibrestModel ribrestModelAnnotation = (RibrestModel) currentClassResource.getAnnotation(RibrestModel.class);
        return Arrays.asList(ribrestModelAnnotation.endpointsConfigurators()).stream()
                .filter(ribrestEndpointConfigurator -> ribrestEndpointConfigurator.method().equals(method))
                .collect(Collectors.toList());
    }

    private List getRibrestDefaultEndpointConfigurators() {
        RibrestModel ribrestModelAnnotation = (RibrestModel) currentClassResource.getAnnotation(RibrestModel.class);
        return Arrays.asList(ribrestModelAnnotation.defaultEndpointsConfigurators());
    }

    private List getCommandInstancesFromCommandClassesList(List<Class> commandClasses) {
        return commandClasses.stream()
                .map((commandClass) -> {
                    Command commandInstance = null;
                    try {
                        commandInstance = getCommandInstanceFromClass(commandClass);
                    } catch (IllegalAccessException | InstantiationException e) {
                    }
                    return commandInstance;
                }).collect(Collectors.toList());
    }

    private Command getCommandInstanceFromClass(Class commandClass) throws InstantiationException, IllegalAccessException {
        Object commandInstance = commandClass.newInstance();
        if (commandInstance instanceof Command) {
            return (Command) commandInstance;
        }

        return null;
    }

    private void getDataFromEndpointConfiguratorAnnotation(RibrestEndpointConfigurator endpointConfigurator) {
        requestFiltersNameBindings = Arrays.asList(endpointConfigurator.requestFiltersNameBindings());
        responseFiltersNameBindings = Arrays.asList(endpointConfigurator.responseFiltersNameBindings());
        beforeCommands = getCommandInstancesFromCommandClassesList(Arrays.asList(endpointConfigurator.beforeCommands()));
        afterCommands = getCommandInstancesFromCommandClassesList(Arrays.asList(endpointConfigurator.afterCommands()));
        currentDao = endpointConfigurator.dao();
    }
    
    private List getCurrentNameBindings(){
        List allCurrentNameBindings = new ArrayList();
        allCurrentNameBindings.addAll(requestFiltersNameBindings);
        allCurrentNameBindings.addAll(responseFiltersNameBindings);
        return allCurrentNameBindings;
    }

    private void clearCommands() {
        beforeCommands = new ArrayList();
        afterCommands = new ArrayList();
    }
}
