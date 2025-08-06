package com.adobe.aem.guides.wknd.core.models;

import com.adobe.aem.guides.wknd.core.servlets.TestPathServlet;
import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.Title;
import com.drew.lang.annotations.Nullable;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = Title.class,
        resourceType = "wknd/components/featuredheadline",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FeaturedHeadlineModel implements Title{

    private static final Logger LOGGER = LoggerFactory.getLogger(FeaturedHeadlineModel.class);

    @Self
    @Via(type = ResourceSuperType.class)
    private Title delegate;

    @ValueMapValue(name = "subHeadline")
    private String subHeadline;

    @PostConstruct
    protected void init() {
        LOGGER.info("Subheadline loaded: {}", subHeadline);
    }

    public String getSubHeadline() {
        return subHeadline;
    }

    @Override
    public String getText() {
        return delegate.getText();
    }

    @Override
    public String getType() {
        return delegate.getType();
    }

    @Override
    public @Nullable Link getLink() {
        return delegate.getLink();
    }

    @Override
    public boolean isLinkDisabled() {
        return delegate.isLinkDisabled();
    }


}
