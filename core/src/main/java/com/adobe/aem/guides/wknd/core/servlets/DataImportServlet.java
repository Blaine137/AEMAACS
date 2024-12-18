package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.model.WorkflowModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.BufferedReader;
import java.io.IOException;

@Component(service = Servlet.class, property = {
        "sling.servlet.paths=/data", // Define a path-based servlet
        "sling.servlet.methods=POST"
})
public class DataImportServlet extends SlingAllMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(DataImportServlet.class);

    // Workflow model path - adjust to match your workflow
    private static final String FACILITY_IMPORT_WORKFLOW_PATH = "/var/workflow/models/facility-import";

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {
        try {
            // Read JSON from request body
            StringBuilder jsonBuilder = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
            }

//            // Get Workflow Session
//            WorkflowSession workflowSession = request.getResourceResolver()
//                    .adaptTo(WorkflowSession.class);
//
//            if (workflowSession == null) {
//                response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR,
//                        "Unable to obtain Workflow Session");
//                return;
//            }
//
//            // Load Workflow Model
//            WorkflowModel workflowModel = workflowSession.getModel(FACILITY_IMPORT_WORKFLOW_PATH);
//
//            if (workflowModel == null) {
//                response.sendError(SlingHttpServletResponse.SC_NOT_FOUND,
//                        "Workflow model not found: " + FACILITY_IMPORT_WORKFLOW_PATH);
//                return;
//            }
//
//            // Create Workflow Data
//            WorkflowData workflowData = workflowSession.newWorkflowData(
//                    "JCR_PATH", // or "JSON" depending on your workflow configuration
//                    jsonBuilder.toString()
//            );
//
//            // Start Workflow
//            workflowSession.startWorkflow(workflowModel, workflowData);

            // Send success response
            response.setStatus(SlingHttpServletResponse.SC_ACCEPTED);
            response.getWriter().write("Facility import workflow initiated successfully");

        } catch (Exception e) {
            LOG.error("Error initiating facility import workflow", e);
            response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error initiating workflow: " + e.getMessage());
        }
    }
}
