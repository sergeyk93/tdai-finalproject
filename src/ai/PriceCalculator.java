package ai;

import java.util.ArrayList;

import models.creatures.Aigle;
import models.creatures.Araignee;
import models.creatures.Creature;
import models.creatures.Elephant;
import models.creatures.Mouton;
import models.creatures.MoutonNoir;
import models.creatures.Paysan;
import models.creatures.Pigeon;
import models.creatures.Rhinoceros;
import models.jeu.Jeu;

public class PriceCalculator {

	private ArrayList<Price> priceList;
	private double minPrice;
	// Upgrade factor - probably will be changed later
	private static final double upgradeFactor = 2;
	
	public PriceCalculator(){
		priceList=new ArrayList<Price>();
		priceList.add(new Price(new Mouton()));
		priceList.add(new Price(new MoutonNoir()));
		priceList.add(new Price(new Aigle()));
		priceList.add(new Price(new Rhinoceros()));
		priceList.add(new Price(new Araignee()));
		priceList.add(new Price(new Paysan()));
		priceList.add(new Price(new Elephant()));
		priceList.add(new Price(new Pigeon()));
		minPrice = Double.MAX_VALUE;
		for(Price p : priceList){
			if(p.getPrice() <= minPrice){
				minPrice = p.getPrice();
			}
		}
	}
	
	private double getGrade(Creature c){
		Grade grade = new Grade(c);
		return grade.getGrade();
	}

	private Price getBestCreature(){
		Price bestCreature = null;
		double maxGrade = Double.MIN_VALUE;
		for(Price p : priceList){
			double grade = getGrade(p.getCreature());
			if(grade > maxGrade){
				maxGrade = grade;
				bestCreature = p;
			}
			else if(grade == maxGrade){
				int rand = (int)(Math.random() + 0.5);
				bestCreature = rand == 0 ? bestCreature : p;
			}
		}

		return bestCreature;
	}

	public ArrayList<Creature> compute(Jeu gameSession){
		ArrayList<Creature> ans = new ArrayList<Creature>();
		ArrayList<Price> prices = new ArrayList<Price>();
		
		int waveSize = 0;
		double budget = gameSession.getWallet();
		
		while(budget > 0 && waveSize <= 15){
			Price p = getBestCreature();
			prices.add(p);
			budget -= p.getPrice();
			waveSize++;
		}
		
		while(budget > minPrice * upgradeFactor){
			double prevBudget = budget;
			for(Price p : prices){
				if(p.getUpgradePrice() < budget){
					budget += p.getPrice() - p.getUpgradePrice();
					p.upgrade();
				}
			}
			if(prevBudget == budget){
				break;
			}
		}
		
		for(Price p : prices){
			ans.add(p.getCreature());
		}
		
		gameSession.setWallet(budget);
		
		return ans;
	}
}
