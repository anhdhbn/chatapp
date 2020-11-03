import org.junit.jupiter.api.Assertions;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ConfigReaderTest {

    @org.junit.jupiter.api.Test
    void testGetPropValues() throws IOException {
        ConfigReader cr = new ConfigReader();
        cr.getPropValues();
        Assertions.assertEquals(1699, cr.port);
    }
}