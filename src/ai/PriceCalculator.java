package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import models.creatures.Creature;
import models.jeu.Jeu;
import models.tours.Tour;

public class PriceCalculator {

	private ArrayList<Creature> previousWave;
	private HashMap<String, LinkedList<Long>> timeAliveTable;
	private GradeCalculator gradeCalculator;
	private Menu menu;

	public PriceCalculator(){
		menu = new Menu();
		gradeCalculator = new GradeCalculator();
		previousWave = new ArrayList<Creature>();
		timeAliveTable = new HashMap<String, LinkedList<Long>>();
		Iterator<Creature> iter = menu.getIter();
		while(iter.hasNext()){
			Creature c = iter.next();
			timeAliveTable.put(c.getNom(), new LinkedList<Long>());
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
		/*for(Creature c : previousWave){
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

			// Updating the grades based on the towers that were
			// built in the previous wave
			iter = menu.getIter();
			while(iter.hasNext()){
				gradeCalculator.updateGradeTowers(iter.next().getNom(), groundTowers, airTowers);
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
		AILogger.info(log.toString());
		
		ArrayList<String> names = new ArrayList<String>();
		
		// Takes the 15 best creatures it can(or less if it can't)
		while(waveSize <= 15){
			Creature c = gradeCalculator.getBestCreature();
			double price = menu.getPrice(c.getNom()).getPrice();
			if(price <= budget){
				budget -= price; 
				ans.add(c);
				names.add(c.getNom());
			}
			else{
				// TODO Choose a cheaper creature
				break;
			}
			waveSize++;
		}
		
		// Adding the chosen wave to the prevWave of the grade calculator
		gradeCalculator.addCreaturesToWave(names);

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

		// Initializing the previous wave array and appending the wave to it
		previousWave = new ArrayList<Creature>();

		log = new StringBuilder();
		log.append(System.lineSeparator());
		log.append("The chosen wave is: ");
		log.append(System.lineSeparator());

		for (Creature c : ans){
			log.append(c.getNom());
			log.append(System.lineSeparator());

			previousWave.add(c);
		}

		log.append(System.lineSeparator());
		log.append("After budget: ");
		log.append(budget);
		AILogger.info(log.toString());
		return ans;
	}
}
