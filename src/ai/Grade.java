package ai;

import models.creatures.Creature;

public class Grade {
	Creature c;
	double grade;
	double avgDistance;
	boolean hasReduced;
	
	public Grade(Creature c){
		this.c = c;
		// First grade value is based on max HP and speed
		grade = (c.getSanteMax() * c.getVitesseNormale()) / (new Price(c).getPrice());
		avgDistance = 5000;
		hasReduced = false;
	}
	
	/**
	 * Updates the grade based on the towers the player bought
	 * For example if there are many air towers we will reduce the grade of air units
	 * @param airTowers
	 * @param groundTowers
	 */
	public void gradeTowerType(int airTowers, int groundTowers){
		double temp = grade;
		if (airTowers >= groundTowers){
			grade = c.getType() == Creature.TYPE_AERIENNE ? grade + 1 : grade - 1;
		}
		else{
			grade = c.getType() == Creature.TYPE_TERRIENNE ? grade + 1 : grade - 1;
		}
		if (temp > grade)
			if (hasReduced)
				grade = temp;
			else
				hasReduced = true;
		else
			hasReduced = false;
	}
	
	/**
	 * Updating the new average distance and updating the grade accordingly
	 * @param timeElapsed
	 */
	public void gradeDistance(double timeElapsed){
		if(avgDistance < timeElapsed){
			grade++;
		}
		else{
			grade--;
		}
		avgDistance = timeElapsed;
	}
	
	public double getGrade(){
		return grade;
	}
	
	public void addGrade(){
		grade++;
	}
	
	public Creature getCreature(){
		return c;
	}
}
