package ai;

import java.util.ArrayList;

import ai.budget_manager.GradeCalculator;
import ai.budget_manager.PriceCalculator;
import ai.dda.DdaManager;
import ai.utils.Constants;
import models.creatures.*;
import models.jeu.*;

public class WaveGenerator {

	private PriceCalculator pc;
	private Jeu gameSession;
	// Package protected field
	private static double budget = 0;

	public WaveGenerator(Jeu gameSession, GradeCalculator gradeCalculator){
		// Initializing the A.I components
		pc = new PriceCalculator(gradeCalculator);
		this.gameSession = gameSession;
		
	}
	/**
	 * Generates a wave based on the price calculation algorithm
	 *  and sends a boss each 10 waves
	 * @return the wave
	 * @throws Exception
	 */
	public ArrayList<Creature> generate() throws Exception{
		ArrayList<Creature> wave = new ArrayList<Creature>();
		
		int waveNumber = gameSession.getNumVagueCourante();
		
		if(waveNumber % 10 == 0){
			GrandeAraignee boss = new GrandeAraignee();
			boss.setSante(Constants.BOSS * waveNumber / 10);
			wave.add(boss);
		}
		
		else{
			budget += DdaManager.budgetPerWave() * waveNumber;
			wave = pc.compute(gameSession.getTowersIterator(), waveNumber);
		}
		
		return wave;
	}
	
	public static double getBudget(){
		return budget;
	}
	
	public static void setBudget(double newBudget){
		budget = newBudget;
	}

}
