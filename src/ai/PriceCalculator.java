package ai;

import java.util.ArrayList;
import java.util.Iterator;

import models.creatures.Creature;
import models.jeu.Jeu;
import models.tours.Tour;

public class PriceCalculator {

	private ArrayList<Creature> previousWave;

	public PriceCalculator(){
		Menu.init();
		GradeCalculator.init();
		previousWave = new ArrayList<Creature>();
	}

	/**
	 * 
	 * @return the best creature based on the current bought creatures
	 */
	private Creature getBestCreature(){
		Creature bestCreature = null;
		double maxGrade = Double.MIN_VALUE;
		Iterator<Creature> iter = Menu.getIter();

		while(iter.hasNext()){
			Creature c = iter.next().copier();
			double grade = GradeCalculator.getGrade(c.getNom());

			if(grade > maxGrade){
				maxGrade = grade;
				bestCreature = c;
			}

			// Randomizing if 2 creatures have the same grade
			else if(grade == maxGrade){
				int rand = (int)(Math.random() + 0.5);
				bestCreature = rand == 0 ? bestCreature : c;
			}
		}
		return bestCreature;
	}

	/**
	 * 
	 * @param gameSession
	 * @return computes the prices of the wave the ai buys and updates the wallet
	 */
	public ArrayList<Creature> compute(Jeu gameSession){
		ArrayList<Creature> ans = new ArrayList<Creature>();

		int waveSize = 0;
		double budget = gameSession.getWallet();

		// Updating the grades based on the time that each creature was alive
		// in the previous wave
		for(Creature c : previousWave){
			GradeCalculator.updateGradeDistance(c.getNom(), c.timeAlive());
		}
		
		//Calcuting the number of air towers and ground towers
		Iterator<Tour> towerIter = gameSession.getTowersIterator();
		int groundTowers = 0, airTowers = 0;
		while(towerIter.hasNext()){
			Tour t = towerIter.next();
			if(t.getType() == Tour.TYPE_TERRESTRE){
				groundTowers++;
			}
			else if(t.getType() == Tour.TYPE_AIR){
				airTowers++;
			}
		}
		System.out.println("Ground Towers: " + groundTowers + ", Air Towers: " + airTowers);

		// Updating the grades based on the towers that were
		// built in the previous wave
		Iterator<Creature> iter = Menu.getIter();
		System.out.println("******************************");
		while(iter.hasNext()){
			Creature c = iter.next();
			GradeCalculator.updateGradeTowers(c.getNom(), groundTowers, airTowers);
			System.out.println(c.getNom() + " grade: " + GradeCalculator.getGrade(c.getNom()));
		}

		// Takes the 15 best creatures it can(or less if it can't)
		while(budget > 0 && waveSize <= 15){
			Creature c = getBestCreature();
			ans.add(c);
			budget -= Menu.getPrice(c.getNom()).getPrice();
			waveSize++;
		}

		// Upgrades the creatures deterministically - could be changed to something 
		// more sophisticated. Stops when the price hasn't been changed for 1 iteration
		double upgradeFactor = 1.1;
		while(true){
			double prevBudget = budget;
			for(Creature c : ans){
				Price p = Menu.getPrice(c.getNom());
				double newPrice = p.getUpgradePrice(upgradeFactor);
				if(newPrice <= budget){
					budget += p.getPrice() - newPrice;
					c.upgrade();
				}
			}
			if(prevBudget == budget){
				break;
			}
			upgradeFactor *= 1.1;
		}

		gameSession.setWallet(budget);
		previousWave = ans;
		return ans;
	}
}
