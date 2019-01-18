package br.com.andrewribeiro.ribrest;

import br.com.andrewribeiro.ribrest.core.applisteners.AppListener;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.server.ResourceConfig;

import br.com.andrewribeiro.ribrest.logs.RibrestLog;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import org.glassfish.hk2.api.ServiceLocator;

public class Ribrest {

    private static Ribrest instance;

    private static final String DEFAULT_APP_BASE_URL = "http://localhost:2007/";
    private static final String DEFAULT_APP_NAME = "ribrest/";
    private static final String DEFAULT_PERSISTENCE_UNIT = "ribrest";
    private final static String DEFAULT_STATIC_PATH = "static";
    private final static String DEFAULT_STATIC_SRC = "statics";

    private ServiceLocator serviceLocator;
    private HttpServer server;

    /*
    RELATED TO INIT RIBREST INIT PARAMETERS
     */
    private String appBaseUrl;
    private String appName;
    private String persistenceUnitName;
    private String staticPath;
    private String staticSrc;
    private String completeAppUrl;
    private boolean debug;
    private RibrestConfiguratorImpl ribrestConfigurator;
    private ResourceConfig resourceConfig;
    private List<AppListener> appListeners;

    public Ribrest() {
        debug = false;
        appBaseUrl = DEFAULT_APP_BASE_URL;
        appName = DEFAULT_APP_NAME;
        persistenceUnitName = DEFAULT_PERSISTENCE_UNIT;
        staticPath = DEFAULT_STATIC_PATH;
        staticSrc = DEFAULT_STATIC_SRC;
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
        initAppListeners();
    }

    /**
     * Function to setup all the Ribrest configuration. *
     */
    private void setup() {

        ribrestConfigurator.setRibrestInstance(instance);

        ribrestConfigurator.setupHK2CDI();

        ribrestConfigurator.setupPersistence();

        server = ribrestConfigurator.getRunningGrizzlyServer();

        ribrestConfigurator.setupStaticServer(server);
        
        ribrestConfigurator.setupAppListeners();

        RibrestLog.logForced(new StringBuilder("Application is up and running at: ")
                .append(getCompleteAppUrl()).toString());

        ribrestConfigurator.setupShutdownHook();

    }

    public void shutdown() {
        RibrestLog.logForced("Bye :)");
        System.exit(0);
    }

    private void initAppListeners() {
        this.appListeners
                .forEach(appListener->{
                    appListener.setRibrestInstance(this);
                    appListener.init();
                });
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

    public Ribrest persistenceUnitName(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName != null && !persistenceUnitName.isEmpty() ? persistenceUnitName : this.persistenceUnitName;
        return instance;
    }

    public Ribrest staticPath(String staticPath) {
        this.staticPath = staticPath != null && !staticPath.isEmpty() ? staticPath : this.staticPath;
        this.staticPath = this.staticPath.startsWith("/") ? this.staticPath.substring(1) : this.staticPath;
        return instance;
    }

    public Ribrest staticSrc(String staticSrc) {
        this.staticSrc = staticSrc != null && !staticSrc.isEmpty() ? staticSrc : this.staticSrc;
        return instance;
    }

    public String getAppBaseUrl() {
        return appBaseUrl;
    }

    public String getAppName() {
        return appName;
    }

    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    public String getCompleteAppUrl() {
        completeAppUrl = completeAppUrl == null ? getAppBaseUrl() + getAppName() : completeAppUrl;
        return completeAppUrl;
    }

    public String getCompleteStaticServerUrl() {
        return this.appBaseUrl + this.staticPath;
    }

    public String getStaticPath() {
        return this.staticPath;
    }

    public String getStaticSrc() {
        return this.staticSrc;
    }

    void setAppListeners(List<AppListener> appListeners) {
        this.appListeners = appListeners;
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
