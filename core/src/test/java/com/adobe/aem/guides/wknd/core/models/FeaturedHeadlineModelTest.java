package com.adobe.aem.guides.wknd.core.models;

import com.adobe.cq.wcm.core.components.models.Title;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;


@ExtendWith(AemContextExtension.class)
class FeaturedHeadlineModelTest {

    private final AemContext context = new AemContext();

    private static final String RESOURCE_PATH = "/content/component";
    private static final String COMPONENT_RESOURCE_TYPE = "wknd/components/featuredheadline";
    private static final String SUBHEADLINE = "Test subheadline";

    private Resource resource;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(FeaturedHeadlineModel.class);

        Title mockTitle = mock(Title.class);

        context.registerAdapter(SlingHttpServletRequest.class, Title.class, mockTitle);
    }

    @Test
    void testSubHeadlineIsLoaded() {
        resource = context.create().resource(RESOURCE_PATH,
                "sling:resourceType", COMPONENT_RESOURCE_TYPE,
                "subHeadline", SUBHEADLINE);
        context.currentResource(resource);

        FeaturedHeadlineModel model = context.request().adaptTo(FeaturedHeadlineModel.class);

        assertEquals(SUBHEADLINE, model.getSubHeadline());
    }

    @Test
    void testSubHeadlineIsNullWhenMissing() {
        context.create().resource("/content/missing", "sling:resourceType", COMPONENT_RESOURCE_TYPE);
        context.currentResource("/content/missing");

        FeaturedHeadlineModel model = context.request().adaptTo(FeaturedHeadlineModel.class);

        assertNull(model.getSubHeadline());
    }
}
