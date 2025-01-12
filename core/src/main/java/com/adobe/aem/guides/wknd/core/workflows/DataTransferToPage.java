package com.adobe.aem.guides.wknd.core.workflows;

import com.adobe.aem.guides.wknd.core.utils.ResourceResolverUtils;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.commons.jcr.JcrConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import java.util.Objects;


@Component(service = WorkflowProcess.class,
        property = {"process.label=Practice Custom Workflow Process"})
public class DataTransferToPage implements WorkflowProcess {

    protected final Logger logger = LoggerFactory.getLogger(DataTransferToPage.class);
    private static final String SERVICE_USER_NAME = "data-import-service-user";

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public void execute(WorkItem workItem, WorkflowSession wfSession, MetaDataMap metaDataMap) throws WorkflowException {
        String payloadType = workItem.getWorkflowData().getPayloadType();
        logger.info("Process step started, payloadType: {}", payloadType);

        if ("JCR_PATH".equals(payloadType)) {
            String path = workItem.getWorkflowData().getPayload().toString();
            logger.info("Payload path: {}", path);

            try (ResourceResolver resourceResolver = ResourceResolverUtils.getServiceUserResolver(resourceResolverFactory, SERVICE_USER_NAME)) {
                Resource payloadResource = resourceResolver.getResource(path);
                if (payloadResource != null) {
                    ValueMap properties = payloadResource.getValueMap();
                    String location = properties.get("location", String.class);
                    String pagePath = properties.get("pagePath", String.class);

                    if (location != null && pagePath != null) {
                        Resource pageResource = resourceResolver.getResource(pagePath);
                        if (pageResource != null) {
                            Node pageNode = pageResource.adaptTo(Node.class);
                            if (pageNode != null && !pageNode.hasNode("pageData")) {
                                Node newNode = pageNode.addNode("pageData", JcrConstants.NT_UNSTRUCTURED);
                                newNode.setProperty("location", location);

                                // Commit changes
                                resourceResolver.commit();
                                logger.info("Node added successfully at: {}", pagePath);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Error in workflow process step: {}", e.getMessage(), e);
                throw new WorkflowException("Error processing workflow step", e);
            }
        } else {
            logger.warn("Unsupported payload type: {}", payloadType);
        }

        logger.info("Process step completed successfully.");
    }
}

