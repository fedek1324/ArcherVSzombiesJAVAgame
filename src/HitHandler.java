import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;


public class HitHandler implements ActionListener {
	Timer timer;
	Arrow arrowTrue;
	Zombie zombieTrue;
	Arrow arrow;
	Zombie zombie;
	
	Arrow getTrue(Arrow arrow) {
		int arrowIndex = GameField.arrows.indexOf(arrow);
		if (arrowIndex != -1) // иначе пытается обработать стрелу которой уже нет
			arrowTrue = GameField.arrows.get(arrowIndex);
		return arrowTrue;
	}
	
	Zombie getTrue(Zombie zombie) {
		int zombieIndex = GameField.zombies.indexOf(zombie);
		if (zombieIndex != -1)
			zombieTrue = GameField.zombies.get(zombieIndex);
		return zombieTrue;
	}
	
	private boolean laysIn(int value, int leftBorder, int rightBorder){
		return (value >= leftBorder && value <= rightBorder);
	}
	
	public boolean checkHit(Arrow arrow, Zombie zombie){
		int xArrowEnd = arrow.x + arrow.width;
		int yArrowUp = arrow.y;
		int yArrowBottom = arrow.y - arrow.height;
		int xZombieStart = zombie.x;
		int xZombieEnd = zombie.x + zombie.width;
		int yZombieUp = zombie.y;
		int yZombieBottom = zombie.y + zombie.height;
		return (laysIn(xArrowEnd, xZombieStart, xZombieEnd) 
				&& (laysIn(yArrowBottom, yZombieUp, yZombieBottom) 
						|| laysIn(yArrowUp, yZombieUp, yZombieBottom)));
	}
	
	public Zombie.BodyParts hitWhere(Arrow arrow, Zombie zombie) {
		int yArrowUp = arrow.y;
		int yArrowBottom = arrow.y - arrow.height;
		if (laysIn(yArrowUp, zombie.y, zombie.y + Zombie.Head.getHeight()) ||
				laysIn(yArrowBottom, zombie.y, zombie.y + Zombie.Head.getHeight())) {
			return Zombie.BodyParts.HEAD;
		}
		else 
			return Zombie.BodyParts.BODY;
	}
	
	public void handle(Zombie zombie, Arrow arrow) {
		if (arrow.state == Arrow.State.INZOMBIE || arrow.state == Arrow.State.UNVISIBLE
				|| zombie.state == Zombie.State.DEAD || !checkHit(arrow, zombie))
			return;
		
		Zombie.BodyParts zombiePart = hitWhere(arrow,zombie);
		
		this.zombie = zombie;
		this.arrow = arrow;
		
		arrowTrue = getTrue(arrow);
		zombieTrue = getTrue(zombie);
		
		zombieTrue.arrowsIn.add(arrowTrue);
		if (zombiePart == Zombie.BodyParts.HEAD || zombie.hp <= 50) {
			Archer.score += 200;
			arrowTrue.zombieSpeed = zombie.speed;
			arrowTrue.state = Arrow.State.INZOMBIE;
			zombieTrue.state = Zombie.State.DYING1;
			timer = new Timer(300,this);
	        timer.start();
		}
		else {
			Archer.score += 50;
			zombie.hp -= 50;
			arrowTrue.zombieSpeed = zombie.speed;
			arrowTrue.state = Arrow.State.INZOMBIE;
			zombieTrue.state = Zombie.State.DYING1;
		}
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		timer.restart();
		for (Arrow arrow : zombie.arrowsIn) {
			arrowTrue = getTrue(arrow);
			arrowTrue.state = Arrow.State.UNVISIBLE;
		}
		
		zombieTrue = getTrue(zombie);
		zombieTrue.state = Zombie.State.DEAD;
	}
}
