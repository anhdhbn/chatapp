import java.io.IOException;

public class MainServer {
    public static void main(String[] args) throws IOException {
        ConfigReader cr = new ConfigReader();
        cr.getPropValues();
        System.out.println(cr.port);
    }
}
