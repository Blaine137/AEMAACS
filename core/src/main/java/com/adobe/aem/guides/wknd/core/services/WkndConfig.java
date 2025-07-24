package com.adobe.aem.guides.wknd.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "Wknd Scheduler Configuration",
        description = "Configuration to set a dynamic value for WkndScheduler"
)
public @interface WkndConfig {

    @AttributeDefinition(
            name = "Dynamic Value From Config",
            description = "Enter Dynamic Value Here",
            type = AttributeType.STRING
    )
    String getDynamicValue() default "Value coming from job!";

}
