package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.aem.guides.wknd.core.utils.ResourceResolverUtils;
import com.day.cq.commons.jcr.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Test Path Servlet",
                "sling.servlet.paths=/bin/import", // Define a path-based servlet
                "sling.servlet.methods=GET"
        })
public class DataImportServlet extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestPathServlet.class);
    private static final String SERVICE_USER_NAME = "data-import-service-user";

    @Reference
    private ResourceResolverFactory resourceResolverFactory;
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        // Example logic: fetching query parameters
        LOGGER.info("servlet fired!");
        String output = "Congratulations, your data has been imported!";

        String nodeName = request.getParameter("nodeName");
        String customProperty1 = request.getParameter("property1");
        String customProperty2 = request.getParameter("property2");


        try (ResourceResolver resourceResolver = ResourceResolverUtils.getServiceUserResolver(resourceResolverFactory, SERVICE_USER_NAME)) {
            Resource parentResource = resourceResolver.getResource("/data");
            if (parentResource == null) {
                LOGGER.info("parent resource is not null!");
                response.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"Parent path '/data' does not exist\"}");
                return;
            }else{
                Node parentNode = parentResource.adaptTo(Node.class);
                if (parentNode != null) {
                    LOGGER.info("parent node (/data) successfully adapted to a Node class!");
                    // Create the new node
                    Node newNode = parentNode.addNode(nodeName, JcrConstants.NT_UNSTRUCTURED);
                    newNode.setProperty("customProperty1", customProperty1);
                    newNode.setProperty("customProperty2", customProperty2);

                    // Save changes
                    resourceResolver.commit();

                    response.setStatus(SlingHttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"success\":\"Node created successfully\"}");
                } else {
                    response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("{\"error\":\"Failed to adapt parent resource to Node\"}");
                }
            }

        } catch (Exception e) {
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }

    }

    private String escapeJson(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("\"", "\\\"")
                .replace("\\", "\\\\")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
