package ai;

import models.creatures.Creature;

public class Price {
	
	private Creature creature;
	private double price;
	private boolean wasChosen;
	private final double AIR_ADDITION = 20;
	private final double upgradeFactor = 1.5;
	
	public Price(Creature creature){
		this.creature=creature;
		wasChosen = false;
		price = creature.getSanteMax() + creature.getVitesseNormale();
		if(creature.getType() == Creature.TYPE_AERIENNE){
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
	
	public void upgrade(){
		creature.upgrade();
		price *= upgradeFactor;
	}
	
	public double getUpgradePrice(){
		return price * upgradeFactor;
	}
}
