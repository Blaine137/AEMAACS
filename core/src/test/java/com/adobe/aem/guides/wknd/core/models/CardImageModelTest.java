package com.adobe.aem.guides.wknd.core.models;

import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class CardImageModelTest {

    private final AemContext context = new AemContext();

    private CardImageModel cardImageModel;

    private Page page;
    private Resource resource;

    @BeforeEach
    void setUp() {
        page = context.create().page("/content/BlainesHomePage");
    }

    @Test
    void testModelWithAllFieldsPopulated() {
        resource = context.create().resource("/content/card-image",
                "sling:resourceType", "wknd/components/card-image",
                "headline", "Example Headline",
                "image", "/content/dam/wknd/english/dispatcher-error.png",
                "imageAlt", "Sample Alt Text",
                "textField", "Example body text"
        );
        cardImageModel = resource.adaptTo(CardImageModel.class);
        assertNotNull(cardImageModel);

        assertEquals("/content/dam/wknd/english/dispatcher-error.png", cardImageModel.getImage());
        assertEquals("Sample Alt Text", cardImageModel.getImageAlt());
        assertEquals("Example Headline", cardImageModel.getHeadline());
        assertEquals("Example body text", cardImageModel.getTextField());
    }

    @Test
    void testModelWithNoFieldsSet() {
        resource = context.create().resource(page, "card-image",
                "sling:resourceType", "wknd/components/card-image");
        cardImageModel = resource.adaptTo(CardImageModel.class);
        assertNotNull(cardImageModel);

        assertNull(cardImageModel.getImage(), "Image should be null when not set");
        assertNull(cardImageModel.getImageAlt(), "ImageAlt should be null when not set");
        assertNull(cardImageModel.getHeadline(), "Headline should be null when not set");
        assertNull(cardImageModel.getTextField(), "Text field should be null when not set");
    }
}