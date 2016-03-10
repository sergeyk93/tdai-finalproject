package ai;

import java.util.ArrayList;

import models.creatures.*;
import models.jeu.Jeu;

public class PriceCalculator {
	
	private ArrayList<Price> priceList;
	
	public PriceCalculator(){
		priceList=new ArrayList<Price>();
		priceList.add(new Price(new Mouton()));
		priceList.add(new Price(new MoutonNoir()));
		priceList.add(new Price(new Aigle()));
		priceList.add(new Price(new Rhinoceros()));
		priceList.add(new Price(new Araignee()));
		priceList.add(new Price(new Paysan()));
		priceList.add(new Price(new Elephant()));
		priceList.add(new Price(new Pigeon()));
	}
	
	public void upgrade(){
		for (Price p : priceList){
			p.upgrade();
		}
	}
	
	public ArrayList<Creature> compute(){
		ArrayList<Creature> ans = new ArrayList<Creature>();
		boolean allChosen = false;
		do{
			double sum = 0;
			double budget = Jeu.getWallet();
			Price p = null;
			do{
				int rand = (int)(Math.random()*priceList.size());
				p = priceList.get(rand);
			}while (p.isChosen());
			double price = p.getPrice();
			while (budget - price > 0 && sum <= 800){
				ans.add(p.getCreature());
				sum += price;
				budget -= price;
			}
			p.choose();
			allChosen = true;
			for (Price pp : priceList)
				if (pp.isChosen() == false){
					allChosen = false;
					break;
				}
			Jeu.setWallet(Jeu.getWallet() - sum);
		}while (Jeu.getWallet() > 0 && allChosen == false);
		for (Price p : priceList)
			p.unChoose();
		return ans;
	}
}
