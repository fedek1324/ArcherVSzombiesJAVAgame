import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

import javax.swing.Timer;


public class Arrow {
	static Image arrowImg = Toolkit.getDefaultToolkit().getImage("arrow.png");
	public int x;
	public int y;
	public int height = 8;
	public int width = 28;
	public double vx;
	public double vy;
	public double angleGrads = 30;
	State state = State.ONSTART;
	int zombieSpeed = 0;
	
	static enum State {ONSTART, MOVES, INZOMBIE, UNVISIBLE};
	
	public void drawArrow(Arrow arrow, Graphics g) {
		int yOffset = 8;
		Graphics2D g2d=(Graphics2D)g;
        AffineTransform backup = g2d.getTransform();
        double angle = -Math.toRadians(arrow.angleGrads); 
        int rx = arrow.x + arrow.width/2, ry=arrow.y + arrow.height/2;
        //rx is the x coordinate for rotation, ry is the y coordinate for rotation, and angle
        //is the angle to rotate the image. If you want to rotate around the center of an image,
        //use the image's center x and y coordinates for rx and ry.
        AffineTransform a = AffineTransform.getRotateInstance(angle, rx, ry);
        //Set our Graphics2D object to the transform
        g2d.setTransform(a);
        //Draw our image like normal
        g2d.drawImage(Arrow.arrowImg, arrow.x, arrow.y - yOffset, null);
        //Reset our graphics object so we can draw with it again.
        g2d.setTransform(backup);
	}
	
	public void move(double elapsedTime, double g) {
		if (state == State.MOVES ) {
			x += (int) Math.round(vx * elapsedTime);
	    	vy += g*elapsedTime;
	    	y += (int) Math.round(vy * elapsedTime);
	    	angleGrads = -(int)Math.toDegrees(Math.atan((double)vy/vx));
		}
		else if (state == State.INZOMBIE) {
			x -= zombieSpeed;
		}
	}
	
	Arrow(int x,int y){
		this.x = x;
		this.y = y;
	}
	
	Arrow(int x,int y, double startAngle){
		this.x = x;
		this.y = y;
		angleGrads = startAngle;
	}

}
