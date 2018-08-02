package br.com.andrewribeiro.ribrest;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.server.ResourceConfig;

import br.com.andrewribeiro.ribrest.logs.RibrestLog;
import javax.persistence.EntityManagerFactory;
import org.glassfish.hk2.api.ServiceLocator;

public class Ribrest {

    private static Ribrest instance;

    private static String DEFAULT_APP_BASE_URL = "http://localhost:2007/";
    private static String DEFAULT_APP_NAME = "ribrest/";
    private static String DEFAULT_PERSISTENCE_UNIT = "ribrest";

    private ServiceLocator serviceLocator;
    private HttpServer server;

    /*
    RELATED TO INIT RIBREST INIT PARAMETERS
     */
    private String appBaseUrl;
    private String appName;
    private String persistenceUnitName;
    private String completeAppUrl;
    private boolean debug;
    private RibrestConfiguratorImpl ribrestConfigurator;
    private ResourceConfig resourceConfig;

    public Ribrest() {
        debug = false;
        appBaseUrl = DEFAULT_APP_BASE_URL;
        appName = DEFAULT_APP_NAME;
        persistenceUnitName = DEFAULT_PERSISTENCE_UNIT;
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

        RibrestLog.logForced(new StringBuilder("Application is up and running at: ")
                .append(getCompleteAppUrl()).toString());
        
        ribrestConfigurator.setupShutdownHook();

    }
    
    public void shutdown() {
        RibrestLog.logForced("Bye :)");
        System.exit(0);
    }

    void stop() {
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

    public Ribrest appBaseUrl(String appBaseUrl) {
        this.appBaseUrl = appBaseUrl != null && !appBaseUrl.isEmpty() ? appBaseUrl : this.appBaseUrl;
        this.appBaseUrl = !this.appBaseUrl.endsWith("/") ? this.appBaseUrl.concat("/") : this.appBaseUrl;
        return instance;
    }

    public Ribrest appName(String appName) {
        this.appName = appName != null && !appName.isEmpty() ? appName : this.appName;
        this.appName = !this.appName.endsWith("/") ? this.appName.concat("/") : this.appName;
        return instance;
    }

    String getAppBaseUrl() {
        return appBaseUrl;
    }

    String getAppName() {
        return appName;
    }

    public Ribrest persistenceUnitName(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName != null && !persistenceUnitName.isEmpty() ? persistenceUnitName : this.persistenceUnitName;
        return instance;
    }

    String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    String getCompleteAppUrl() {
        completeAppUrl = completeAppUrl == null ? getAppBaseUrl() + getAppName() : completeAppUrl;
        return completeAppUrl;
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
