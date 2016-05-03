package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import models.creatures.Aigle;
import models.creatures.Araignee;
import models.creatures.Creature;
import models.creatures.Elephant;
import models.creatures.Mouton;
import models.creatures.MoutonNoir;
import models.creatures.Paysan;
import models.creatures.Pigeon;
import models.creatures.Rhinoceros;

public class Menu {

	/**
	 * This class holds a list of all creatures and maps the creatures to their prices
	 */
	private ArrayList<Creature> creatures = new ArrayList<Creature>();
	private HashMap<String, Price> prices = new HashMap<String, Price>();

	public Menu(){
		creatures.add(new Aigle());
		creatures.add(new Araignee());
		creatures.add(new Elephant());
		creatures.add(new Mouton());
		creatures.add(new MoutonNoir());
		creatures.add(new Paysan());
		creatures.add(new Pigeon());
		creatures.add(new Rhinoceros());
		for(Creature c : creatures){
			prices.put(c.getNom(), new Price(c));
		}
	}

	public Iterator<Creature> getIter(){
		return creatures.iterator();
	}

	public Price getPrice(String creatureName){
		return prices.get(creatureName);
	}

}
