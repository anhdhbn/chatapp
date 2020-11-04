package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class ConfigReader {
    public int port = -1;
    public void getPropValues() throws IOException {
        InputStream inputStream = null;
        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            int port = Integer.parseInt(prop.getProperty("port"));
            this.port = port;
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
    }
}
