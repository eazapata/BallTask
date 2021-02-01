import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Channel implements Runnable {

    private int port = 9999;
    private ServerSocket ServerSocket;
    private Socket socket;
    private boolean ok;
    private boolean running;
    private Thread channelThread;
    private BallTask ballTask;

    public Channel(BallTask ballTask){
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

    //Método para enviar pelotas
    public void send(Ball ball) {
        try {
            PrintWriter writer = new PrintWriter((this.socket.getOutputStream()), true);
            String ballInfo = String.valueOf(ball.getSize()) + "," +
                    String.valueOf(ball.getVelX() + "," +
                            String.valueOf(ball.getCordY()) + "," +
                            String.valueOf(ball.getCordX()));
            writer.println(ballInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Metodo para recibir pelotas
    public void receiveInfo() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String received = reader.readLine();
            this.ballTask.addBall(received);
            if (received == null) {
                System.out.println("Content is null");
                this.ok = false;
            } else {

                System.out.println(received);
            }
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




