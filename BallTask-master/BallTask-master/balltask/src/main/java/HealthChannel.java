

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HealthChannel implements Runnable{

    private Channel channel;
    private Thread healthThread;
    private boolean running;
    private Socket socket;
    private boolean health;

    public void setHealth(boolean health) {
        this.health = health;
    }

    public void setHealthThread(Thread healthThread) {
        this.healthThread = healthThread;
    }

    public HealthChannel(Channel channel, Socket socket) {
        this.channel = channel;
        this.socket = socket;
        this.healthThread = new Thread();
        this.healthThread.start();
    }

    private void sendOk(){
        try {
            DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
            String works = "channel ok?";
            out.writeUTF(works);

            DataInputStream in = new DataInputStream(this.socket.getInputStream());
            String response = in.readUTF();

            if(!response.equals("channel ok")){
                this.channel.setOk(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        while(this.channel.isOk()){
            try {
                Thread.sleep(15);
                sendOk();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
