import game.component.PanelGame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {
    private PanelGame panelGame;

    public Main() {
        init();
    }

    private void init() {
        setTitle("Let The War Begin!");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());  // divides window to 5 regions center east west..
                   // why divide...
        panelGame = new PanelGame(); // creates instance for ..
        add(panelGame); // adds to jframe

// helper classes

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                panelGame.start();
            }
        });


        panelGame.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("R"), "restart");


        panelGame.getActionMap().put("restart", new AbstractAction() { //create an action ,restart
           // links action name restart with actual behavior
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                panelGame = new PanelGame();
                add(panelGame);
                revalidate();  // refresh layout
                panelGame.start();
            }
        });
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Main main = new Main();
            main.setVisible(true);
        });
    }
}