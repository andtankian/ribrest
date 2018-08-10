package br.com.andrewribeiro.ribrest;

import br.com.andrewribeiro.ribrest.logs.RibrestLog;
import br.com.andrewribeiro.ribrest.services.cdi.hk2.RibrestSLPopulator;
import br.com.andrewribeiro.ribrest.services.orm.PersistenceUnitWrapper;
import java.net.URI;
import javax.persistence.EntityManagerFactory;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author Andrew Ribeiro
 */
class RibrestConfiguratorImpl extends AbstractRibrestConfigurator {

    private final RibrestScanner ribrestScanner = new RibrestScanner();
    private final RibrestResourceManager ribrestResourceManager = new RibrestResourceManager();

    @Override
    public void setRibrestInstance(Ribrest ribrest) {
        super.setRibrestInstance(ribrest);
        ribrestResourceManager.setRibrestInstance(ribrest);
    }

    void setupHK2CDI() {
        RibrestLog.log("Setting up CDI Services by HK2 Framework");
        ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create(null);
        RibrestLog.log(new StringBuilder("Service Locator created: #ID ").append(serviceLocator.getLocatorId()).toString());
        RibrestSLPopulator.populate(serviceLocator);
        RibrestLog.log("Service Locator populated with Ribrest core services.");
        ribrest.setServiceLocator(serviceLocator);
    }

    void setupPersistence() {
        ServiceLocator serviceLocator = ribrest.getServiceLocator();
        RibrestLog.log("Setting up persistence unit");
        PersistenceUnitWrapper persistenceUnitWrapper = serviceLocator.getService(PersistenceUnitWrapper.class);
        persistenceUnitWrapper.setPersistenceUnitName(ribrest.getPersistenceUnitName());
        RibrestLog.log(new StringBuilder("Initializing persistence unit named \"").append(persistenceUnitWrapper.getPersistenceUnitName())
                .append("\"").toString());
        serviceLocator.getService(EntityManagerFactory.class);
    }

    HttpServer getRunningGrizzlyServer() {
        ResourceConfig resourceConfig = ribrest.getResourceConfig();
        resourceConfig.packages(ribrestScanner.getAllPackagesToBeRegistered());
        resourceConfig.registerResources(ribrestResourceManager.getProgrammaticallyResources(ribrestScanner.getModelClassesInstances()));
        return initServer();
    }
    
    void setupShutdownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(()->{ribrest.stop();}));
        RibrestLog.logForced("To shutdown Ribrest, type CTRL+C...");
    }

    private HttpServer initServer() {
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(ribrest.getCompleteAppUrl()),
                ribrest.getResourceConfig(),
                ribrest.getServiceLocator());
    }

}
