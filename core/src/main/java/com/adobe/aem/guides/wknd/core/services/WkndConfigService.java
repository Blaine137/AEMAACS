package com.adobe.aem.guides.wknd.core.services;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = WkndConfigService.class)
@Designate(ocd = WkndConfig.class)
public class WkndConfigService {

    private String configValue;

    @Activate
    public void start(WkndConfig config) {
        this.configValue = config.getDynamicValue();
    }

    public String getConfigValue() {
        return configValue;
    }
}
