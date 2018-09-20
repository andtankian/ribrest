package br.com.andrewribeiro.ribrest.services.miner;

import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultExceptionConstants;
import br.com.andrewribeiro.ribrest.core.exceptions.RibrestDefaultExceptionFactory;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.jersey.server.ContainerRequest;
import br.com.andrewribeiro.ribrest.core.model.Model;

/**
 *
 * @author Andrew Ribeiro
 */
public class MinerImpl extends AbstractMiner {

    @Override
    public void extractDataFromRequest(ContainerRequest cr) throws RibrestDefaultException {
        try {
            super.extractDataFromRequest(cr);
            Model m = (Model) flowContainer.getModel();
            fillModel(m);            
        } catch (ClassCastException cce) {
            throw RibrestDefaultExceptionFactory.getRibrestDefaultException(RibrestDefaultExceptionConstants.RESOURCE_IS_NOT_IMODEL_SUBCLASS, RibrestUtils.getResourceName(flowContainer.getModel().getClass()));
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(MinerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MinerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch(UnsupportedOperationException uoe){
            throw RibrestDefaultExceptionFactory.getRibrestDefaultException(RibrestDefaultExceptionConstants.RESOURCE_DOESNT_IMPLEMENTS_ABSTRACT_METHODS, RibrestUtils.getResourceName(flowContainer.getModel().getClass()));
        } catch(RibrestDefaultException rde){
            throw rde;
        } catch (InstantiationException ex) {
            Logger.getLogger(MinerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
