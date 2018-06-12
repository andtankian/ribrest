package br.com.andrewribeiro.ribrest.services.miner.abstracts;

import br.com.andrewribeiro.ribrest.services.miner.interfaces.Miner;
import br.com.andrewribeiro.ribrest.exceptions.RibrestDefaultException;
import br.com.andrewribeiro.ribrest.services.FlowContainer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import org.glassfish.jersey.server.ContainerRequest;
import br.com.andrewribeiro.ribrest.model.interfaces.Model;

/**
 *
 * @author Andrew Ribeiro
 */
public abstract class AbstractMiner implements Miner {

    @Inject
    protected FlowContainer fc;

    protected MultivaluedMap<String, String> form;
    protected MultivaluedMap<String, String> query;
    protected MultivaluedMap<String, String> path;
    protected MultivaluedMap<String, String> header;

    protected List accepts;

    private List ignored;

    @Override
    public void extractDataFromRequest(ContainerRequest cr) throws RibrestDefaultException {
        prepareDataObjects(cr);
    }

    protected void fillModel(Model model) throws IllegalArgumentException, IllegalAccessException {
        List<Field> l = model.getAllAttributes();
        for (Field attribute : l) {
            attribute.setAccessible(true);

            /*Firstly, let's check for String types*/
            if (attribute.getType().getSimpleName().equals("String")){
                /*Fill the equivalent name to the model attribute*/
                String value = form.getFirst(attribute.getName());
                attribute.set(model, value);
            }
        }
    }

    private void prepareDataObjects(ContainerRequest cr) {
        /*Populating form attribute with data coming from ContainerRequest*/
        Form f = cr.readEntity(Form.class);
        form = f.asMap();

        /*Populating query parameters with data coming from ContainerRequest*/
        UriInfo u = cr.getUriInfo();
        query = u.getQueryParameters();

        /*Populating path parameters with data coming from ContainerRequest*/
        path = u.getPathParameters();

        /*Populating header with data coming from ContainerRequest*/
        header = cr.getHeaders();

        /*GETTING THE ACCEPTS*/
        accepts = query != null ? query.get("accepts") : new ArrayList();
        accepts = accepts != null ? accepts : new ArrayList();
    }

    @Override
    public List extractIgnoredFields() {
        ignored = ignored != null ? ignored : new ArrayList();
        accepts = accepts != null ? accepts : new ArrayList();

        ignored.removeAll(accepts);

        return new ArrayList(ignored);
    }

}