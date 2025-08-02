package com.adobe.aem.guides.wknd.core.schedulers;

import com.adobe.aem.guides.wknd.core.constants.WkndConstants;
import com.adobe.aem.guides.wknd.core.services.ResourceResolverProvider;
import com.adobe.aem.guides.wknd.core.services.WkndConfigService;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import org.apache.sling.api.resource.*;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class WkndSchedulerTest {

    final AemContext context = new AemContext();

    @Mock
    private Scheduler scheduler;

    @Mock
    private ResourceResolverFactory mockFactory;

    @Mock
    WkndConfigService wkndConfigService;

    @Mock
    ResourceResolverProvider resourceResolverProvider;

    @Mock
    private ScheduleOptions scheduleOptions;

    @Mock
    ResourceResolver mockServiceResourceResolver;

    @Mock
    private Resource resource;

    @Mock
    private ModifiableValueMap modifiableValueMap;

    @InjectMocks
    private WkndScheduler wkndScheduler;

    @BeforeEach
    void setup() {
        when(wkndConfigService.getDynamicValue()).thenReturn("MyConfigValue: ");
        when(wkndConfigService.getCronExpression()).thenReturn("0 0/15 * * * ?");

        // Setup scheduler mock chain
        when(scheduler.EXPR(anyString())).thenReturn(scheduleOptions);
        when(scheduleOptions.name(anyString())).thenReturn(scheduleOptions);
        when(scheduleOptions.canRunConcurrently(anyBoolean())).thenReturn(scheduleOptions);
        wkndScheduler.activate();

    }


    @Test
    void testActivateSchedulesJob() {
        verify(scheduler).schedule(eq(wkndScheduler), any(ScheduleOptions.class));
    }

    @Test
    void testSuccessfulRun() throws LoginException {
        context.registerService(ResourceResolverFactory.class, mockFactory);
        when(resourceResolverProvider.getServiceUserResolver(any(), any())).thenReturn(context.resourceResolver());

        // Create the resource to be written to
        Resource resource = context.create().resource(WkndConstants.DYNAMIC_DATA_RESOURCE_PATH);

        // Execute the scheduler
        wkndScheduler.run();

        // Validate the data
        ModifiableValueMap map = resource.adaptTo(ModifiableValueMap.class);
        assertNotNull(map);

        String expected = "MyConfigValue: " + LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        assertEquals(expected, map.get("data"));
    }

    @Test
    void testRunNoConfigService() throws LoginException {
        // Validate the config service returns null
        when(wkndConfigService.getDynamicValue()).thenReturn(null);
        when(wkndConfigService.getCronExpression()).thenReturn(null);
        assertNull(wkndConfigService.getDynamicValue());
        assertNull(wkndConfigService.getCronExpression());
    }

    @Test
    void testNoDataResource() throws LoginException {
        ResourceResolver mockResolver = mock(ResourceResolver.class);

        when(resourceResolverProvider.getServiceUserResolver(any(), any())).thenReturn(mockResolver);

        when(mockResolver.getResource(WkndConstants.DYNAMIC_DATA_RESOURCE_PATH)).thenReturn(null);

        Error thrown = assertThrows(Error.class, () -> wkndScheduler.run());

        assertNotNull(thrown.getMessage());
        assertEquals("Scheduler failed: no dynamic data resource found.", thrown.getMessage());
    }

    //still need to test
    @Test
    void testNullValueMap() throws LoginException {
        // Arrange
        ResourceResolver mockResolver = mock(ResourceResolver.class);
        Resource mockResource = mock(Resource.class);

        when(resourceResolverProvider.getServiceUserResolver(any(), any())).thenReturn(mockResolver);
        when(mockResolver.getResource(WkndConstants.DYNAMIC_DATA_RESOURCE_PATH)).thenReturn(mockResource);
        when(mockResource.adaptTo(ModifiableValueMap.class)).thenReturn(null);

        // Act + Assert
        Error thrown = assertThrows(Error.class, () -> wkndScheduler.run());

        assertNotNull(thrown.getMessage());
        assertEquals("Scheduler failed: value map is null!", thrown.getMessage());
    }

}



