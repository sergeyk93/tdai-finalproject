package ai;

import models.creatures.Creature;

public class Grade {
	private Creature c;
	private double grade;
	private double avgDistance;
	private int prevAirTowers;
	private int prevGroundTowers;
	private boolean moreAirTowers;

	public Grade(Creature c){
		this.c = c;
		// First grade value is based on max HP and speed
		grade = (c.getSanteMax() * c.getVitesseNormale()) / (new Price(c).getPrice());
		avgDistance = 5000;
		prevAirTowers = 0;
		prevGroundTowers = 0;
		moreAirTowers = false;
	}

	/**
	 * Updates the grade based on the towers the player bought
	 * For example if there are many air towers the grade of air units is reduced
	 * @param airTowers
	 * @param groundTowers
	 */
	public void gradeTowerType(int airTowers, int groundTowers){
		// If the number of the winning hasn't changed we don't update the grade
		// Hence, we update it only once until it increases again
		if (airTowers >= groundTowers){
			if(prevAirTowers <= airTowers || !moreAirTowers){
				moreAirTowers = true;
				grade = c.getType() == Creature.TYPE_AERIENNE ? grade + 1 : grade - 1;
			}
		}
		else{
			if(prevGroundTowers <= groundTowers || moreAirTowers){
				moreAirTowers = false;
				grade = c.getType() == Creature.TYPE_TERRIENNE ? grade + 1 : grade - 1;
			}
		}
		
		prevAirTowers = airTowers;
		prevGroundTowers = groundTowers;
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

	public void incGrade(){
		grade++;
	}

	public Creature getCreature(){
		return c;
	}
}
