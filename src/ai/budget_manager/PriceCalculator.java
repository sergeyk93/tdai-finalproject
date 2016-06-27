package ai.budget_manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import models.creatures.Creature;
import ai.WaveGenerator;
import ai.dda.DdaManager;
import ai.utils.AILogger;

public class PriceCalculator {
	
	private GradeCalculator gradeCalculator;
	private HashMap<String, Integer> chooseCounter;
	private ArrayList<Creature> previousWave;
	
	public PriceCalculator(GradeCalculator gg){
		gradeCalculator = gg;
		chooseCounter = new HashMap<String, Integer>();
		previousWave = new ArrayList<Creature>();
		Iterator<Creature> iter = Menu.getIter();
		
		while(iter.hasNext()){
			chooseCounter.put(iter.next().getNom(), 0);
		}
	}

	/**
	 * 
	 * @param gameSession
	 * @return computes the prices of the wave the A.I buys and updates the wallet
	 */
	public ArrayList<Creature> compute(int groundTowers, int airTowers, int waveNumber){
		ArrayList<Creature> ans = new ArrayList<Creature>();
		Iterator<Creature> iter = Menu.getIter();
		double budget = WaveGenerator.getBudget();
		
		// In the first wave we don't compute the grade and uses the first values
		if(waveNumber > 1){
			// 1. Updating the grades based on the towers that were built in the previous wave
			// 2. Updating the grades based on the time that the creature was alive
			// 3. Increasing the grade of the creatures that weren't chosen for n waves
			while(iter.hasNext()){
				String creatureName = iter.next().getNom();
				if(chooseCounter.get(creatureName)>=DdaManager.prevWavesConsideration()){
					gradeCalculator.incGrade(creatureName);
				}
				gradeCalculator.updateGradeTowers(creatureName, groundTowers, airTowers);
				
			}
			
			// Updating the grade based on the last alive time
			for(Creature c : previousWave){
				gradeCalculator.updateGradeDistance(c.getNom(), 
						TimeAliveTable.getAliveTIme(c.getNom()));
			}
			
			// Updating the alive time average of the chosen creatures in the previous wave
			for(Creature c : previousWave){
				long timeAlive = TimeAliveTable.getAliveTIme(c.getNom());
				gradeCalculator.addAliveTime(c.getNom(), 
						timeAlive);
			}
		}

		// Initializing wave logger
		StringBuilder log = new StringBuilder();
		log.append(System.lineSeparator());
		log.append("----------- Wave");
		log.append(" #");
		log.append(waveNumber);
		log.append(" creature grades: -----------");
		log.append(System.lineSeparator());
		log.append("Current budget: ");
		log.append(budget);
		log.append(System.lineSeparator());
		log.append(System.lineSeparator());

		// Logging the grades of the creatures
		iter = Menu.getIter();
		while(iter.hasNext()){
			Creature c = iter.next();
			log.append(c.getNom());
			log.append("[HP: ");
			log.append(c.getSanteMax());
			log.append(", Speed: ");
			log.append(c.getVitesseNormale());
			log.append("]");
			log.append(System.lineSeparator());
			log.append("Grade: ");
			log.append(gradeCalculator.getGrade(c.getNom()));
			log.append(System.lineSeparator());
			log.append("Price: ");
			log.append(Menu.getPrice(c.getNom()));
			log.append(System.lineSeparator());
			log.append("Previous alive time: ");
			log.append(gradeCalculator.getLastAliveTime(c.getNom()));
			log.append(System.lineSeparator());
			log.append(System.lineSeparator());
		}

		// Takes the 15 best creatures it can(or less if it can't)
		int waveSize = 0;
		
		while(waveSize <= DdaManager.getWaveSize()){
			Creature c = gradeCalculator.getBestCreature(budget);
			if(c==null){
				break;
			}
			
			double price = Menu.getPrice(c.getNom());
			budget -= price; 
			ans.add(c);
			waveSize++;
		}

		// Upgrades the creatures deterministically - could be changed into something 
		// more sophisticated. Stops when the price hasn't been changed for 1 iteration
//		double upgradeFactor = 1.1;
//		double prevBudget;
//		do{
//			prevBudget = budget;
//			for(Creature c : ans){
//				Price p = Menu.getPrice(c.getNom());
//				double newPrice = p.getUpgradePrice(upgradeFactor);
//				if(newPrice <= budget){
//					budget -= newPrice;
////					c.upgrade();
//				}
//			}
//			upgradeFactor *= 1.1;
//		} while(budget!=prevBudget);
		
		WaveGenerator.setBudget(budget);

		log.append("The chosen wave is: ");
		log.append(System.lineSeparator());

		previousWave = new ArrayList<Creature>();
		
		for (Creature c : ans){
			String name = c.getNom();

			log.append(name);
			log.append(System.lineSeparator());

			previousWave.add(c);
			chooseCounter.put(name, 0);
		}

		iter = Menu.getIter();

		while(iter.hasNext()){
			String name = iter.next().getNom();
			if(!containName(name)){
				chooseCounter.put(name, chooseCounter.get(name) + 1);
			}
		}

		log.append(System.lineSeparator());
		log.append("After budget: ");
		log.append(budget);
		log.append(System.lineSeparator());
		AILogger.info(log.toString());
		return ans;
	}
	
	private boolean containName(String name){
		for(Creature c : previousWave){
			if(c.getNom().equals(name)){
				return true;
			}
		}
		return false;
	}
}
