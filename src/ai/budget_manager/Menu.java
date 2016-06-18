package ai.budget_manager;

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
	private static ArrayList<Creature> creatures = new ArrayList<Creature>();
	private static HashMap<String, Price> prices = new HashMap<String, Price>();
	private static double minPrice;

	public static void init(){
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
		
		minPrice = Double.MAX_VALUE;
		for(Creature c : creatures){
			double price = prices.get(c.getNom()).getPrice();
			if(price < minPrice){
				minPrice = price;
			}
		}
	}

	public static Iterator<Creature> getIter(){
		return creatures.iterator();
	}

	public static Price getPrice(String creatureName){
		return prices.get(creatureName);
	}
	
	public static double getMinPrice(){
		return minPrice;
	}

}
