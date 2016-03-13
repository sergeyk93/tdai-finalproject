package ai;

import java.util.ArrayList;
import java.util.HashMap;

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

	private HashMap<String, Price> priceList;
	private ArrayList<Creature> previousWave;
	private double minPrice;
	// Upgrade factor - probably will be changed later
	private static final double upgradeFactor = 2;
	public PriceCalculator(){
		priceList=new HashMap<String, Price>();
		priceList.put(Constants.MOUTON, new Price(new Mouton()));
		priceList.put(Constants.MOUTON_NOIR, new Price(new MoutonNoir()));
		priceList.put(Constants.AIGLE, new Price(new Aigle()));
		priceList.put(Constants.RHINOCEROS, new Price(new Rhinoceros()));
		priceList.put(Constants.ARAIGNEE, new Price(new Araignee()));
		priceList.put(Constants.PAYSAN, new Price(new Paysan()));
		priceList.put(Constants.ELEPHAN, new Price(new Elephant()));
		priceList.put(Constants.PIGEONN, new Price(new Pigeon()));
		minPrice = Double.MAX_VALUE;
		for(String c : priceList.keySet()){
			double currentPrice = priceList.get(c).getPrice();
			if(currentPrice <= minPrice){
				minPrice = currentPrice;
			}
		}
		previousWave = new ArrayList<Creature>();
	}

	private Price getBestCreature(){
		Creature bestCreature = null;
		double maxGrade = Double.MIN_VALUE;
		for(Creature c : previousWave){
			double grade = (new Grade(c)).getGrade();
			if(grade > maxGrade){
				maxGrade = grade;
				bestCreature = c;
			}
			else if(grade == maxGrade){
				int rand = (int)(Math.random() + 0.5);
				bestCreature = rand == 0 ? bestCreature : c;
			}
		}
		if(bestCreature == null){
			return (Price) priceList.values().toArray()[(int)(Math.random()*priceList.size())];
		}
		return priceList.get(bestCreature.getNom());
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
		previousWave = ans;
		return ans;
	}
}
