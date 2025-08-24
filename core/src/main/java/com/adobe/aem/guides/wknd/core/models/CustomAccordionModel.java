package com.adobe.aem.guides.wknd.core.models;

import com.adobe.cq.wcm.core.components.models.Accordion;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = {Accordion.class, CustomAccordionModel.class},
        resourceType = "wknd/components/customaccordion",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CustomAccordionModel implements Accordion {
    private static final Logger logger = LoggerFactory.getLogger(CustomAccordionModel.class);

    @Self
    private SlingHttpServletRequest request;

    @Self
    private Resource resource;

    @Self
    @Via(type = ResourceSuperType.class)
    private Accordion delegate;

    @ValueMapValue
    private String enableCustomHeadline;

    @ValueMapValue
    private String customHeadline;

    // Custom methods
    public String getCustomHeadline() {
        return StringUtils.defaultString(customHeadline, null);
    }

    public String getEnableCustomHeadline() {
        return enableCustomHeadline;
    }

    public boolean getCustomHeadlineEnabled() {
        return StringUtils.isNotBlank(enableCustomHeadline) &&
                "yes".equals(enableCustomHeadline);
    }

    public boolean hasCustomHeadline() {
        return getCustomHeadlineEnabled() && StringUtils.isNotBlank(customHeadline);
    }

    // Delegate all Accordion interface methods in htl
    @Override
    public boolean isSingleExpansion() {
        return delegate != null ? delegate.isSingleExpansion() : false;
    }

    @Override
    public String[] getExpandedItems() {
        return delegate != null ? delegate.getExpandedItems() : new String[0];
    }

    @Override
    public String getHeadingElement() {
        return delegate != null ? delegate.getHeadingElement() : "h3";
    }

    @Override
    public List<ListItem> getItems() {
        return delegate != null ? delegate.getItems() : null;
    }

    @Override
    public String getId() {
        return delegate != null ? delegate.getId() : null;
    }

    @Override
    public ComponentData getData() {
        return delegate != null ? delegate.getData() : null;
    }

    @Override
    public String getAppliedCssClasses() {
        return delegate != null ? delegate.getAppliedCssClasses() : null;
    }

    @Override
    public String getExportedType() {
        return delegate != null ? delegate.getExportedType() : resource.getResourceType();
    }
}