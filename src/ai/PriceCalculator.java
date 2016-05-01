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

	public PriceCalculator(){
		Menu.init();
		GradeCalculator.init();
		DdaManager.init();
		previousWave = new ArrayList<Creature>();

		timeAliveTable = new HashMap<String, LinkedList<Long>>();
		Iterator<Creature> iter = Menu.getIter();
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

		// Adding the time that creature c was alive for computing the average alive time later
		for(Creature c : previousWave){
			timeAliveTable.get(c.getNom()).add(c.timeAlive());
		}

		// Updating the grade of a creature by the time it was alive
		// If the creature wasn't chosen we increase its' grade
		Iterator<Creature> iter = Menu.getIter();
		if(Jeu.getNumVagueCourante() > 1){
			while(iter.hasNext()){
				String name = iter.next().getNom();
				if(timeAliveTable.get(name).isEmpty()){
					GradeCalculator.incGrade(name);
					continue;
				}
				LinkedList<Long> aliveTimes = timeAliveTable.get(name);
				double avg = 0;
				int size = aliveTimes.size();
				while(!aliveTimes.isEmpty()){
					avg += aliveTimes.pop();
				}
				avg /= size;
				GradeCalculator.updateGradeDistance(name, avg);
			}
		}

		//Calculating the number of air towers and ground towers
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
		iter = Menu.getIter();
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
		while(iter.hasNext()){
			Creature c = GradeCalculator.getCreature(iter.next().getNom());
			log.append(c.getNom());
			log.append("[");
			log.append("HP: ");
			log.append(c.getSanteMax());
			log.append(", Speed: ");
			log.append(c.getVitesseNormale());
			log.append("]");
			log.append(System.lineSeparator());
			log.append("Grade: ");
			log.append(GradeCalculator.getGrade(c.getNom()));
			log.append(System.lineSeparator());
			log.append("Price: ");
			log.append(Menu.getPrice(c.getNom()).getPrice());
			log.append(System.lineSeparator());
			log.append(System.lineSeparator());
			
			// The actual update - the chosen creatures are being logged in that iteration
			GradeCalculator.updateGradeTowers(c.getNom(), groundTowers, airTowers);
		}
		AILogger.info(log.toString());
		// Takes the 15 best creatures it can(or less if it can't)
		while(waveSize <= 15){
			Creature c = GradeCalculator.getBestCreature();
			ans.add(c);
			double price = Menu.getPrice(c.getNom()).getPrice();
			if(price <= budget){
				budget -= price; 
			}
			else{
				break;
			}
			waveSize++;
		}

		// Upgrades the creatures deterministically - could be changed into something 
		// more sophisticated. Stops when the price hasn't been changed for 1 iteration
		double upgradeFactor = 1.1;
		while(true){
			double prevBudget = budget;
			for(Creature c : ans){
				Price p = Menu.getPrice(c.getNom());
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

		// Initializing the previous wave array and adding appending the wave to it
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
