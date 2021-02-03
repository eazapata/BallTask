

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HealthChannel implements Runnable {

    private Channel channel;
    private Thread healthThread;
    private boolean running;
    private Socket socket;
    private boolean ACK;


    public HealthChannel(Channel channel, Socket socket) {
        this.channel = channel;
        this.socket = socket;
        this.healthThread = new Thread();
        this.healthThread.start();
    }

    public synchronized void setACK(boolean ACK) {
        this.ACK = ACK;
    }

    public boolean isACK() {
        return ACK;
    }

    private void sendOk() {

        try {
            this.ACK = false;
            DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
            String works = "channel ok?";
            out.writeUTF(works);

            DataInputStream in = new DataInputStream(this.socket.getInputStream());
            String response = in.readUTF();

            if (!response.equals("channel ok")) {
                this.channel.setOk(false);
                this.socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        while (this.channel.isOk()) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendOk();
        }

    }
}
