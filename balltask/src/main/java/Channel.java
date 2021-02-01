import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Channel implements Runnable {

    private int port = 9999;
    private ServerSocket ServerSocket;
    private Socket socket;
    private boolean ok;
    //private boolean running;
    private Thread channelThread;

    public Channel() {
        this.channelThread = new Thread(this);
        this.channelThread.start();
    }

    public synchronized boolean assignSocket(Socket socket) {
        if (this.socket != null && this.socket.equals(socket)) return true;
        if (this.socket != null) return false;
        this.socket = socket;
        return true;
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
        this.socket = socket;
    }

    //Método para enviar pelotas
    public void send(String message) {
        try {
            System.out.println("enviado");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            writer.write(message + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Metodo para recibir pelotas
    public String receiveBall(String info) {


        return info;
    }


    public void run() {
        try {
            while (true) {
                BufferedReader objectInputStream = null;
                while (this.socket != null) {
                    if (objectInputStream == null) {
                        objectInputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                    }
                    String recived = objectInputStream.readLine();
                    // String[] strings = recived.split(",");
                    System.out.println(recived);
                   /* if (strings[0].equals(Package.BALL)){
                        Ball ball = new Ball();
                        int size = Integer.parseInt(strings[1]);
                        int xPosition = Integer.parseInt(strings[2]);
                        int yPosition = Integer.parseInt(strings[3]);
                        int velocidad = Integer.parseInt(strings[4]);
                        int velocidad1 = Integer.parseInt(strings[5]);
                        ball.setSize(size);
                        ball.setxPosition(xPosition);
                        ball.setyPosition(yPosition);
                        ball.setEllipse2D(new Ellipse2D.Double(xPosition,yPosition,size,size));
                        ball.setxVelocity(velocidad);
                        ball.setyVelocity(velocidad1);
                        this.ballTask.addReceivedBall(ball);
                    }*/
                }
            }
        } catch (IOException e) {
            System.out.println("status false");
            e.printStackTrace();
        }
    }

   /* public void run() {
        this.running = true;
        while(this.running){
            try {
                if(this.socket != null){
                    DataInputStream reader = new DataInputStream(this.socket.getInputStream());
                    String line = reader.readUTF();
                    System.out.println(line);
                }
            } catch (Exception e) {
              e.printStackTrace();
            }
        }
    }
   public class Package{
       public static final String BALL="ball";
       public static final String GREETING="a";

       private String text;

       public Package(String text){
           this.text=text+",";
       }

       //<editor-fold desc="Getters and setters">
       public String getText() {
           return text;
       }

       public void setText(String text) {
           this.text = text;
       }
       //</editor-fold>

       public void addBallToPackage(Ball ball){
           this.text+=Integer.toString(ball.getSize())+",";
           this.text+=Integer.toString(ball.getSize()+1)+",";//Xposition
           this.text+=Integer.toString(ball.getyPosition())+",";
           this.text+=Integer.toString(ball.getxVelocity())+",";
           this.text+=Integer.toString(ball.getyVelocity())+",";
       }
   }*/
}




