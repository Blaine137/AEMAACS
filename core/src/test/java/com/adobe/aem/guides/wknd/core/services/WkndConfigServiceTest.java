import com.adobe.aem.guides.wknd.core.services.WkndConfig;
import com.adobe.aem.guides.wknd.core.services.WkndConfigService;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

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
        when(mockConfig.getCronExpression()).thenReturn("0 */5 * * * ?");

        // Activate the service with mock config
        wkndConfigService = new WkndConfigService();
        wkndConfigService.start(mockConfig);  // calls @Activate
    }

    @Test
    void testConfigValueIsStoredCorrectly() {

        assertEquals("Test Config Value", wkndConfigService.getDynamicValue());
        assertEquals("0 */5 * * * ?", wkndConfigService.getCronExpression());
    }

}