import java.io.IOException;
import java.util.Scanner;

public class MainServer {
    public static void main(String[] args) throws IOException {
        ConfigReader cr = new ConfigReader();
        cr.getPropValues();
        System.out.println(cr.port);
        Scanner myObj = new Scanner(System.in);
        myObj.nextLine();
    }
}
