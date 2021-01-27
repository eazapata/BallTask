import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class BallTask extends JFrame {
    private Viewer viewer;
    private ControlPanel controlPanel;
    private ArrayList<Ball> balls;
    private ArrayList<BlackHole> blackHoles;
    private Dimension dimension;
    private Statistics statistics;

    public BallTask() {
        this.dimension = getToolkit().getScreenSize();
        this.setSize(dimension.width, dimension.height);
        this.setVisible(true);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.statistics = new Statistics();
        this.viewer = new Viewer(dimension.width, dimension.height);
        createBlackHoles();
        createBalls();


        this.addControlPanel(c);
        this.addViewer(c);
        this.pack();

    }

    public static void main(String[] args) {

        BallTask ballTask = new BallTask();

    }

    private void addControlPanel(GridBagConstraints c) {
        this.controlPanel = new ControlPanel(statistics, balls);
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.VERTICAL;
        c.gridy = 0;
        c.gridx = 0;
        c.weighty = 0.5;
        c.weightx = 0.0;
        c.gridwidth = 1;
        this.add(controlPanel, c);
    }

    private void addViewer(GridBagConstraints c) {

        this.viewer.loadBackground();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.gridx = 1;
        c.weightx = 1.9;
        this.add(this.viewer, c);
        Thread viewerThread = new Thread(this.viewer);
        viewerThread.start();

    }

    private void createBalls() {
        this.balls = new ArrayList<Ball>();
        for (int i = 0; i < 10; i++) {
            Ball ball = new Ball(this, this.viewer);
            this.balls.add(ball);
            this.statistics.setBall();
        }
        this.viewer.setBalls(this.balls);
    }

    private void createBlackHoles() {
        this.blackHoles = new ArrayList<BlackHole>();
        for (int i = 0; i < 2; i++) {
            BlackHole blackHole = new BlackHole(this);
            blackHoles.add(blackHole);
        }
        this.viewer.setBlackHoles(this.blackHoles);
    }

    public void checkMove(Ball ball) {
        String action = "";
        if (ball.getCordX() - ball.getVelX() <= 0) {
            action = "left";
        } else if (ball.getCordX() + ball.getVelX() >= this.viewer.getWidth() - ball.getWidth()) {
            action = "right";
        } else if (ball.getCordY() - ball.getVelY() <= 0) {
            action = "up";
        } else if (ball.getCordY() + ball.getVelY() >= this.viewer.getHeight() - ball.getHeight()) {
            action = "down";
        }
        ball.moveBall(action);
        checkBlackHole(ball);


    }

    private void checkBlackHole(Ball ball) {

        for (BlackHole blackHole : this.blackHoles) {
            if (blackHole.getRect().intersects(ball.getRect()) && ball.isOutSide()) {
                blackHole.putBall(ball);
            }
            if (!blackHole.getRect().intersects(ball.getRect()) && !ball.isOutSide()) {
                blackHole.removeBall(ball);
            }
        }
    }
}



