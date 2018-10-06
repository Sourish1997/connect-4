import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JPanel implements ActionListener, KeyListener {
    static GameState game;
    int x = 90, y = 65;
    static javax.swing.Timer timer;

    private static JMenuBar menuBar;
    private static JMenu menus[];
    private static JMenuItem menuItems[];

    public Main() {
        int inits[][] = new int[6][7];
        for (int a = 0; a < 6; a++) {
            for (int b = 0; b < 7; b++) {
                inits[a][b] = 0;;
            }
        }
        inits[5][3] = 1;
        game = new GameState(inits, false);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new javax.swing.Timer(40, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(20, 20,20));
        g2.fillRect(60, 35, 680, 590);
        g2.setColor(UIManager.getColor("Panel.background"));
        for (int a = 90, b = 0; b < 7; a += 90, b++) {
            for (int c = 65, d = 0; d < 6; c += 90, d++) {
                if (game.board[d][b] == 1)
                    g2.setColor(new Color(73, 115, 183));
                else if (game.board[d][b] == 2)
                    g2.setColor(new Color(183,73, 73));
                g2.fillOval(a, c, 80, 80);
                g2.setColor(UIManager.getColor("Panel.background"));
            }
        }
        if (game.maxPlayer)
            g2.setColor(new Color(73, 115, 183, 60));
        else
            g2.setColor(new Color(183, 73, 73, 60));
        g2.fillOval(x, y, 80, 80);
    }

    public static void init() {
        menuBar = new JMenuBar();
        menus = new JMenu[2];
        menuItems = new JMenuItem[4];
        menus[0] = new JMenu("        File        ");
        menus[1] = new JMenu("        Help        ");
        menuItems[0] = new JMenuItem("        New Game       ");
        menuItems[1] = new JMenuItem("        How to Play        ");
        menuItems[2] = new JMenuItem("        About       ");
        menuItems[3] = new JMenuItem("        Exit       ");
        menus[0].add(menuItems[0]);
        menus[0].add(new JSeparator());
        menus[0].add(menuItems[3]);
        menus[1].add(menuItems[1]);
        menus[1].add(new JSeparator());
        menus[1].add(menuItems[2]);
        menuBar.add(menus[0]);
        menuBar.add(menus[1]);
        menuItems[0].addActionListener(e -> {
            timer.stop();
            int inits[][] = new int[6][7];
            for (int a = 0; a < 6; a++) {
                for (int b = 0; b < 7; b++) {
                    inits[a][b] = 0;;
                }
            }
            inits[5][3] = 1;
            game = new GameState(inits, false);
            timer.start();
        });
        menuItems[1].addActionListener(e -> JOptionPane.showMessageDialog(null, "Select a column using arrow keys and press enter to insert a new checker." +
                "\nTo win you must place 4 checkers in an row, horizontally, vertically or diagonally."));
        menuItems[2].addActionListener(e -> JOptionPane.showMessageDialog(null, "© Created by Sourish Banerjee"));
        menuItems[3].addActionListener(e -> System.exit(0));
    }

    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {}

        init();

        JFrame jf = new JFrame("Connect Four");
        Main obj = new Main();
        jf.setJMenuBar(menuBar);
        jf.setSize(800, 700);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.add(obj);
        jf.setVisible(true);
    }

    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == e.VK_LEFT) {
            if (x > 90) {
                x -= 90;
            }
        }
        if (e.getKeyCode() == e.VK_RIGHT) {
            if (x < 630)
                x += 90;
        }
        if (e.getKeyCode() == e.VK_ENTER) {
            if (game.maxPlayer == false) {
                int allowed = game.addDisc((x / 90) - 1);
                if(allowed == -1)
                    return;

                Computation comp = new Computation();
                comp.refresh();

                if(comp.winCheck(game) == 2) {
                    JOptionPane.showMessageDialog(null, "Red Wins!!");
                    timer.stop();
                    return;
                } else if(comp.boardFilled(game)) {
                    JOptionPane.showMessageDialog(null, "It's a Draw!!");
                    timer.stop();
                    return;
                }

                int[] bestMove = comp.minimax(game, 6, Integer.MIN_VALUE, Integer.MAX_VALUE);
                System.out.println(bestMove[0] + " " + bestMove[1]);
                game.addDisc(bestMove[1]);

                if (comp.winCheck(game) == 1) {
                    JOptionPane.showMessageDialog(null, "Blue Wins!!");
                    timer.stop();
                } else if(comp.boardFilled(game)) {
                    JOptionPane.showMessageDialog(null, "It's a Draw!!");
                    timer.stop();
                    return;
                }
            }
        }
    }
    public void keyTyped(KeyEvent e) {}
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}