package ai;

import models.creatures.Creature;

public class Price {
	
	private Creature creature;
	private double price;
	private final double AIR_ADDITION = 20;
	
	public Price(Creature creature){
		this.creature=creature;
		// The price is a function of max HP and speed
		price = creature.getSanteMax() + creature.getVitesseNormale() * 1.5;
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
	
	public double getUpgradePrice(double upgradeFactor){
		return price * upgradeFactor;
	}
}
