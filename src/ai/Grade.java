package ai;

import models.creatures.Creature;

public class Grade {
	Creature c;
	double grade;
	int counter;
	int quantity;
	double avgDistance;
	boolean hasReduced;
	
	public Grade(Creature c){
		this.c = c;
		// First grade value is based on max HP and speed
		grade = (c.getSanteMax() * c.getVitesseNormale()) / (new Price(c).getPrice());
		System.out.println(c.getNom() + " grade: " + grade);
		counter = 1;
		quantity = 0;
		avgDistance = 5000;
		hasReduced = false;
	}
	
	public void gradeTowerType(int airTowers, int groundTowers){
		double temp = grade;
		if (airTowers >= groundTowers){
			grade = c.getType() == Creature.TYPE_AERIENNE ? grade + 1 : grade - 1;
		}
		else{
			grade = c.getType() == Creature.TYPE_TERRIENNE ? grade + 1 : grade - 1;
		}
		if (temp>grade)
			if (hasReduced == true)
				grade = temp;
			else
				hasReduced = true;
		else
			hasReduced = false;
	}
	
	public synchronized void gradeDistance(long timeElapsed){
		if (quantity > 1)
			quantity--;
		else{
			counter++;
			double newAvg = (avgDistance*(counter-1) + timeElapsed) / counter;
			if(newAvg > avgDistance){
				grade++;
			}
			else if(newAvg < avgDistance){
				grade--;
			}
			quantity--;
			System.out.println("yes");
		}
	}
	
	public void incQuantity(){
		quantity++;
	}
	
	public double getGrade(){
		return grade;
	}
}
