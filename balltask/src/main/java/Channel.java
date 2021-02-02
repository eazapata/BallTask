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

    public synchronized void setSocket(Socket socket) {
        this.ok = true;
        this.socket = socket;
        this.channelThread = new Thread(this);
        this.channelThread.start();
    }

    private synchronized Ball createBall(String ballInfo) {
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

    //Método para enviar pelotas
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
            e.printStackTrace();
        }
    }

    //Metodo para recibir pelotas
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
            e.printStackTrace();
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




