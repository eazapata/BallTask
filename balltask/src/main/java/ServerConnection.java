import helper.BallTaskHelper;
import org.omg.CORBA.Object;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConnection implements Runnable {

    private int port = 9999;
    private BallTask ballTask;
    private Thread serverThread;
    private Channel channel;
    private Socket socket;

    public ServerConnection(Channel channel) {
        this.channel = channel;
        this.serverThread = new Thread(this);
        this.serverThread.start();
    }


    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(this.port);
            System.out.println("Waiting connections.");
            while (true) {
                socket = serverSocket.accept();
                String clientAddress = socket.getInetAddress().getHostAddress();
                System.out.println("New connection from: " + clientAddress);
                BufferedReader objectInputStream = null;
                if (objectInputStream == null) {
                    objectInputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                }
                String recived = objectInputStream.readLine();
                // String[] strings = recived.split(",");
                System.out.println(recived);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
