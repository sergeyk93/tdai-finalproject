package ai;

import java.util.ArrayList;
import java.util.Iterator;

import ai.budget_manager.GradeCalculator;
import ai.budget_manager.Menu;
import ai.budget_manager.PriceCalculator;
import ai.budget_manager.TimeAliveTable;
import ai.dda.DdaManager;
import ai.utils.Constants;
import models.creatures.Creature;
import models.creatures.GrandeAraignee;
import models.jeu.Jeu;
import models.tours.Tour;

public class WaveGenerator {

	private PriceCalculator pc;
	private GradeCalculator gradeCalculator;
	private Jeu gameSession;
	private static double budget = 0;

	public WaveGenerator(Jeu gameSession){
		// Initializing the A.I components
		this.gameSession = gameSession;
		DdaManager.init(gameSession);
		Menu.init();
        TimeAliveTable.init();
		gradeCalculator = new GradeCalculator();
		pc = new PriceCalculator(gradeCalculator);
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

		//Calculating the number of air towers and ground towers
		int groundTowers = 0, airTowers = 0;
		Iterator<Tour> towerIter = gameSession.getTowersIterator();
		while(towerIter.hasNext()){
			Tour t = towerIter.next();
			if(t.getType() == Tour.TYPE_TERRESTRE){
				groundTowers++;
			}
			if(t.getType() == Tour.TYPE_AIR){
				airTowers++;
			}
		}

		DdaManager.updateTowers();
		
		if (waveNumber == 30){
			GrandeAraignee boss = new GrandeAraignee();
			boss.setSanteMax(Constants.BOSS * waveNumber / 10);
			boss.setVitesse(Constants.NORMAL);
			for (int i = 0; i < 5; i++){
				wave.add(boss.copier());
			}
			return wave;
		}
		
		if(waveNumber % 10 == 0){
			GrandeAraignee boss = new GrandeAraignee();
			boss.setSanteMax(Constants.BOSS * waveNumber / 10);
			wave.add(boss);
		}

		else{
			budget += DdaManager.budgetPerWave() * (waveNumber / 1.5);
			wave = pc.compute(groundTowers, airTowers, waveNumber);
		}

		return wave;
	}

	public static double getBudget(){
		return budget;
	}

	public static void setBudget(double newBudget){
		budget = newBudget;
	}
	
	public GradeCalculator getGradeCalculator(){
		return gradeCalculator;
	}

}
