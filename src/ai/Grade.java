package ai;

import models.creatures.Creature;

public class Grade {
	Creature c;
	double grade;
	int counter;
	double avgDistance;
	
	public Grade(Creature c){
		this.c = c;
		// First grade value is based on max HP and speed
		grade = (c.getSanteMax() * c.getVitesseNormale()) / (new Price(c).getPrice());
		System.out.println(c.getNom() + " grade: " + grade);
		counter = 0;
		avgDistance = 0;
	}
	
	public void gradeTowerType(int airTowers, int groundTowers){
		if (airTowers >= groundTowers){
			grade = c.getType() == Creature.TYPE_AERIENNE ? grade + 1 : grade - 1;
		}
		else{
			grade = c.getType() == Creature.TYPE_TERRIENNE ? grade + 1 : grade - 1;
		}
	}
	
	public void gradeDistance(long timeElapsed){
		counter++;
		double newAvg = (avgDistance + timeElapsed) / counter;
		if(newAvg > avgDistance){
			grade++;
		}
		else if(newAvg < avgDistance){
			grade--;
		}
	}
	
	public double getGrade(){
		return grade;
	}
}
