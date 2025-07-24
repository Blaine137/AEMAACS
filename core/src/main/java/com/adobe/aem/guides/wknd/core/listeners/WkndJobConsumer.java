package com.adobe.aem.guides.wknd.core.listeners;

import com.adobe.aem.guides.wknd.core.constants.WkndConstants;
import com.adobe.aem.guides.wknd.core.utils.ResourceUtils;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.*;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

@Component(
        service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=" + WkndConstants.PAGE_DATA_MODIFIED
        }
)
public class WkndJobConsumer implements JobConsumer{

        private static final Logger log = LoggerFactory.getLogger(WkndJobConsumer.class);

        private PageManager pageManager;


        @Reference
        private ResourceResolverFactory resolverFactory;
        

        @Override
        public JobResult process(Job job) {

                String path = (String) job.getProperty("path");

                if (path == null || path.isEmpty()) {
                        log.warn("Job Consumer: No path provided in job properties.");
                        return JobResult.CANCEL;
                }

                try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(
                        Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, WkndConstants.DATA_WRITE_SERVICE_USER))) {

                        Resource resource = resolver.getResource(path);

                        if (resource != null) {
                                log.info("Job Consumer: Successfully found resource at path: {}", resource.getPath());
                                ValueMap resourceValueMap = resource.getValueMap();
                                if (resourceValueMap != null) {
                                    String dynamicFieldValue = resourceValueMap.get("data").toString();
                                    if (StringUtils.isNotBlank(dynamicFieldValue)){
                                        try{
                                                Resource pageResource = resolver.getResource(WkndConstants.BLAINES_CUSTOM_PAGE_PATH);

                                                if (pageResource != null) {
                                                        Resource pageData = ResourceUtils.getChildByName(pageResource, "pageData");
                                                        if (pageData == null){
                                                                log.info("Job Consumer: page data on page resource is null!");
                                                                return JobResult.FAILED;
                                                        }else{
                                                                log.info("Job Consumer: pageData resource is not null!!!!");
                                                                ModifiableValueMap editableMap = pageData.adaptTo(ModifiableValueMap.class);
                                                                if (editableMap != null) {
                                                                        editableMap.put("dataFromJob", dynamicFieldValue);
                                                                        resolver.commit();
                                                                }else{
                                                                        log.info("Job Consumer: Error while adapting to ModifiableValueMap!");
                                                                }
                                                        }
                                                }else{
                                                        log.info("Job Consumer: page resource is null!");
                                                        return JobResult.FAILED;
                                                }
                                        }catch (PersistenceException e){
                                                log.info("Job Consumer: Error while committing changes!");
                                        }
                                    }else{
                                            log.info("Job Consumer: dynamic field value is null!");
                                            return JobResult.FAILED;
                                    }
                                }
                        } else {
                                log.warn("Resource not found at path: {}", path);
                                return JobResult.FAILED;
                        }

                } catch (LoginException e) {
                        log.error("Job Consumer: Unable to get Service ResourceResolver", e);
                        return JobResult.FAILED;
                }

                return JobResult.OK;
        }

}
