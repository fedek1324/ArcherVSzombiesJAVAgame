import javax.swing.*;
import javax.swing.Timer;

import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
public class GameField extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	boolean inGame;
	static int maxX = 820 - 17, maxY = 345 - 39;
	int xZero = 0, yZero = maxY - 16 - 20;
	int timeKoef = 9;
	Timer timer;
	double g = 9.8;
	Archer archer; //archer creates arrows
	Zombie zombie1;
	static ArrayList<Zombie> zombies;
	static ArrayList<Arrow> arrows;
	HitHandler hitHandler;
	double zombieSpawnVariety;
	double delta = 5/10000000.0;
	Image backGround = Toolkit.getDefaultToolkit().getImage("FrontYardBlack.png");
	enum GameState {StartMenu, Playing, GameOver};
	GameState gameState;
	public GameField() {
		setBackground(new Color(39,26,71));
		loadImages();
		timer = new Timer(17,this);
		gameState = GameState.StartMenu;
		addMenuUI(this);
		//initGame();
		addKeyListener(new FieldKeyListener());
		
        setFocusable(true);
        setLayout(new GridBagLayout());
        
	}
	void addMenuUI(Container container) {
		JPanel gameMenuPanel = new JPanel();
		gameMenuPanel.setLayout(new BoxLayout(gameMenuPanel, BoxLayout.Y_AXIS));
		gameMenuPanel.setOpaque(false);
		JLabel gameMenuLabel = modifyLabel(new JLabel("Archer VS Zombies"));
		gameMenuLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 35));
		JLabel toStartLabel = new JLabel("Press G to start game.");
		JLabel toExitLabel = new JLabel("Press X to exit.");
		
		gameMenuPanel.add(gameMenuLabel);
		gameMenuPanel.add(modifyLabel(toStartLabel));
		gameMenuPanel.add(modifyLabel(toExitLabel));
		container.add(gameMenuPanel);
		gameMenuPanel.setVisible(false);
		gameMenuPanel.setVisible(true);
	}
	
	void addGameOverUI(int score, int record, int experience, Container container) {
		JPanel gameOverP = new JPanel();
		gameOverP.setLayout(new BoxLayout(gameOverP, BoxLayout.Y_AXIS));
		gameOverP.setOpaque(false);
		JLabel gameOver = modifyLabel(new JLabel("Game Over"));
		gameOver.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		JLabel scoreLabel = new JLabel("Your score is: " + score);
		JLabel recordLabel = new JLabel("Your record is: " + record);
		JLabel experienceLabel = new JLabel("Your experience is: " + experience);
		JLabel pressRLabel = new JLabel("Press R to restart");
		JLabel exitLabel = new JLabel("Press X to exit");
		gameOverP.add(gameOver);
		gameOverP.add(modifyLabel(scoreLabel));
		gameOverP.add(modifyLabel(recordLabel));
		gameOverP.add(modifyLabel(experienceLabel));
		gameOverP.add(modifyLabel(pressRLabel));
		gameOverP.add(modifyLabel(exitLabel));
		container.add(gameOverP);
		gameOverP.setVisible(false);
		gameOverP.setVisible(true);
	}
	
	JLabel modifyLabel(JLabel label) {
//		label.setHorizontalAlignment(JLabel.CENTER);
//		label.setVerticalAlignment(JLabel.CENTER);
		label.setAlignmentX(CENTER_ALIGNMENT);
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		label.setForeground(Color.white);
		label.setOpaque(false);
		return label;
	}
	
	void restart() {
		archer.score = 0;
		archer.inGame = false;
        timer.restart();
        this.remove(0); // delete gameOverPanel
		initGame();
	}
	
	void initGame() {
		hitHandler = new HitHandler();
		zombies = new ArrayList<Zombie>();
		arrows = new ArrayList<Arrow>();
		zombieSpawnVariety = 1/270.0;
		createZombie();
		archer = new Archer(xZero, yZero);
		inGame = true;
		
        addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent e) {
        		archer.shoot(e);
        	}
		});
		timer.start();
	}
	
	void saveGame(int experience) throws IOException, ClassNotFoundException {
		SavedGame save;
		try {
			SavedGame lastSave = getSave();
			save = new SavedGame(Math.max(archer.score, lastSave.score), lastSave.experience + archer.score);
		} catch (IOException e) {
			save = new SavedGame(archer.score, archer.score);
		}
	    FileOutputStream outputStream = new FileOutputStream("save.ser");
	    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
	    objectOutputStream.writeObject(save);
	    objectOutputStream.close();
	}
	
	SavedGame getSave() throws IOException, ClassNotFoundException {
	    FileInputStream fileInputStream = new FileInputStream("save.ser");
	    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
	    SavedGame savedGame = (SavedGame) objectInputStream.readObject();
	    return savedGame;
	}
	
	public void createZombie(){
		int yOffset = (int) (Math.random() * 100);
        zombie1 = new Zombie(800,200 - yOffset);
        zombies.add(zombie1);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
        if(inGame){
        	double rand =  Math.random();
        	zombieSpawnVariety += delta;
        	if (rand < zombieSpawnVariety ) {
        		createZombie();
        	}
        	double elapsedTime = 17/1000.0 * timeKoef;
        	for (Arrow arrow : arrows) {
        		arrow.move(elapsedTime, g);
			}
        	for (Zombie zombie : zombies) {
        		zombie.move(elapsedTime, g);
			}
        	checkHits();
        	
        	for (Zombie zombie : zombies) {
        		if (zombie.x < 95) {
        			inGame = false;
        			gameState = GameState.GameOver;
        			try {
						saveGame(archer.score);
						addGameOverUI(archer.score, getSave().score, getSave().experience, GameField.this);
					} catch (ClassNotFoundException | IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
        		}
        	}
        }
        repaint();
    }
	
	public void checkHits(){
		for (Arrow arrow : arrows) {
			for (Zombie zombie : zombies) {
				hitHandler = new HitHandler();
				hitHandler.handle(zombie, arrow);
			}
		}
	}
	
	public void loadImages(){
    }
	
	class FieldKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
        	super.keyPressed(e);
        	int key = e.getKeyCode();
        	if (gameState == GameState.StartMenu) {
        		if(key == KeyEvent.VK_G){
	            	initGame();
	            	GameField.this.remove(0);
	            	gameState = GameState.Playing;
	            }
        		if(key == KeyEvent.VK_X){
	            	System.exit(1);
	            }
        	}
        	if(gameState == GameState.GameOver) {
        		if(key == KeyEvent.VK_X){
	            	System.exit(1);
	            }
        	}
        	if (gameState != GameState.StartMenu) {
	        	archer.keyPressed(e); 
	            if(key == KeyEvent.VK_Z){
	            	createZombie();
	            }
	            if(key == KeyEvent.VK_R){
	            	if (!inGame)
	            		restart();
	            }
	            if(key == KeyEvent.VK_P){
	            	inGame = !inGame;
	            }
	            if(key == KeyEvent.VK_K){
	            	try {
						saveGame(Archer.score);
					} catch (IOException | ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            }
        	}
        }
    }

	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        g.setColor(Color.WHITE);
        if (inGame) {
        	g.drawImage(backGround, 0, 0, maxX, maxY, this);
        	
	        for (Arrow arrow : arrows) {
	        	if (arrow.state != Arrow.State.UNVISIBLE)
	            	arrow.drawArrow(arrow, g);
			}
	        
	        Double angleS = arrows.get(arrows.size()-1).angleGrads;
	        g.drawString(String.format("%.0f", angleS),maxX/2, maxY/2);
	        g.drawString(String.valueOf(Archer.score),maxX/2 - 200, maxY/2);
	        g.drawString(String.format("%.5f", zombieSpawnVariety),maxX/2 + 200, maxY/2);
	        for (Zombie zombie : zombies) {
	        	g.drawImage(zombie.getImage(), zombie.x, zombie.y, this);
			}
        }
    }
}
