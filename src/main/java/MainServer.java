import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class MainServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        ConfigReader cr = new ConfigReader();
        cr.getPropValues();
        System.out.println(cr.port);
        while (true){
            Thread.sleep(500);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));
        }
    }
}
