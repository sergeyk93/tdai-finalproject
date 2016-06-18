package ai.budget_manager;

import models.creatures.Creature;

public class Price {

	private Creature creature;
	private double price;
	private final double AIR_ADDITION = 20;

	public Price(Creature creature){
		this.creature=creature;
		computePrice();
	}

	public Creature getCreature(){
		return creature;
	}

	public void computePrice(){
		// The price is a function of max HP and speed
		price = (creature.getSanteMax() + creature.getVitesseNormale()) * 2;
		if(creature.getType() == Creature.TYPE_AERIENNE){
			price += AIR_ADDITION;
		}
	}

	public double getPrice(){
		computePrice();
		return price;
	}

	public double getUpgradePrice(double upgradeFactor){
		return price * upgradeFactor;
	}
}
