package com.adobe.aem.guides.wknd.core.listeners;

import com.adobe.aem.guides.wknd.core.constants.WkndConstants;
import com.adobe.aem.guides.wknd.core.listeners.WkndJobCreater;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.event.jobs.JobManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WkndJobCreaterTest {

    @InjectMocks
    private WkndJobCreater wkndJobCreater;

    @Mock
    private JobManager jobManager;

    private ResourceChange sampleChange;

    @BeforeEach
    void setup() {
        sampleChange = new ResourceChange(
                ResourceChange.ChangeType.CHANGED,
                "/data/dynamicData/test",
                false
        );
    }

    @Test
    void testOnChange_createsJobWithCorrectProperties() {
        List<ResourceChange> changes = Collections.singletonList(sampleChange);

        wkndJobCreater.onChange(changes);

        ArgumentCaptor<Map<String, Object>> propertiesCaptor = ArgumentCaptor.forClass(Map.class);

        verify(jobManager, times(1)).addJob(eq(WkndConstants.PAGE_DATA_MODIFIED), propertiesCaptor.capture());

        Map<String, Object> props = propertiesCaptor.getValue();
        assert props.get("path").equals("/data/dynamicData/test");
        assert props.get("changeType").equals("CHANGED");
    }

    @Test
    void testOnChange_handlesMultipleChanges() {
        ResourceChange change2 = new ResourceChange(
                ResourceChange.ChangeType.CHANGED,
                "/data/dynamicData/another",
                false
        );

        List<ResourceChange> changes = Arrays.asList(sampleChange, change2);

        wkndJobCreater.onChange(changes);

        verify(jobManager, times(2)).addJob(eq(WkndConstants.PAGE_DATA_MODIFIED), anyMap());
    }
}