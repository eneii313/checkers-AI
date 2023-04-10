package BoardGames;

import BoardGames.Board.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class MainGUI extends JFrame {
    public final int MAXWIDTH = 720; //9*8
    public final int MAXHEIGHT = 720;

    private BoardGUI checkersPanel;

    private JPanel startPanel;
    private JButton startButton;

    private JPanel endPanel;
    private JButton restartButton;
    private JLabel winnerText;

    public MainGUI() {
        super("MinIMax Checkers");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(MAXWIDTH, MAXHEIGHT));
        makeStartScreen();
        makeGameOverScreen();
        this.add(startPanel);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public void setMouseListeners(MouseListener listener) {
        checkersPanel.setMouseListeners(listener);
    }

    public void setActionListeners(ActionListener listener){
        startButton.addActionListener(listener);
        restartButton.addActionListener(listener);
    }

    public void removeActionListeners(ActionListener listener){
        startButton.removeActionListener(listener);
        restartButton.removeActionListener(listener);
    }

    private void makeStartScreen() {
        startPanel = new JPanel();
        startPanel.setLayout(null);
        startPanel.setSize(MAXWIDTH, MAXHEIGHT);
        startPanel.setBackground(Color.LIGHT_GRAY);

        ImageIcon bg = new ImageIcon(loadImage("images/logo.png"));
        JLabel logo = new JLabel();
        logo.setIcon(bg);
        logo.setBounds (240, 180, bg.getIconWidth(), bg.getIconHeight());

        JLabel welcome = new JLabel("Welcome to Checkers!", JLabel.CENTER);
        welcome.setBounds(20, 60, MAXWIDTH - 40, 120);
        welcome.setFont(new Font ("Monospaced", Font.BOLD, 50));

        JLabel blackText = new JLabel("Black:AI", JLabel.CENTER);
        blackText.setBounds(40, 220, 200, 100);
        blackText.setFont(new Font ("Monospaced", Font.BOLD, 30));

        JLabel redText = new JLabel("Red:Player", JLabel.CENTER);
        redText.setBounds(460, 220, 200, 100);
        redText.setFont(new Font ("Monospaced", Font.BOLD, 30));
        redText.setForeground(new Color(180,0,0));

        startButton = new JButton("Start Game");
        startButton.setFont(new Font ("Arial", Font.BOLD, 20));
        startButton.setFocusPainted(false);
        startButton.setBounds(240, 450, 200, 50);

        startPanel.add(logo);
        startPanel.add(welcome);
        startPanel.add(blackText);
        startPanel.add(redText);
        startPanel.add(startButton);
        startPanel.setVisible(true);
    }

    private void makeGameOverScreen(){
        endPanel = new JPanel();
        endPanel.setLayout(null);
        endPanel.setSize(MAXWIDTH, MAXHEIGHT);
        endPanel.setBackground(Color.LIGHT_GRAY);
        JLabel gameOver = new JLabel("Game Over!", JLabel.CENTER);
        gameOver.setBounds(20, 120, MAXWIDTH - 40, 120);
        gameOver.setFont(new Font ("Monospaced", Font.BOLD, 50));
        gameOver.setForeground(new Color(0,100, 0));

        winnerText = new JLabel("", JLabel.CENTER);
        winnerText.setBounds(0, 200, MAXWIDTH, 100);
        winnerText.setFont(new Font ("Monospaced", Font.BOLD, 40));

        restartButton = new JButton("Play Again");
        restartButton.setFont(new Font ("Arial", Font.BOLD, 20));
        restartButton.setFocusPainted(false);
        restartButton.setBounds(240, 350, 200, 50);

        endPanel.add(gameOver);
        endPanel.add(winnerText);
        endPanel.add(restartButton);
        endPanel.setVisible(true);
    }


    public BoardGUI makeCheckersPanel(Board b) {
        checkersPanel = new BoardGUI(b);
        return checkersPanel;
    }

    public void showGame() {
        this.getContentPane().removeAll();
        this.add(checkersPanel);
        this.repaint();
        this.revalidate();
    }

    public void endGame(boolean winner){
        this.getContentPane().removeAll();
        if (winner) {
            winnerText.setForeground(Color.BLACK);
            winnerText.setText("Black AI Won!");
        }
        else {
            winnerText.setText("Red Player Won!");
            winnerText.setForeground(new Color(180,0,0));
        }
        this.add(endPanel);
        this.repaint();
        this.revalidate();
    }

    // checks if the image file is available
    protected Image loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (Exception e){
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
