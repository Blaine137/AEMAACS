package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.aem.guides.wknd.core.utils.ResourceResolverUtils;
import com.day.cq.commons.jcr.JcrConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
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
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Test Path Servlet",
                "sling.servlet.paths=/bin/import", // Define a path-based servlet
                "sling.servlet.methods=GET"
        })
public class DataImportServlet extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestPathServlet.class);
    private static final String SERVICE_USER_NAME = "data-import-service-user";
    private static final String BASE_PATH = "/data";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private boolean isUpdate = false;
    @Reference
    private ResourceResolverFactory resourceResolverFactory;
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        InputStream inputStream = request.getInputStream();
        String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        JsonNode jsonArray = objectMapper.readTree(jsonString);

        if (jsonArray.isArray()) {
            LOGGER.info("is an array!");

            try (ResourceResolver resourceResolver = ResourceResolverUtils.getServiceUserResolver(resourceResolverFactory, SERVICE_USER_NAME)) {
                Iterator<JsonNode> iterator = jsonArray.iterator();
                while (iterator.hasNext()) {
                    JsonNode jsonNode = iterator.next();
                    String nodeName = jsonNode.has("nodeName") ? jsonNode.get("nodeName").asText() : null;
                    String location = jsonNode.has("location") ? jsonNode.get("location").asText() : null;
                    String pagePath = jsonNode.has("pagePath") ? jsonNode.get("pagePath").asText() : null;
                    LOGGER.info("nodeName is: {}", nodeName);
                    if(location != null){
                        String nodePath = BASE_PATH + "/" + nodeName;
                        Resource currentResource = resourceResolver.getResource(nodePath);
                        if(currentResource != null){
                            LOGGER.info("current resource is not null: {}", nodePath);
                            Node currentNode = currentResource.adaptTo(Node.class);
                            if(currentNode != null){
                                isUpdate = true;
                                currentNode.setProperty("location", location);
                                currentNode.setProperty("pagePath", pagePath);
                            }
                        }else{
                            Resource parentResource = resourceResolver.getResource(BASE_PATH);
                            if(parentResource != null){
                                Node parentNode = parentResource.adaptTo(Node.class);
                                if(parentNode != null){
                                    Node newNode = parentNode.addNode(nodeName, JcrConstants.NT_UNSTRUCTURED);
                                    newNode.setProperty("location", location);
                                    newNode.setProperty("pagePath", pagePath);

                                    // Save changes
                                    resourceResolver.commit();
                                }
                            }
                        }
                    }
                }

                response.setStatus(SlingHttpServletResponse.SC_OK);
                response.setContentType("application/json");
                if(isUpdate){
                    response.getWriter().write("{\"success\":\"Node updated successfully\"}");
                }else{
                    response.getWriter().write("{\"success\":\"Node created successfully\"}");
                }
            }catch(Exception e){
                LOGGER.info("error while getting or using resource resolver: {}", e.getMessage());
            }


            LOGGER.info("after reading array");
        }

//        try (ResourceResolver resourceResolver = ResourceResolverUtils.getServiceUserResolver(resourceResolverFactory, SERVICE_USER_NAME)) {
//            Resource parentResource = resourceResolver.getResource("/data");
//            if (parentResource == null) {
//                LOGGER.info("parent resource is not null!");
//                response.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
//                response.getWriter().write("{\"error\":\"Parent path '/data' does not exist\"}");
//            }else{
//                Node parentNode = parentResource.adaptTo(Node.class);
//
//                // check if a node already exists
//                Resource baseResource = resourceResolver.getResource("/data/" + nodeName);
//                if (baseResource != null){
//                    Node existingNode = baseResource.adaptTo(Node.class);
//                    if (existingNode != null) {
//                        LOGGER.info("parent node (/data) successfully adapted to a Node class!");
//                        existingNode.setProperty("customProperty1", customProperty1);
//                        existingNode.setProperty("customProperty2", customProperty2);
//
//                        // Save changes
//                        resourceResolver.commit();
//
//                        response.setStatus(SlingHttpServletResponse.SC_OK);
//                        response.setContentType("application/json");
//                        response.getWriter().write("{\"success\":\"Node updated successfully\"}");
//                    }
//                }else{
//                    if (parentNode != null){
//                        // Create the new node
//                        Node newNode = parentNode.addNode(nodeName, JcrConstants.NT_UNSTRUCTURED);
//                        newNode.setProperty("customProperty1", customProperty1);
//                        newNode.setProperty("customProperty2", customProperty2);
//
//                        // Save changes
//                        resourceResolver.commit();
//
//                        response.setStatus(SlingHttpServletResponse.SC_OK);
//                        response.setContentType("application/json");
//                        response.getWriter().write("{\"success\":\"Node created successfully\"}");
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
//        }

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
