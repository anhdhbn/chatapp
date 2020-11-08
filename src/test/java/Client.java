import nputils.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class Client
{
    private DatagramSocket socket;
    private int init = 0;
    private String target;
    private int port;

    public Client()
    {
        try
        {
            socket = new DatagramSocket();
        }
        catch(SocketException e)
        {
            e.printStackTrace();
        }
        Thread in = new Thread()
        {
            public void run()
            {
                while(true)
                {
                    byte[] bytes = new byte[Constants.BUFFER_SIZE];
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                    try
                    {
                        socket.receive(packet);
                        bytes = Arrays.copyOfRange(bytes, 0, packet.getLength());
                        String s = new String(bytes);
                        System.out.println("Received: " + s);
                        if(init == 0)
                        {
                            target = s;
                            System.out.println("Target: " + target);
                            init++;
                        }
                        else if(init == 1)
                        {
                            port = Integer.parseInt(s);
                            System.out.println("Port: " + port);
                            init++;
                        }
                        else System.out.println(new String(bytes));
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
        in.start();
        connectToSupervisor();
    }

    private void connectToSupervisor()
    {
        byte[] bytes = new byte[Constants.BUFFER_SIZE];
        System.out.println("Greeting server");
//        System.arraycopy("".getBytes(), 0, bytes, 0, 4);
        try
        {
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, InetAddress.getByName("localhost"), 789);
            socket.send(packet);

//            socket.send(packet);
//            socket.close();
            DatagramPacket packet1 = new DatagramPacket(bytes, bytes.length);
            socket.receive(packet1);

            System.out.println("Greetings sent...");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        send();
    }

    private void send()
    {
        while(init != 2)
        {
            try
            {
                Thread.sleep(20L);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("Init completed!");
        while(true)
        {
            byte[] b2 = "Hello".getBytes();
            byte[] b1 = new byte[6];
            System.arraycopy(b2, 0, b1, 0, b2.length);
            try
            {
                DatagramPacket packet = new DatagramPacket(b1, b1.length, InetAddress.getByName(target), port);
                socket.send(packet);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args)
    {
        new Client();
    }
}