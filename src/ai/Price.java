package ai;

import models.creatures.Aigle;
import models.creatures.Creature;
import models.creatures.Pigeon;
import models.jeu.Jeu;

public class Price {
	
	private Creature creature;
	private double price;
	private boolean wasChosen;
	private final double AIR_ADDITION = 20;
	
	public Price(Creature creature){
		this.creature=creature;
		wasChosen = false;
		price = creature.getSanteMax() + creature.getVitesseNormale();
		if(creature instanceof Aigle || creature instanceof Pigeon){
			price += AIR_ADDITION;
		}
	}
	
	public Creature getCreature(){
		return creature;
	}
	
	public double getPrice(){
		return price;
	}
	
	public boolean equals(Creature other){
		return creature.getClass().equals(other.getClass());
	}
	
	public boolean isChosen(){
		return wasChosen;
	}
	
	public void choose(){
		wasChosen = true;
	}
	
	public void unChoose(){
		wasChosen = false;
	}

	public void upgrade() {
		creature = creature.upgrade(2*Jeu.getUpgrade());
		price += price;
	}
}
