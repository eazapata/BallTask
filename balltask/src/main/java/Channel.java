import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Channel implements Runnable {

    private Socket socket;
    private boolean ok;
    private Thread channelThread;
    private BallTask ballTask;


    public Channel(BallTask ballTask) {
        this.ballTask = ballTask;
    }

    // GETTERS Y SETTERS
   // ----------------------------------------------------------
    public synchronized boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Thread getChannelThread() {
        return channelThread;
    }

    public void setChannelThread(Thread channelThread) {
        this.channelThread = channelThread;
    }

    public boolean isStatus() {
        return ok;
    }

    public void setStatus(boolean status) {
        this.ok = status;
    }

    public Socket getSocket() {
        return socket;
    }

    /**
     * Método para asignar un socket al channel
     * @param socket
     */
    public synchronized void setSocket(Socket socket) {
        this.ok = true;
        this.socket = socket;
        this.channelThread = new Thread(this);
        this.channelThread.start();
       // this.healthChannel = new HealthChannel(this,this.socket);
    }

    /**
     * Método para crear una pelota nueva a partir de la info de un string.
     * @param ballInfo información relevante de una pelota.
     * @return la nueva pelota.
     */
    private Ball createBall(String ballInfo) {
        String[] info = ballInfo.split(",");
        Ball ball = new Ball();
        ball.setBallTask(this.ballTask);
        ball.setSize(Integer.parseInt(info[0]));
        ball.setOutSide(true);
        ball.setColor(new Color(0, 255, 0));
        ball.setBorderColor(new Color(0));
        ball.setCordY(Integer.parseInt(info[3]));
        ball.setCordX(0);
        ball.setVelX(Integer.parseInt(info[1]));
        ball.setVelY(Integer.parseInt(info[2]));
        ball.setSleepTime(1);
        ball.setStopped(false);
        ball.setChannel(this);
        ball.setRect(new Rectangle(ball.getSize(), ball.getSize()));
        ball.getRect().setBounds(ball.getCordX(), ball.getCordY(), ball.getSize(), ball.getSize());
        ball.setBallThread(new Thread(ball));
        ball.getBallThread().start();
        return ball;
    }

    public void sendACK(String message){
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(this.socket.getOutputStream());
            String works = "channel ok?";
            out.writeUTF(works);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para enviar la información relevante de una pelota a través del canal.
     * @param ball objeto que queremos enviar a través del channel.
     */
    public void send(Ball ball) {
        try {
            DataOutputStream writer = new DataOutputStream((this.socket.getOutputStream()));
            String ballInfo = ball.getSize() + "," +
                    ball.getVelX() + "," +
                    ball.getVelY() + "," +
                    ball.getCordY() + "," +
                    ball.getCordX() + "\n";
            writer.writeUTF(ballInfo);
            this.ballTask.removeBall(ball);
        } catch (IOException e) {
            System.out.println("Connection reset");
            this.ok = false;
        }
    }

    /**
     * Método que usamos para recibir la información de la pelota, crear una nueva y añadirla a la lista del balltask.
     */
    public void receiveInfo() {
        DataInputStream reader = null;
        try {
            reader = new DataInputStream(this.socket.getInputStream());
            String received = null;
            received = reader.readUTF();

            if (received == null) {
                System.out.println("Content is null");
                this.ok = false;
            } else {
                System.out.println(received);
            }

            Ball ball = createBall(received);
            this.ballTask.addNewBall(ball);
        } catch (IOException e) {
            System.out.println("Connection reset");
            this.ok = false;
        }
    }

    public void run() {
        while (this.isOk()) {
            try {
                receiveInfo();
            } catch (Exception e) {
                System.out.println("Recibiendo info");
            }
        }
    }
}




