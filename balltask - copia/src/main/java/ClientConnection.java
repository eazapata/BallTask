
import helper.BallTaskHelper;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;

public class ClientConnection implements Runnable {

    private Socket clientSocket;
    private String ip = "localhost";
    private int port = 9998;
    private Channel channel;
    private boolean running = true;

    public ClientConnection(Channel channel) {
        this.channel = channel;
        Thread clientThread = new Thread(this);
        clientThread.start();
    }

    private void startConnection() {
        Socket socket = null;
        try {
            socket = new Socket(ip, port);
            if (!this.channel.isOk()) {
                this.channel.setSocket(socket);
                System.out.println("Conexion establecida");
            }
        } catch (Exception e) {
            System.out.println("Setting socket");
            //log.error("failed to connect to server", e);
        }
    }

    @Override
    public void run() {
        while (this.running) {
            try {
                startConnection();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
