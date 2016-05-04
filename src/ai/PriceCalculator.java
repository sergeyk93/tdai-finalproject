package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import models.creatures.Creature;
import models.jeu.Jeu;
import models.tours.Tour;

public class PriceCalculator {

	private HashMap<String, Integer> chooseCounter;
	private HashMap<String, LinkedList<Long>> timeAliveTable;
	private GradeCalculator gradeCalculator;
	private Menu menu;

	public PriceCalculator(){
		menu = new Menu();
		gradeCalculator = new GradeCalculator();

		chooseCounter = new HashMap<String, Integer>();

		timeAliveTable = new HashMap<String, LinkedList<Long>>();

		Iterator<Creature> iter = menu.getIter();
		while(iter.hasNext()){
			Creature c = iter.next();
			timeAliveTable.put(c.getNom(), new LinkedList<Long>());
			chooseCounter.put(c.getNom(), 0);
		}
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
		Iterator<Creature> iter = menu.getIter();
		// Adding the time that creature c was alive for computing the average alive time later
		/*for(Creature c : chooseCounter){
			timeAliveTable.get(c.getNom()).push(c.timeAlive());
		}

		// Updating the grade of a creature by the time it was alive
		// If the creature wasn't chosen we increase its' grade
		Iterator<Creature> iter = menu.getIter();
		if(Jeu.getNumVagueCourante() > 1){
			while(iter.hasNext()){
				String name = iter.next().getNom();
				LinkedList<Long> aliveTimes = timeAliveTable.get(name);
				double avg = 0;
				int size = aliveTimes.size();
				while(!aliveTimes.isEmpty()){
					avg += aliveTimes.pop();
				}
				avg /= size;
				gradeCalculator.updateGradeDistance(name, avg);
			}
		}*/

		//Calculating the number of air towers and ground towers
		if(Jeu.getNumVagueCourante() > 1){
			Iterator<Tour> towerIter = gameSession.getTowersIterator();
			int groundTowers = 0, airTowers = 0;
			while(towerIter.hasNext()){
				Tour t = towerIter.next();
				if(t.getType() == Tour.TYPE_TERRESTRE){
					groundTowers++;
				}
				if(t.getType() == Tour.TYPE_AIR){
					airTowers++;
				}
			}

			// 1. Updating the grades based on the towers that were built in the previous wave
			// 2.
			// 3. Increasing the grade of the creatures that weren't chosen
			iter = menu.getIter();
			while(iter.hasNext()){
				String creatureName = iter.next().getNom();
				// TODO: Change 3 to the DDA value of the previous wave consideration
				if(chooseCounter.get(creatureName)>=3){
					gradeCalculator.incGrade(creatureName);
				}
				gradeCalculator.updateGradeTowers(creatureName, groundTowers, airTowers);
			}
		}

		// Initializing wave logger
		StringBuilder log = new StringBuilder();
		log.append(System.lineSeparator());
		log.append("----------- Wave");
		log.append(" #");
		log.append(Jeu.getNumVagueCourante());
		log.append(" ");
		log.append("creature grades: -----------");
		log.append(System.lineSeparator());
		log.append("Current budget: ");
		log.append(budget);
		log.append(System.lineSeparator());
		log.append(System.lineSeparator());

		// Logging the grades of the creatures
		iter = menu.getIter();
		while(iter.hasNext()){
			Creature c = iter.next();
			log.append(c.getNom());
			log.append("[");
			log.append("HP: ");
			log.append(c.getSanteMax());
			log.append(", Speed: ");
			log.append(c.getVitesseNormale());
			log.append("]");
			log.append(System.lineSeparator());
			log.append("Grade: ");
			log.append(gradeCalculator.getGrade(c.getNom()));
			log.append(System.lineSeparator());
			log.append("Price: ");
			log.append(menu.getPrice(c.getNom()).getPrice());
			log.append(System.lineSeparator());
			log.append(System.lineSeparator());
		}

		// Takes the 15 best creatures it can(or less if it can't)
		while(true){
			Creature c = gradeCalculator.getBestCreature(budget, menu);
			
			if(c==null){
				break;
			}
			
			double price = menu.getPrice(c.getNom()).getPrice();
			
			budget -= price; 
			ans.add(c);

			waveSize++;
		}

		// Upgrades the creatures deterministically - could be changed into something 
		// more sophisticated. Stops when the price hasn't been changed for 1 iteration
		double upgradeFactor = 1.1;
		while(true){
			double prevBudget = budget;
			for(Creature c : ans){
				Price p = menu.getPrice(c.getNom());
				double newPrice = p.getUpgradePrice(upgradeFactor);
				if(newPrice <= budget){
					budget = budget + p.getPrice() - newPrice;
					c.upgrade();
				}
			}
			if(prevBudget == budget){
				break;
			}
			upgradeFactor *= 1.1;
		}

		gameSession.setWallet(budget);

		log.append("The chosen wave is: ");
		log.append(System.lineSeparator());

		HashSet<String> previousWave = new HashSet<String>();

		for (Creature c : ans){
			String name = c.getNom();

			log.append(name);
			log.append(System.lineSeparator());

			previousWave.add(name);
			chooseCounter.put(name, 0);
		}

		iter = menu.getIter();

		while(iter.hasNext()){
			String name = iter.next().getNom();
			if(!previousWave.contains(name)){
				chooseCounter.put(name, chooseCounter.get(name) + 1);
			}
		}

		log.append(System.lineSeparator());
		log.append("After budget: ");
		log.append(budget);
		AILogger.info(log.toString());
		return ans;
	}
}
