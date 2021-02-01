
import helper.BallTaskHelper;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;

public class ClientConnection implements Runnable {

    private Socket clientSocket;
    private String ip = "127.0.0.1";
    private int port = 9999;
    private Channel channel;

    public ClientConnection(Channel channel) {
        this.channel = channel;
        Thread clientThread = new Thread(this);
        clientThread.start();
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            while (true) {
                Thread.sleep(200);
                try {
                    socket = new Socket(ip, port);
                } catch (ConnectException e) {
                    //log.error("failed to connect to server", e);
                }
                System.out.println("enviado");
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                writer.write("hola" + "\n");
                writer.flush();

                if (socket != null) {
                    if (!this.channel.assignSocket(socket)) {
                        socket = null;
                    } else {
                        String packet = "prueba";
                        this.channel.send(packet);
                    }
                }
            }
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
