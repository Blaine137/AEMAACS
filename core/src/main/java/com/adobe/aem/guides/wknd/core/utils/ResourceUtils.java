package com.adobe.aem.guides.wknd.core.utils;

import com.adobe.aem.guides.wknd.core.listeners.WkndJobConsumer;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceUtils {
    public static Resource getChildByName(Resource parentResource, String targetName) {
        if (parentResource == null || targetName == null) {
            return null;
        }

        for (Resource child : parentResource.getChildren()) {
            if (targetName.equals(child.getName())) {
                return child;
            }
        }

        return null; // Not found
    }

}
