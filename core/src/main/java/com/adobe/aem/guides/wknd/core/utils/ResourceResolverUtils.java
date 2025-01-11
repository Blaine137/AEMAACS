package com.adobe.aem.guides.wknd.core.utils;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

import java.util.HashMap;
import java.util.Map;

public class ResourceResolverUtils {

    /**
     * Gets a resource resolver for a specific service user.
     *
     * @param resourceResolverFactory the ResourceResolverFactory instance.
     * @param subServiceName the sub-service name configured in OSGi.
     * @return a ResourceResolver for the service user.
     * @throws LoginException if the service resolver cannot be obtained.
     */
    public static ResourceResolver getServiceUserResolver(ResourceResolverFactory resourceResolverFactory, String subServiceName) throws LoginException {
        Map<String, Object> authInfo = new HashMap<>();
        authInfo.put(ResourceResolverFactory.SUBSERVICE, subServiceName);
        return resourceResolverFactory.getServiceResourceResolver(authInfo);
    }

}
