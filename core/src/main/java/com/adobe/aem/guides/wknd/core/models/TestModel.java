package com.adobe.aem.guides.wknd.core.models;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;

import javax.annotation.PostConstruct;

import com.adobe.aem.guides.wknd.core.services.DamService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import java.util.Optional;

@Model(adaptables = Resource.class, resourceType = TestModel.RESOURCE_TYPE)
public class TestModel {
    final protected static String RESOURCE_TYPE="wknd/components/test";

    @ValueMapValue
    @Default(values="No resourceType")
    private String textField;

    @OSGiService
    private DamService damService;

    private String dynamicField;

    @PostConstruct
    protected void init() {
        dynamicField = damService.getDynamicString();
    }

    public String getDynamicField(){
        return dynamicField;
    }

    public String getTextField(){
        return textField;
    }

}
