import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server
{
    public static void main(String[] args)
    {
        int port1 = 0, port2 = 0;
        String address1 = null, address2;
        byte[] bytes = new byte[1024];
        try
        {
            System.out.println("Server waiting");
            DatagramSocket ds = new DatagramSocket(789);
            while(!Thread.interrupted())
            {
                DatagramPacket p = new DatagramPacket(bytes, bytes.length);
                ds.receive(p);
                System.out.println(p.getAddress());
                if(port1 == 0)
                {

                    port1 = p.getPort();
                    address1 = p.getAddress().getHostAddress();
                    System.out.println("(1st) Server received:" + new String(bytes) + " from " + address1 + " on port " + port1);
                }
                else
                {
                    port2 = p.getPort();
                    address2 = p.getAddress().getHostAddress();
                    System.out.println("(2nd) Server received:" + new String(bytes) + " from " + address1 + " on port " + port1);
                    sendConnDataTo(address1, port1, address2, port2, ds);
                    sendConnDataTo(address2, port2, address1, port1, ds);
                }
            }
            ds.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void sendConnDataTo(String a1, int p1, String a2, int p2, DatagramSocket ds)
    {
        byte[] bA, bP;
        bA = a1.getBytes();
        bP = Integer.toString(p1).getBytes();
        DatagramPacket pck;
        try
        {
            pck = new DatagramPacket(bA, bA.length, InetAddress.getByName(a2), p2);
            ds.send(pck);
            pck = new DatagramPacket(bP, bP.length, InetAddress.getByName(a2), p2);
            ds.send(pck);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}