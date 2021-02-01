import sun.security.jca.GetInstance;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


public class BallTask extends JFrame {
    private Viewer viewer;
    private ControlPanel controlPanel;
    private ArrayList<Ball> balls;
    private ArrayList<BlackHole> blackHoles;
    private Dimension dimension;
    private Statistics statistics;
    private Channel channel;
    private ServerConnection serverConnection;
    private ClientConnection clientConnection;

    public BallTask() {
        this.setTitle("original");
        this.channel = new Channel(this);
        this.serverConnection = new ServerConnection(this.channel);
        this.clientConnection = new ClientConnection(this.channel);

        this.dimension = getToolkit().getScreenSize();
        this.setSize(dimension.width, dimension.height);
        this.setVisible(true);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.statistics = new Statistics();

        this.viewer = new Viewer(dimension.width, dimension.height,this);
        createBlackHoles();
        createBalls();

        this.addControlPanel(c);
        this.addViewer(c);
        this.pack();
    }

    public ArrayList<Ball> getBalls() {
        return balls;
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

    public void addBall(String ballInfo) {

        String[] info = ballInfo.split(",");
        Ball ball = new Ball();
        ball.setViewer(this.viewer);
        ball.setBallTask(this);
        ball.setSize(Integer.parseInt(info[0]));
        ball.setOutSide(true);
        ball.setColor(new Color(0, 255, 0));
        ball.setBorderColor(new Color(0));
        ball.setCordY(Integer.parseInt(info[1]));
        ball.setCordX(Integer.parseInt(info[2]));
        ball.setVelX(1);
        ball.setVelY(1);
        ball.setSleepTime(1);
        ball.setStopped(false);
        ball.setChannel(this.channel);
        ball.setRect(new Rectangle(ball.getSize(), ball.getSize()));
        ball.setBallThread(new Thread(ball));
        ball.getBallThread().start();
        addBall(ball);
    }

    private void createBalls() {
        this.balls = new ArrayList<Ball>();
        for (int i = 0; i < 10; i++) {
            Ball ball = new Ball(this, this.viewer, this.channel);
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
        if (ball.getCordX() - ball.getVelX() <= 0) {

            ball.moveBall("left");

        } else if (ball.getCordX() + ball.getVelX() >= this.viewer.getWidth() - ball.getSize()) {

            ball.moveBall("right");

        } else if (ball.getCordY() - ball.getVelY() <= 0) {

            ball.moveBall("up");

        } else if (ball.getCordY() + ball.getVelY() >= this.viewer.getHeight() - ball.getSize()) {
            ball.moveBall("down");
        } else {
            ball.moveBall("");
        }
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

    public synchronized void removeBall(Ball ball){
        this.balls.remove(ball);
    }

    public synchronized void addBall(Ball ball){
        this.balls.add(ball);
    }

}



