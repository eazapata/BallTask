import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class BlackHole implements VisualObject {

    private int width;
    private int height;
    private int cordY;
    private int cordX;
    private Random random;
    private BallTask ballTask;
    private Rectangle rect;
    private Color color;
    private int count;
    private ArrayList<Ball> ball;

    public BlackHole(BallTask ballTask) {
        this.ballTask = ballTask;
        this.random = new Random();
        this.ball = new ArrayList<Ball>();
        this.width = 250;
        this.height = 100;
        this.cordY = this.random.nextInt(this.ballTask.getHeight() - (this.height * 2));
        this.cordX = this.random.nextInt(this.ballTask.getWidth() - (this.width * 2));
        this.rect = new Rectangle(width, height);
        this.rect.setBounds(this.cordX, this.cordY, width, height);
        this.color = Color.white;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void paint(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.cordX, this.cordY, width, height);

    }

    //Metodo para añadir una pelota
    public synchronized void putBall(Ball ball) {

        while (this.count >= 1) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.ball.add(ball);
        this.count++;
        ball.setSleepTime(50);
        ball.setOutSide(false);
        notifyAll();
    }

    //Metodo para retirar la pelota
    public synchronized void removeBall(Ball ball) {
        if (this.ball.size() > 0) {
            if (ball.equals(this.ball.get(0))) {
                this.count--;
                this.ball.remove(0);
                ball.setOutSide(true);
                ball.setSleepTime(1);
                notifyAll();
            }
        }
    }
}