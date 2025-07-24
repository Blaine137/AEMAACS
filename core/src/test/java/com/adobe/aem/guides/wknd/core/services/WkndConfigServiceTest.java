import com.adobe.aem.guides.wknd.core.services.WkndConfig;
import com.adobe.aem.guides.wknd.core.services.WkndConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class WkndConfigServiceTest {

    private WkndConfigService wkndConfigService;
    private WkndConfig mockConfig;

    @BeforeEach
    void setUp() {
        // Mock the configuration interface
        mockConfig = mock(WkndConfig.class);
        when(mockConfig.getDynamicValue()).thenReturn("Test Config Value");

        // Activate the service with mock config
        wkndConfigService = new WkndConfigService();
        wkndConfigService.start(mockConfig);  // calls @Activate
    }

    @Test
    void testConfigValueIsStoredCorrectly() {
        assertEquals("Test Config Value", wkndConfigService.getConfigValue());
    }
}