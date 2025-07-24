package com.adobe.aem.guides.wknd.core.listeners;

import com.adobe.aem.guides.wknd.core.constants.WkndConstants;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.EventConstants;

import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(service = ResourceChangeListener.class,
immediate = true,
property = {
        ResourceChangeListener.PATHS + "=/data/dynamicData",
        ResourceChangeListener.CHANGES + "=CHANGED",
})
public class WkndJobCreater implements ResourceChangeListener{

    private static final Logger log = LoggerFactory.getLogger(WkndJobCreater.class);

    @Reference
    private JobManager jobManager;

    @Override
    public void onChange(List<ResourceChange> changes) {
        for (ResourceChange change : changes) {
            log.info("Change detected at path: {}", change.getPath());

            Map<String, Object> jobProperties = new HashMap<>();
            jobProperties.put("path", change.getPath());
            jobProperties.put("changeType", change.getType().toString());

            // Enqueue job
            jobManager.addJob(WkndConstants.PAGE_DATA_MODIFIED, jobProperties);
        }
    }
}
