package br.com.andrewribeiro.ribrest;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.server.ResourceConfig;

import br.com.andrewribeiro.ribrest.logs.RibrestLog;
import javax.persistence.EntityManagerFactory;
import org.glassfish.hk2.api.ServiceLocator;

public class Ribrest {

    private static Ribrest instance;

    private static String DEFAULT_BASE_URL = "http://localhost:2007/ribrestapp/";
    private static String DEFAULT_PU = "ribrest";

    private ServiceLocator serviceLocator;
    private HttpServer server;

    /*
    RELATED TO INIT RIBREST INIT PARAMETERS
     */
    private String baseUrl;
    private String persistenceUnitName;
    private boolean debug;
    private RibrestConfiguratorImpl ribrestConfigurator;
    private ResourceConfig resourceConfig;

    public Ribrest() {
        debug = false;
        baseUrl = DEFAULT_BASE_URL;
        persistenceUnitName = DEFAULT_PU;
        ribrestConfigurator = new RibrestConfiguratorImpl();
        resourceConfig = new ResourceConfig();
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

        ribrestConfigurator.setRibrestInstance(instance);

        ribrestConfigurator.setupHK2CDI();

        ribrestConfigurator.setupPersistence();

        server = ribrestConfigurator.getRunningGrizzlyServer();

        RibrestLog.log(new StringBuilder("Application is up and running at: ")
                .append(baseUrl).toString());

    }

    public void shutdown() {
        serviceLocator.getService(EntityManagerFactory.class).close();
        server.shutdown();
    }

    public boolean isDebug() {
        return debug;
    }

    public Ribrest debug(boolean debug) {
        this.debug = debug;
        return instance;
    }

    public Ribrest baseUrl(String baseUrl) {
        this.baseUrl = baseUrl != null && !baseUrl.isEmpty() ? baseUrl : this.baseUrl;
        return instance;
    }

    String getBaseUrl() {
        return baseUrl;
    }

    public Ribrest persistenceUnitName(String pesistenceUnitName) {
        this.persistenceUnitName = pesistenceUnitName != null && !persistenceUnitName.isEmpty() ? persistenceUnitName : this.persistenceUnitName;
        return instance;
    }

    String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    public Ribrest resourceConfig(ResourceConfig resourceConfig) {
        this.resourceConfig = resourceConfig;
        return instance;
    }

    ResourceConfig getResourceConfig() {
        return resourceConfig;
    }

    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }

    void setServiceLocator(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }
}
