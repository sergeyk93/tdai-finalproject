package ai;

import java.util.ArrayList;

import models.creatures.*;
import models.jeu.Jeu;

public class WaveGenerator {
	
	private PriceCalculator pc;
	
	public WaveGenerator(){
		pc = new PriceCalculator();
	}
	
	public ArrayList<Creature> generate() throws Exception{
		if (Jeu.getNumVagueCourante() / 10 > Jeu.getUpgrade()){
			Jeu.incUpgrade();
			pc.upgrade();
		}
		return pc.compute();
	}

}
