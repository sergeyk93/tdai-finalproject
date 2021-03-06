package ai.pathfinding;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import models.jeu.Jeu;
import models.tours.Tour;

public class Blocker {

	public int length;
	public int threshold;
	public Point2D p;
	public Point2D d;
	public int width;
	public int height;
	private Tour tower;

	public Blocker(int length,
			int threshold,
			Point2D p,
			Point2D d,
			int width,
			int height,
			Tour tower){

		this.length = length;
		this.threshold = threshold;
		this.p = p;
		this.d = d;
		this.width = width;
		this.height = height;
		this.tower = tower; 
	}

	public boolean isTower(int x, int y){
		return x == tower.getXi() && y == tower.getYi();
	}

	public Blocker getNeighbourBlocker(){
		int newX = (int)p.getX() + width * (int)d.getX();
		int newY = (int)p.getY() + height * (int)d.getY();
		Point2D newP = new Point2D(newX, newY); 
		Jeu.addPoint(newP);
		return new Blocker(length - threshold,
				threshold,
				newP,
				d,
				width,
				height,
				tower);
	}
	
	public ArrayList<Blocker> getNeighbourBlockers(){
		
		ArrayList<Blocker> blockers = new ArrayList<Blocker>();
		
		for (int i = -1; i <= 1; i++)
			for (int j = -1; j <= 1; j++){
				if(i == 0 && j == 0)
					continue;
				int newX = (int)p.getX() + width * i;
				int newY = (int)p.getY() + height * j;
				Point2D newP = new Point2D(newX, newY);
				int hypotenuse = (int)Math.sqrt(Math.pow(width,2)
						+ Math.pow(height,2));
				int newLength = diagonal(i,j) ? length : length ;
				int newThreshold = diagonal(i,j) ? hypotenuse : threshold;
				if (!isTower(newX,newY) && !Jeu.isPointExist(newP)){
					Jeu.addPoint(newP);
					blockers.add(new Blocker(newLength,
							newThreshold,
							newP,
							new Point2D(i,j),
							width,
							height,
							tower));
				}
			}
		return blockers;
	}
	
	private boolean diagonal(int i, int j){
		return (i == -1 && j == -1) || 
		(i == -1 && j == 1) ||
		(i == 1 && j == -1) ||
		(i == 1 && j == 1);
	}
	
	public TowerNeighbour getTN(){
		return new TowerNeighbour((int)p.getX(), (int)p.getY(), width, height, tower.getPrioprietaire());
	}

	public boolean hasLength(){
		return length >= threshold;
	}
}
