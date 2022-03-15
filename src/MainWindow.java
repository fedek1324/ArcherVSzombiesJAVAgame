
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * MainWindow
 */
public class MainWindow extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public MainWindow() {
        setTitle("Game");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(820,345);
        setLocation(300, 300);
        GameField gameField = new GameField();
        setContentPane(gameField);
        
//        JPanel gameOverP = new JPanel();
//		gameOverP.setLayout(new BoxLayout(gameOverP, BoxLayout.Y_AXIS));
//		gameField.add(gameOverP);
        
        //add(gameField);
        //setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
    }
    
}
