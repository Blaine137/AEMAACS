package com.adobe.aem.guides.wknd.core.listeners;

import com.adobe.aem.guides.wknd.core.constants.WkndConstants;
import com.adobe.aem.guides.wknd.core.listeners.WkndJobConsumer;
import com.adobe.aem.guides.wknd.core.utils.ResourceUtils;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.*;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.testing.resourceresolver.MockResourceResolverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith({MockitoExtension.class})
@ExtendWith(AemContextExtension.class)
class WkndJobConsumerTest {

    private static final Logger log = LoggerFactory.getLogger(WkndJobConsumerTest.class);

    final AemContext context = new AemContext();

    ResourceResolverFactory mockFactory;

    private WkndJobConsumer jobConsumer;
    private Job job;

    @Mock
    private ResourceResolverFactory resolverFactory;


    @BeforeEach
    void setUp() throws LoginException, PersistenceException {

        // Mock the factory
        mockFactory = mock(ResourceResolverFactory.class);

        // Use context.resourceResolver() for all service user calls
        when(mockFactory.getServiceResourceResolver(anyMap())).thenReturn(context.resourceResolver());

        // Register it in the test context
        context.registerService(ResourceResolverFactory.class, mockFactory);

        Map<String, Object> jobDataProps = new HashMap<>();
        jobDataProps.put("data", "Value coming from job!");
        context.create().resource("/data/dynamicData", jobDataProps);

        // Create the target pageData node
        Map<String, Object> pageProps = new HashMap<>();
        pageProps.put("jcr:primaryType", "cq:Page");
        context.create().resource(WkndConstants.BLAINES_CUSTOM_PAGE_PATH, pageProps);

        Map<String, Object> pageDataProps = new HashMap<>();
        pageDataProps.put("jcr:primaryType", "nt:unstructured");
        context.create().resource(WkndConstants.BLAINES_CUSTOM_PAGE_PATH + "/pageData", pageDataProps);

        //commit changes after resources have been created
        context.resourceResolver().commit();

        jobConsumer = context.registerInjectActivateService(new WkndJobConsumer());

        // Create a mock job that returns the resource path
        job = mock(Job.class);
        when(job.getProperty("path")).thenReturn("/data/dynamicData");
    }

    @Test
    void testSuccessfulJobProcessing() {
        log.info("Resource exists at path? {}", context.resourceResolver().getResource(WkndConstants.BLAINES_CUSTOM_PAGE_PATH) != null);
        System.out.println("DEBUG - test pageData exists: " + context.resourceResolver().getResource(WkndConstants.BLAINES_CUSTOM_PAGE_PATH) != null);
        JobConsumer.JobResult result = jobConsumer.process(job);
        log.info("Testing result is {}", result);
        // Validate the result is OK
        assertEquals(JobConsumer.JobResult.OK, result);
    }

    @Test
    void testJobProcessingWithNoPath() {
        when(job.getProperty("path")).thenReturn(null); // Simulate missing path
        JobConsumer.JobResult result = jobConsumer.process(job);
        assertEquals(JobConsumer.JobResult.CANCEL, result);
    }

}
