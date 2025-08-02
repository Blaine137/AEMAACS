package com.adobe.aem.guides.wknd.core.schedulers;

import com.adobe.aem.guides.wknd.core.constants.WkndConstants;
import com.adobe.aem.guides.wknd.core.services.ResourceResolverProvider;
import com.adobe.aem.guides.wknd.core.services.WkndConfigService;
import com.adobe.aem.guides.wknd.core.utils.ResourceResolverUtils;
import org.apache.sling.api.resource.*;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component(immediate = true, service=WkndScheduler.class)
public class WkndScheduler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(WkndScheduler.class);

    private static final String JOB_NAME = "com.adobe.aem.guides.wknd.core.schedulers.WkndScheduler";

    @Reference
    private Scheduler scheduler;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private WkndConfigService wkndConfigService;

    @Reference
    private ResourceResolverProvider resourceResolverProvider;

    private String dynamicValue;
    private String cronExpression;

    private String dynamicValueWithDate;


    @Activate
    protected void activate() {
        log.info("Activating WKND Scheduler...");
        this.dynamicValue = wkndConfigService.getDynamicValue();
        this.cronExpression = wkndConfigService.getCronExpression();

        ScheduleOptions scheduleOptions = scheduler.EXPR(cronExpression); // Runs every day at midnight
        scheduleOptions.name(JOB_NAME);
        scheduleOptions.canRunConcurrently(false);

        scheduler.schedule(this, scheduleOptions);

        log.info("WKND Scheduler registered to run at this cron expression: {}", cronExpression);
    }

    @Deactivate
    protected void deactivate() {
        scheduler.unschedule(JOB_NAME);
        log.info("WKND Scheduler unscheduled.");
    }

    @Override
    public void run() {
        log.info("WKND Scheduler running... Retrieved config value: {}", dynamicValue);

        // You can add your daily job logic here
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = currentDate.format(formatter);
        dynamicValueWithDate = dynamicValue + formattedDate;

        try (ResourceResolver resourceResolver = resourceResolverProvider.getServiceUserResolver(resourceResolverFactory, WkndConstants.DATA_WRITE_SERVICE_USER)) {
            Resource dynamicData = resourceResolver.getResource(WkndConstants.DYNAMIC_DATA_RESOURCE_PATH);
            if(dynamicData != null){
                ModifiableValueMap valueMap = dynamicData.adaptTo(ModifiableValueMap.class);
                if(valueMap != null){
                    valueMap.put("data", dynamicValueWithDate);
                    resourceResolver.commit();
                }else{
                    log.error("WkndScheduler: Error while adapting dynamic data resource to Modifiable Value Map!");
                    throw new RuntimeException("Scheduler failed: value map is null!");
                }
            }else{
                log.error("WkndScheduler: Error while getting Dynamic Data Resource at: {}", WkndConstants.DYNAMIC_DATA_RESOURCE_PATH);
                throw new RuntimeException("Scheduler failed: no dynamic data resource found.");
            }
        }catch(Exception e){
            log.error("WkndScheduler: error while getting resource resolver: {}", e.getMessage());
            throw new Error(e.getMessage());
        }
    }
}
