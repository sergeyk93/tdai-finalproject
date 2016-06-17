package ai.pathfinding;

import java.awt.Rectangle;

import models.joueurs.Joueur;

public class TowerNeighbour extends Rectangle{

	private static final long serialVersionUID = 1L;
	private Joueur proprietaire;
	private RectGraphics r;
	private boolean bIsAlive;
	
	public TowerNeighbour(int x, int y, int width, int height, Joueur proprietaire){
		super(x,y,width,height);
		this.proprietaire = proprietaire;
		r = new RectGraphics(x, y, width, height);
		bIsAlive = true;
	}
	
	public RectGraphics getRect(){
		return r;
	}

	public Joueur getPrioprietaire() {
		return proprietaire;
	}

	public boolean isAlive() {
		return bIsAlive;
	}

	public void killTowerNeighbour() {
		bIsAlive = false;
	}

}
