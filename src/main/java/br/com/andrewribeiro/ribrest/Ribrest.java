package br.com.andrewribeiro.ribrest;

import br.com.andrewribeiro.ribrest.annotations.RibrestModel;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;
import java.util.List;
import br.com.andrewribeiro.ribrest.annotations.RibrestResource;
import br.com.andrewribeiro.ribrest.controller.Facade;
import br.com.andrewribeiro.ribrest.logs.RibrestLog;
import br.com.andrewribeiro.ribrest.services.cdi.hk2.RibrestSLPopulator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.model.Resource;

public class Ribrest {

    private static Ribrest instance;

    private List scannedResourceClassesNames;
    private List scannedResourceClasses;
    private List scannedModelClassesNames;
    private List scannedModelClasses;
    private ServiceLocator serviceLocator;
    private HttpServer server;

    public static Ribrest getInstance() {
        instance = instance != null ? instance : new Ribrest();
        return instance;
    }

    /**
     * Function to init all the Ribrest configuration
     */
    public void init() {

        /*
        Let's get and starts a server with
        1) An array of all package names who contains classes annotated with @RibrestResource
        2) A string that will be a URI to be accessible via http requests. (including its port and appname)
        3) A new instance of ResourceConfig
         */
        server = getServer(getResourcesPackagesNames(), "http://localhost:2007/ribrestapp", new ResourceConfig());
//        try {
//            System.in.read();
//        } catch (IOException ex) {
//        } finally {
//            server.shutdown();
//        }
    }
    
    public void shutdown(){
        server.shutdown();
    }

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
     * application.
     *
     * @return Grizzly HTTP server.
     */
    private HttpServer getServer(String[] packages, String baseUri, ResourceConfig rc) {

        /*
        CREATING RESOURCECONFIG INSTANCE
        */
        rc = new ResourceConfig().packages(packages);
        
        /*
        SETTING UP ALL HK2 CDI
        */
        
        serviceLocator = ServiceLocatorFactory.getInstance().create(null);
        RibrestSLPopulator.populate(serviceLocator);
        
        /*
        FINDING ALL @RibrestModel classes to create its respective dynamic resources.
        */
        Set resources = createResources(getModelClasses());
        rc.registerResources(resources);
        

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at the uri
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), rc, serviceLocator);
    }

    /**
     * Method to return the array of resource package names.
     *
     * @return Array of resource package names.
     */
    private String[] getResourcesPackagesNames() {
        RibrestLog.getLogger().info("Finding all annotated resources classes and its respectives package names.");
        return getScannedPackageNamesArray(new ArrayList(new HashSet(getResourcesClassesNames())));
    }
    
    /**
     * Method that return a list of found resource classes names.
     * 
     * @return List of resource classes name.
     */
    private List getResourcesClassesNames() {
        RibrestLog.getLogger().info("Getting resources classes names: ");
        scannedResourceClassesNames = scannedResourceClassesNames != null ? scannedResourceClassesNames : getScannedClassesName(RibrestResource.class);
        RibrestLog.getLogger().info(scannedResourceClassesNames.toString());
        return scannedResourceClassesNames;
    }
    
    
    private List getResourcesClasses() {
        RibrestLog.getLogger().info("Getting resource classes instances: ");
        scannedResourceClasses = scannedResourceClasses != null ? scannedResourceClasses : new ArrayList(getScannedClasses(getResourcesClassesNames()));
        RibrestLog.getLogger().info(scannedResourceClasses.toString());
        return scannedResourceClasses;
    }
    
    /**
     * Method to return a list of model classes names annotaded with @RibrestModel annotation.
     * 
     * @return A list of model classes names annotated with @RibrestModel annotation. 
     */
    private List getModelClassesNames(){
        RibrestLog.getLogger().info("Getting model classes names: ");
        scannedModelClassesNames = scannedModelClassesNames != null ? scannedModelClassesNames : getScannedClassesName(RibrestModel.class);
        RibrestLog.getLogger().info(scannedModelClassesNames.toString());
        return scannedModelClassesNames;
    }
    
    /**
     * Method that returns a list of Model Classes instance.
     * 
     * @return 
     */
     private List getModelClasses() {
        RibrestLog.getLogger().info("Getting model classes instances: ");
        scannedModelClasses = scannedModelClasses != null ? scannedModelClasses : new ArrayList(getScannedClasses(getModelClassesNames()));
        RibrestLog.getLogger().info(scannedModelClasses.toString());
        return scannedModelClasses;
    }
    
    private Set createResources(List classes){
        RibrestLog.getLogger().info("Creating dynamic resources based on RibrestModels");
        Set resources = new HashSet();
        classes = classes != null ? classes : new ArrayList();
        System.out.println(classes);
        for (final Object clazz : classes) {
            Resource.Builder rb = Resource.builder(new StringBuilder(((Class)clazz).getSimpleName().toLowerCase()).append("s").toString());
            rb.addMethod("GET")
                    .produces(MediaType.APPLICATION_JSON).handledBy(new Inflector<ContainerRequestContext, Response>(){
                @Override
                public Response apply(ContainerRequestContext data) {
                    Facade f = new Facade((ContainerRequest)data, clazz.toString());
                    serviceLocator.inject(f);
                    serviceLocator.postConstruct(f);
                    return f.process();
                }
            });
            resources.add(rb.build());
        }
        return resources;
    }

    /**
     * Generic method to return list of classes names based on the parameter
     * annotation
     *
     * @param annotation
     * @return List of classes names filtered by the parameter passed
     * annotation.
     */
    private List getScannedClassesName(Class annotation) {
        RibrestLog.getLogger().info(new StringBuilder("Starting FastClasspathScanner process to @").append(annotation.getSimpleName())
                .append(" annotated resource class...").toString());
        return new FastClasspathScanner().scan()
                .getNamesOfClassesWithAnnotation(annotation);
    }

    /**
     *
     * @return
     */
    private Set getScannedClasses(List scannedClassesName) {
        Set classes = new HashSet();
        for (Object scannedClass : scannedClassesName) {
            try {
                Class clazz = Class.forName((String) scannedClass);
                classes.add(clazz);
            } catch (ClassNotFoundException ex) {
                RibrestLog.getLogger().severe(new StringBuilder("Couldn't get an instance of Class given classname: ").append(scannedClass).toString());
            }
        }

        return classes;
    }

    /**
     * Generic method to extract all the package names given a list of complete
     * classes names.
     *
     * @param classes
     * @return String array containing all the packagenames of the parameter
     * passed list of classes name.
     */
    private String[] getScannedPackageNamesArray(List classes) {
        RibrestLog.getLogger().info(new StringBuilder("Getting all respective packagenames of ")
                .append(classes).append(".").toString());
        classes = classes != null ? classes : new ArrayList();
        String[] packages = new String[classes.size()];
        for (int i = 0; i < classes.size(); i++) {
            packages[i] = ((String) classes.get(i)).substring(0, ((String) classes.get(i)).lastIndexOf("."));
        }

        return packages;
    }

}
