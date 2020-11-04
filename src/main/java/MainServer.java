import java.io.IOException;
import java.util.Scanner;

public class MainServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        ConfigReader cr = new ConfigReader();
        cr.getPropValues();
        System.out.println(cr.port);
        while (true){
            Thread.sleep(1000);
        }
    }
}
