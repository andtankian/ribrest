package br.com.andrewribeiro.ribrest.services.miner;

import br.com.andrewribeiro.ribrest.services.miner.abstracts.AbstractMiner;
import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultExceptionConstants;
import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultExceptionFactory;
import br.com.andrewribeiro.ribrest.utils.RibrestUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.jersey.server.ContainerRequest;
import br.com.andrewribeiro.ribrest.model.interfaces.Model;

/**
 *
 * @author Andrew Ribeiro
 */
public class MinerImpl extends AbstractMiner {

    @Override
    public void extract(ContainerRequest cr) throws RibrestDefaultException {
        try {
            super.extract(cr);
            Model m = (Model) fc.getModel();
            fill(m);            
        } catch (ClassCastException cce) {
            throw RibrestDefaultExceptionFactory.getRibrestDefaultException(RibrestDefaultExceptionConstants.RESOURCE_IS_NOT_IMODEL_SUBCLASS, RibrestUtils.getResourceName(fc.getModel().getClass()));
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(MinerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MinerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch(UnsupportedOperationException uoe){
            throw RibrestDefaultExceptionFactory.getRibrestDefaultException(RibrestDefaultExceptionConstants.RESOURCE_DOESNT_IMPLEMENTS_ABSTRACT_METHODS, RibrestUtils.getResourceName(fc.getModel().getClass()));
        } catch(RibrestDefaultException rde){
            throw rde;
        }
    }

}
