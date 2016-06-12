package ai.pathfinding;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import models.jeu.Jeu;
import models.tours.Tour;

public class Blocker {
	public int length;
	public int threshold;
	public Point2D p;
	public int width;
	public int height;
	private Tour tower;
	
	public Blocker(int length,
				   int threshold,
                   Point2D p,
                   int width,
                   int height,
                   Tour tower){
		
		this.length = length;
		this.threshold = threshold;
		this.p = p;
		this.width = width;
		this.height = height;
		this.tower = tower; 
	}
	
	public boolean isTower(int x, int y){
		return x == tower.getXi() && y == tower.getYi();
	}
	
	public ArrayList<Blocker> getNeighbourBlockers(){
		ArrayList<Blocker> blockers = new ArrayList<Blocker>();
		
		if (isTower((int)p.getX(),(int)p.getY()))
			length += threshold;
		for (int i = -1; i <= 1; i++)
			for (int j = -1; j <= 1; j++){
				if(i == 0 && j == 0)
					continue;
				int newX = (int)p.getX() + width * i;
				int newY = (int)p.getY() + height * j;
				Point2D newP = new Point2D(newX, newY); 
				if (!isTower(newX,newY) && !Jeu.isPointExist(newP)){
					Jeu.addPoint(newP);
					blockers.add(new Blocker(length - threshold,
							threshold,
							newP,
							width,
							height,
							tower));
				}
			}
		return blockers;
	}
	
	public TowerNeighbour getTN(){
		return new TowerNeighbour((int)p.getX(), (int)p.getY(), width, height, tower.getPrioprietaire());
	}
	
	public boolean hasLength(){
		return length >= threshold;
	}
}
