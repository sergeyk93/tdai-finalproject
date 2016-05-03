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

	public ArrayList<Creature> generate() throws Exception{
		return pc.compute(gameSession);
	}

}
