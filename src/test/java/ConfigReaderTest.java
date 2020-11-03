import org.junit.jupiter.api.Assertions;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
class ConfigReaderTest {

    @Test
    void testGetPropValues() throws IOException {
        ConfigReader cr = new ConfigReader();
        cr.getPropValues();
        assertEquals(1699, cr.port);
    }
}