import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class Zombie {
	static Image zombieImg = Toolkit.getDefaultToolkit().getImage("zombie.png");
	static Image zombieDying1Img = Toolkit.getDefaultToolkit().getImage("zombieDying1.png");
	static Image zombieDead = Toolkit.getDefaultToolkit().getImage("zombieDead.png");
	public int x;
	int hp = 100;
	double xd;
	public int y;
	int width = 29;
	int height = 86;
	State state = State.ALIVE;
	int speed = 1;
	public ArrayList<Arrow> arrowsIn = new ArrayList<Arrow>();
	
	public enum State {ALIVE, DYING1, DEAD};
// head height = 21
	public static enum BodyParts{HEAD, BODY};
	
	static public class Head {
		static public int width = 29;
		static public int height = 20;
		static public int getHeight() {
			return height;
		}
	}
	
	Zombie(int x,int y){
		this.x = x;
		this.y = y;
	}

	
	public void move(double elapsedTime, double g) {
		if (state != State.DEAD) {
			x -= speed;
		}
	}
	
	public Image getImage() {
		switch(state) {
			case ALIVE:
				return zombieImg; 
			case DYING1:
				return zombieDying1Img;
			case DEAD:
				return zombieDead;
			default:
				return zombieImg;
				
		}
	}
}
