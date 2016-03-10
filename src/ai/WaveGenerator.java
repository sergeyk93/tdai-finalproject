package ai;

import java.util.ArrayList;

import models.creatures.*;
import models.jeu.Jeu;

public class WaveGenerator {
	
	private PriceCalculator pc;
	private Jeu jeu;
	
	public WaveGenerator(Jeu jeu){
		pc = new PriceCalculator();
		this.jeu = jeu;
	}

	public ArrayList<Creature> generate() throws Exception{
		if (Jeu.getNumVagueCourante() / 10 > Jeu.getUpgrade()){
			Jeu.incUpgrade();
			//pc.upgrade();
		}
		return pc.compute();
	}

}
