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
    private ServerSocket serverSocket;
    private boolean running = true;

    public ServerConnection(Channel channel) {

        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.channel = channel;
        this.serverThread = new Thread(this);
        this.serverThread.start();
    }

    private void startConnection() {
        try {
            while (this.socket == null) {
                this.socket = serverSocket.accept();
                if (!this.channel.isOk()) {
                    String clientAddress = this.socket.getInetAddress().getHostAddress();
                    System.out.println("New connection from: " + clientAddress);
                    this.channel.setSocket(this.socket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        while (this.running) {
            try {
                startConnection();
            } catch (Exception e) {
                System.out.println("Error initial connection" + e);
            }
        }


    }
}
