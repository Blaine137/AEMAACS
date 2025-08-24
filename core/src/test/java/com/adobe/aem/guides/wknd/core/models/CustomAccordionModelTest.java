package com.adobe.aem.guides.wknd.core.models;

import com.adobe.cq.wcm.core.components.models.Accordion;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class CustomAccordionModelTest {

    private final AemContext context = new AemContext();


    @BeforeEach
    void setUp() {
        // load your component definition
        context.addModelsForPackage("com.adobe.aem.guides.wknd.core.models");
    }

    @Test
    void testHeadlineIsInjected() {
        // Arrange: Create a resource simulating your custom accordion component
        context.create().resource("/content/testAccordion",
                "sling:resourceType", "wknd/components/customaccordion",
                "enableCustomHeadline", "yes",
                "customHeadline", "My Custom Accordion Headline");

        // Create a mock request with the resource
        SlingHttpServletRequest request = context.request();

        // Bind resource to the request
        context.currentResource("/content/testAccordion");

        // Act: Adapt from request instead of resource
        Accordion model = request.adaptTo(Accordion.class);

        assertNotNull(model);
        assertTrue(model instanceof CustomAccordionModel);

        CustomAccordionModel custom = (CustomAccordionModel) model;

        // Assert: enable custom headline is yes
        assertEquals("yes", custom.getEnableCustomHeadline());

        // Assert: verify headline is injected correctly
        assertEquals("My Custom Accordion Headline", custom.getCustomHeadline());
    }

    @Test
    void testHeadlineIsNullIfNotSet() {
        // Arrange: resource without headline
        context.create().resource("/content/testNullHeadlineAccordion",
                "sling:resourceType", "wknd/components/customaccordion",
                "enableCustomHeadline", "no");

        // Create a mock request with the resource
        SlingHttpServletRequest request = context.request();

        // Bind resource to the request
        context.currentResource("/content/testNullHeadlineAccordion");

        // Act: Adapt from request instead of resource
        Accordion model = request.adaptTo(Accordion.class);

        assertNotNull(model);
        CustomAccordionModel custom = (CustomAccordionModel) model;

        // Assert: enable custom headline is no
        assertEquals("no", custom.getEnableCustomHeadline());

        // Assert: headline defaults to null
        assertNull(custom.getCustomHeadline());
    }
}
