package ai;

import java.util.ArrayList;

import models.creatures.*;
import models.jeu.Jeu;

public class WaveGenerator {

	private PriceCalculator pc;
	private Jeu gameSession;

	public WaveGenerator(Jeu gameSession){
		// Initializing the A.I components
		DdaManager.init();
		pc = new PriceCalculator();
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
		
		if(Jeu.getNumVagueCourante() % 10 == 0){
			GrandeAraignee boss = new GrandeAraignee();
			boss.setSante(Constants.BOSS);
			wave.add(boss);
		}
		
		else{
			wave = pc.compute(gameSession);
		}
		
		return wave;
	}

}
