package ai.utils;

import java.util.ArrayList;
import java.util.Random;

import models.creatures.Aigle;
import models.creatures.Araignee;
import models.creatures.Creature;
import models.creatures.Elephant;
import models.creatures.Mouton;
import models.creatures.MoutonNoir;
import models.creatures.Paysan;
import models.creatures.Pigeon;
import models.creatures.Rhinoceros;

public class DefaultWave {

	private ArrayList<Creature> creatures;
	
	public DefaultWave(){
		creatures = new ArrayList<Creature>();
		initArray();
	}

	private void initArray() {
		
		Creature c;
		
		c = new Aigle(Constants.EAGLE, 3, Constants.FAST);
		c.updateData();
		creatures.add(c);
		c = new MoutonNoir(Constants.BLACK_SHEEP, 3, Constants.NORMAL);
		c.updateData();
		creatures.add(c);
		c = new Rhinoceros(Constants.RHINO, 3, Constants.SLOW);
		c.updateData();
		creatures.add(c);
		c = new Paysan(Constants.PEASENT, 3, Constants.FAST);
		c.updateData();
		creatures.add(c);
		c = new Elephant(Constants.ELEPHANT, 3, Constants.SLOW);
		c.updateData();
		creatures.add(c);
		c = new Pigeon(Constants.PIGEON, 3, Constants.FAST);
		c.updateData();
		creatures.add(c);
		c = new Mouton(Constants.SHEEP, 3, Constants.NORMAL);
		c.updateData();
		creatures.add(c);
		c = new Araignee(Constants.SPIDER, 3, Constants.FAST);
		c.updateData();
		creatures.add(c);
		
	}
	
	public ArrayList<Creature> getDefaultWave(){
		ArrayList<Creature> wave = new ArrayList<Creature>();
		int size = 12;
		Random r = new Random();
		
		while (size > 0){
			Creature c = null;
			
			while (c == null){
				int index = r.nextInt(8);
				if (index >=0 && index < 8)
					c = creatures.get(index);
			}
			int q = 0;
			while (q == 0)
				r.nextInt(5);
			
			for (; q>0; q++, size--)
				wave.add(c);
				
		}
		return wave;
	}
}
