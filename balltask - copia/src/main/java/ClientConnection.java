import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientConnection implements Runnable {


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
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String greeting = "BALLTASK";
            out.writeUTF(greeting);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String response = in.readUTF();
            if (!this.channel.isOk() && response.equals("OK")) {
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
