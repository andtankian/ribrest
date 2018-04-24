package br.com.andrewribeiro.ribrest;

import br.com.andrewribeiro.ribrest.annotations.RibrestModel;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;
import java.util.List;
import br.com.andrewribeiro.ribrest.controller.Facade;
import br.com.andrewribeiro.ribrest.logs.RibrestLog;
import br.com.andrewribeiro.ribrest.services.orm.PersistenceUnitWrapper;
import br.com.andrewribeiro.ribrest.services.cdi.hk2.RibrestSLPopulator;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Path;
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

    private static String DEFAULT_BASE_URL = "http://localhost:2007/ribrestapp/";
    private static String DEFAULT_PU = "ribrest";

    private List scannedResourceClassesNames;
    private List scannedResourceClasses;
    private List scannedModelClassesNames;
    private List scannedModelClasses;
    private ServiceLocator serviceLocator;
    private HttpServer server;

    /*
    RELATED TO INIT RIBREST INIT PARAMETERS
     */
    String baseUrl;
    String pu;
    boolean debug;

    public Ribrest() {
        debug = false;
        baseUrl = DEFAULT_BASE_URL;
        pu = DEFAULT_PU;
    }

    public static Ribrest getInstance() {
        instance = instance != null ? instance : new Ribrest();
        return instance;
    }

    public void init() {
        RibrestLog.logForced("***INITIALIZING RIBREST FRAMEWORK***");
        setup();
    }

    /**
     * Function to setup all the Ribrest configuration. *
     */
    private void setup() {

        /* Setting up HK2 CDI */
        setupCDI();

        /*Setting up Persistence Services*/
        setupPersistence();

        /*
        Let's get and starts a server with
        1) An array of all package names who contains classes annotated with @RibrestResource
        2) A string that will be a URI to be accessible via http requests. (including its port and appname)
        3) A new instance of ResourceConfig
         */
        server = getServer(getResourcesPackagesNames(), baseUrl, new ResourceConfig());

    }

    /**
     * Method to initialize all the HK2 configuration and Services.
     */
    private void setupCDI() {
        RibrestLog.log("Setting up CDI Services by HK2 Framework");
        /*Creating new service locator*/
        serviceLocator = ServiceLocatorFactory.getInstance().create(null);
        RibrestLog.log(new StringBuilder("Service Locator created: #ID ").append(serviceLocator.getLocatorId()).toString());

        /*Populating the service locator with Dynamic added services.*/
        RibrestSLPopulator.populate(serviceLocator);
        RibrestLog.log("Service Locator populated with Ribrest core services.");
    }

    /**
     * Method that initialize expensive services related to persistence.
     */
    private void setupPersistence() {

        /*Getting the singleton service related to Persistence Unit name.
        It's necessary to dynamically create an EntityManagerFactory
        based on the initialized Persistence Unit name.*/
        PersistenceUnitWrapper puw = serviceLocator.getService(PersistenceUnitWrapper.class);
        puw.setPu(pu);

        /*Initialize the EntityManagerFactory which is expensive.
        It should be created only once by the Singleton context.*/
        serviceLocator.getService(EntityManagerFactory.class);
    }

    public void shutdown() {
        serviceLocator.getService(EntityManagerFactory.class).close();
        server.shutdown();
    }

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
     * application.
     *
     * @return Grizzly HTTP server.
     */
    private HttpServer getServer(String[] packages, String baseUrl, ResourceConfig rc) {

        /*
        CREATING RESOURCECONFIG INSTANCE
         */
        rc = new ResourceConfig().packages(packages);


        /*
        FINDING ALL @RibrestModel classes to create its respective dynamic resources.
         */
        Set resources = createResources(getModelClasses());
        rc.registerResources(resources);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at the uri
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUrl), rc, serviceLocator);
    }

    /**
     * Method to return the array of resource package names.
     *
     * @return Array of resource package names.
     */
    private String[] getResourcesPackagesNames() {
        RibrestLog.log("Trying to scan independent resources annoted with @javax.ws.rs.Path...");
        Set s = new HashSet(getResourcesClassesNames());
        RibrestLog.log(new StringBuilder("Scanned ").append(s.size()).append(" independent resource classes, they are :")
                .append(s.toString()).toString());
        String[] packageNames = getScannedPackageNamesArray(new ArrayList(s));
        RibrestLog.log(new StringBuilder("Identified the following packagenames for the scanned independent resources: ")
                .append(Arrays.toString(packageNames)).toString());
        return packageNames;
    }

    /**
     * Method that return a list of found resource classes names.
     *
     * @return List of resource classes name.
     */
    private List getResourcesClassesNames() {
        scannedResourceClassesNames = scannedResourceClassesNames != null ? scannedResourceClassesNames : getScannedClassesName(Path.class);
        return scannedResourceClassesNames;
    }

    private List getResourcesClasses() {
        scannedResourceClasses = scannedResourceClasses != null ? scannedResourceClasses : new ArrayList(getScannedClasses(getResourcesClassesNames()));
        return scannedResourceClasses;
    }

    /**
     * Method to return a list of model classes names annotaded with
     *
     * @RibrestModel annotation.
     *
     * @return A list of model classes names annotated with @RibrestModel
     * annotation.
     */
    private List getModelClassesNames() {
        scannedModelClassesNames = scannedModelClassesNames != null ? scannedModelClassesNames : getScannedClassesName(RibrestModel.class);
        return scannedModelClassesNames;
    }

    /**
     * Method that returns a list of Model Classes instance.
     *
     * @return
     */
    private List getModelClasses() {
        RibrestLog.log("Trying to scan @RibrestModel annotated classes...");
        scannedModelClasses = scannedModelClasses != null ? scannedModelClasses : new ArrayList(getScannedClasses(getModelClassesNames()));
        RibrestLog.log(new StringBuilder("Scanned ").append(scannedModelClasses.size()).append(" models, they are: ")
                .append(scannedModelClasses).toString());
        return scannedModelClasses;
    }

    private Set createResources(List classes) {
        RibrestLog.log("Creating resources based on scanned models...");
        Set resources = new HashSet();
        classes = classes != null ? classes : new ArrayList();
        for (final Object clazz : classes) {
            Resource.Builder rb = Resource.builder(RibrestUtils.getResourceName((Class)clazz));
            rb.addMethod("GET")
                    .produces(MediaType.APPLICATION_JSON).handledBy(new Inflector<ContainerRequestContext, Response>() {
                @Override
                public Response apply(ContainerRequestContext data) {
                    Facade f = new Facade((ContainerRequest) data, clazz.toString());
                    serviceLocator.inject(f);
                    serviceLocator.postConstruct(f);
                    return f.process();
                }
            });
            resources.add(rb.build());
            RibrestLog.log(new StringBuilder("Resource created at: ").append(baseUrl).append(((Class) clazz).getSimpleName().toLowerCase()).append("s").toString());
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
        classes = classes != null ? classes : new ArrayList();
        String[] packages = new String[classes.size()];
        for (int i = 0; i < classes.size(); i++) {
            packages[i] = ((String) classes.get(i)).substring(0, ((String) classes.get(i)).lastIndexOf("."));
        }

        return packages;
    }

    public boolean isDebug() {
        return debug;
    }

    public Ribrest setDebug(boolean debug) {
        this.debug = debug;
        return instance;
    }

}
