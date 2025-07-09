package com.adobe.aem.guides.wknd.core.models;

import javax.annotation.PostConstruct;

import com.adobe.aem.guides.wknd.core.services.DamService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, resourceType = CardImageModel.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CardImageModel {
    final protected static String RESOURCE_TYPE="wknd/components/card-image";

    @ValueMapValue
//    @Default(values="No resourceType")
    private String textField;

    @ValueMapValue
//    @Default(values="No resourceType")
    private String image;

    @ValueMapValue
//    @Default(values="No resourceType")
    private String imageAlt;

    @ValueMapValue
//    @Default(values="No resourceType")
    private String headline;

    public String getImage(){
        return image;
    }

    public String getImageAlt(){
        return imageAlt;
    }

    public String getHeadline(){
        return headline;
    }

    public String getTextField(){
        return textField;
    }

}
