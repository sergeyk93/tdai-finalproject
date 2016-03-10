package ai;

import models.creatures.Creature;

public class Grade {
	Creature c;
	double grade;
	double avgDistance;
	int avgNum;
	
	public Grade(Creature c){
		this.c = c;
		grade = initGrade();
		avgDistance = 0;
		avgNum = 0;
	}

	private double initGrade() {
		return c.getSanteMax() * c.getVitesseNormale();
	}
	
	public void gradeTowerType(int airTowers, int groundTowers){
		if (airTowers >= groundTowers){
			if (c.getType() == Creature.TYPE_AERIENNE){
				grade++;
			}
			else
				grade--;
		}
		else{
			if (c.getType() == Creature.TYPE_TERRIENNE){
				grade++;
			}
			else
				grade--;
		}
	}
	
	public void gradeDistance(double lastAvgDistance, int lastAvgNum){
		int newNum = lastAvgNum + avgNum;
		double newAvg = (avgNum * avgDistance) / newNum;
		if (newAvg > avgDistance)
			grade++;
		if (newAvg < avgDistance)
			grade--;
		avgDistance = newNum;
		avgNum = newNum;
	}
}
