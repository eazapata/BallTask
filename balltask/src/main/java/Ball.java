import java.awt.*;
import java.io.Serializable;
import java.util.Random;


public class Ball implements Runnable, VisualObject, Serializable {

    private int width;
    private int height;
    private int velX;
    private int velY;
    private int cordX;
    private int cordY;
    private final Thread ballThread;
    private boolean outSide;
    private boolean stopped;
    private BallTask ballTask;
    private Random random;
    private Color color;
    private Color borderColor;
    private String status;
    private int sleepTime;
    private Rectangle rect;
    private int size;
    private Viewer viewer;
    private boolean running;
    private Channel channel;

    public Ball() {
        ballThread = new Thread(this);
        ballThread.start();
    }

    public Ball(BallTask ballTask, Viewer viewer, Channel channel) {
        this.viewer = viewer;
        this.random = new Random();
        this.ballTask = ballTask;
        this.size = this.random.nextInt(150) + 30;
        this.width = this.size;
        this.height = this.size;
        this.outSide = true;
        this.color = new Color(255);
        this.borderColor = this.color;
        this.cordY = this.random.nextInt(this.ballTask.getHeight() - (this.height * 3));
        this.cordX = this.random.nextInt(this.ballTask.getWidth() - (this.width * 4));
        this.velY = 1;
        this.velX = 1;
        this.sleepTime = 1;
        this.stopped = false;
        this.channel = channel;
        this.rect = new Rectangle(this.width, this.height);
        ballThread = new Thread(this);
        ballThread.start();
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getVelX() {
        return velX;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public int getVelY() {
        return velY;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public int getCordX() {
        return cordX;
    }

    public void setCordX(int cordX) {
        this.cordX = cordX;
    }

    public int getCordY() {
        return cordY;
    }

    public void setCordY(int cordY) {
        this.cordY = cordY;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public boolean isOutSide() {
        return outSide;
    }

    public void setOutSide(boolean outSide) {
        this.outSide = outSide;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * Método para mover la bola
     */
    public void moveBall(String action) {
        int absX = Math.abs(this.getVelX());
        int absY = Math.abs(this.getVelY());

        if (!this.stopped) {
            if (action.equals("left")) {
                this.setVelX(absX);
            } else if (action.equals("right")) {
                this.setVelX(-absX);
            } else if (action.equals("up")) {
                this.setVelY(absY);
            } else if (action.equals("down")) {
                this.setVelY(-absY);
            } else if (action.equals("send")) {
                //channel.sendBall(this);
            }
            this.rect.setBounds(this.cordX, this.cordY, this.width, this.height);
            cordX = cordX + velX;
            cordY = cordY + velY;
        }
    }

    /**
     * Método para pintar la bola.
     */
    public void paint(Graphics g) {
        if (this.isOutSide()) {

            g.setColor(this.color);
        } else {
            g.setColor(this.borderColor);
        }
        g.fillOval(this.cordX, this.cordY, width, height);
    }

    public void run() {
        this.running = true;
        while (running) {
            try {
                Thread.sleep(this.sleepTime);
                ballTask.checkMove(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}