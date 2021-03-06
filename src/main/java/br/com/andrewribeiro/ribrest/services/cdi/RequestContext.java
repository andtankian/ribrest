/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2015 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package br.com.andrewribeiro.ribrest.services.cdi;

import br.com.andrewribeiro.ribrest.services.cdi.annotations.RequestScope;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;


import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.Context;
import org.glassfish.hk2.api.ServiceHandle;

/**
 * This is the request scope context.  It houses all request scope
 * objects.  This is a proxiable scope, so care must be taken that
 * all objects from this scope are proxiable
 * 
 * @author jwells
 */
public class RequestContext implements Context<RequestScope> {       
    
    private final HashMap<ActiveDescriptor<?>, Object> requestScopedEntities = new HashMap<ActiveDescriptor<?>, Object>();
    
    private boolean inRequest = false;
    
    /**
     * Starts a request
     */
    public void startRequest() {
        inRequest = true;
    }
    
    /**
     * Stops a request (including properly disposing all the previous request objects)
     */
    @SuppressWarnings("unchecked")
    public void stopRequest() {
        inRequest = false;
        
        for (Map.Entry<ActiveDescriptor<?>, Object> entry : requestScopedEntities.entrySet()) {
            ActiveDescriptor<Object> ad = (ActiveDescriptor<Object>) entry.getKey();
            Object value = entry.getValue();
            
            ad.dispose(value);
        }
        
        requestScopedEntities.clear();
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.api.Context#getScope()
     */
    @Override
    public Class<? extends Annotation> getScope() {
        return RequestScope.class;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.api.Context#findOrCreate(org.glassfish.hk2.api.ActiveDescriptor, org.glassfish.hk2.api.ServiceHandle)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <U> U findOrCreate(ActiveDescriptor<U> activeDescriptor,
            ServiceHandle<?> root) {
        U retVal = (U) requestScopedEntities.get(activeDescriptor);
        if (retVal != null) {
            return retVal;
        }
        
        retVal = activeDescriptor.create(root);
        requestScopedEntities.put(activeDescriptor, retVal);
        
        return retVal;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.api.Context#find(org.glassfish.hk2.api.ActiveDescriptor)
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean containsKey(ActiveDescriptor<?> descriptor) {
        return requestScopedEntities.containsKey(descriptor);
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.api.Context#isActive()
     */
    @Override
    public boolean isActive() {
        return inRequest;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.api.Context#supportsNullCreation()
     */
    @Override
    public boolean supportsNullCreation() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.glassfish.hk2.api.Context#supportsNullCreation()
     */
    @Override
    public void shutdown() {
    }

    @Override
    public void destroyOne(ActiveDescriptor<?> descriptor) {
    }

}
