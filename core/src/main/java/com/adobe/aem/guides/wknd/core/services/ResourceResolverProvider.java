package com.adobe.aem.guides.wknd.core.services;

import com.adobe.aem.guides.wknd.core.utils.ResourceResolverUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;

@Component(immediate=true, service= ResourceResolverProvider.class)
public class ResourceResolverProvider {

    public ResourceResolver getServiceUserResolver(ResourceResolverFactory factory, String subService) throws LoginException {
        try {
            return ResourceResolverUtils.getServiceUserResolver(factory, subService);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
    }

}
