import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Viewer extends Canvas implements Runnable {

    private BufferedImage background;
    private ArrayList<Ball> balls;
    private ArrayList<BlackHole> blackHoles;


    public Viewer(int width, int height) {
        this.setSize(width, height);
    }

    public void setBalls(ArrayList<Ball> balls) {
        this.balls = balls;
    }

    public void setBlackHoles(ArrayList<BlackHole> blackHoles){
        this.blackHoles = blackHoles;
    }

    public ArrayList<Ball> getBalls() {
        return balls;
    }

    /**
     * Carga un fondo de pantalla para el viewer
     *
     *
     */
    public void loadBackground() {
        try {
            File file = new File("F:\\Datos\\Usuario\\src\\BallTaskRepo\\BallTask\\balltask\\src\\main\\resources\\fondo.png");
            this.background = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paint() {
        BufferStrategy bs;
        bs = this.getBufferStrategy();

        Graphics g = bs.getDrawGraphics();
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
        if (this.blackHoles != null) {
            for (BlackHole blackHole : this.blackHoles) {
                blackHole.paint(g);
            }
        }
        if (this.balls != null) {
            for (Ball ball : balls) {
                ball.paint(g);
            }
        }
        bs.show();
        g.dispose();
    }

    public void run() {
        this.createBufferStrategy(2);
        do {
            this.paint();
        } while (true);
    }
}
