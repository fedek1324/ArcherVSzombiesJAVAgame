import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Timer;

public class Archer implements ActionListener {
	Arrow arrow;
	int x,y;
	public int vZero = 80; // strength
	Timer timer;
	int reloadTime = 1500;
	static public int score = 0;
	double prevArrowGrad = 30;
	boolean inGame = true;
	
	Archer(int x, int y) {
		this.x = x;
		this.y = y;
		arrow = new Arrow(x,y);
		GameField.arrows.add(arrow);
	}
	
	void reload() {
		if (arrow.state != Arrow.State.ONSTART) {
			arrow = new Arrow(x,y, prevArrowGrad);
			GameField.arrows.add(arrow);
		}
	}

    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        
        if(key == KeyEvent.VK_SPACE && arrow.state == Arrow.State.ONSTART){
        	double arrowAngleInRads = Math.toRadians(arrow.angleGrads);
        	arrow.vx = vZero*Math.cos(arrowAngleInRads);
        	arrow.vy = - vZero*Math.sin(arrowAngleInRads);
        	arrow.state = Arrow.State.MOVES;
        	timer = new Timer(reloadTime, this);
        	timer.start();
        	prevArrowGrad = arrow.angleGrads;	
        }
        
        if(key == KeyEvent.VK_U){
        	arrow.vy -= 10;
        }
        if(key == KeyEvent.VK_H){
        	arrow.vx -= 10;
        }
        if(key == KeyEvent.VK_K){
        	arrow.vx += 10;
        }
        
        
        if(arrow.state == Arrow.State.ONSTART && e.isShiftDown() && key == KeyEvent.VK_W){
        	arrow.angleGrads += 10;
        }
        if(arrow.state == Arrow.State.ONSTART && e.isShiftDown() && key == KeyEvent.VK_S){
        	arrow.angleGrads -= 10;
        }
        if(arrow.state == Arrow.State.ONSTART && key == KeyEvent.VK_W){
        	arrow.angleGrads += 2;
        }
        if(arrow.state == Arrow.State.ONSTART && key == KeyEvent.VK_S){
        	arrow.angleGrads -= 2;
        }
    }

	@Override
	public void actionPerformed(ActionEvent a) {
		if (!inGame)
			return;
		timer.restart();
		reload();
		
	}

	public void shoot(MouseEvent e) {
		if (arrow.state != Arrow.State.ONSTART)
			return;
		int x = e.getX() - this.x;
		int y = this.y - e.getY();
		double angleGrads = (int)Math.toDegrees(Math.atan((double)y/x));
		arrow.angleGrads = angleGrads;
    	double arrowAngleInRads = Math.toRadians(angleGrads);
    	arrow.vx = vZero*Math.cos(arrowAngleInRads);
    	arrow.vy = - vZero*Math.sin(arrowAngleInRads);
    	arrow.state = Arrow.State.MOVES;
    	timer = new Timer(reloadTime, this);
    	timer.start();
    	prevArrowGrad = arrow.angleGrads;			
	}
}
