package ai;

import models.creatures.Creature;

public class Grade {
	private Creature c;
	private double grade;
	private double avgDistance;
	private boolean moreAirTowers;
	private boolean wasChosen;

	public Grade(Creature c){
		this.c = c;
		// First grade value is based on max HP and speed
		grade = (c.getSanteMax() * c.getVitesseNormale()) / (new Price(c).getPrice());
		avgDistance = 5000;
		moreAirTowers = false;
		wasChosen = false;
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
			if(!moreAirTowers){
				moreAirTowers = true;
				grade = c.getType() == Creature.TYPE_AERIENNE ? grade + 1 : grade - 1;
			}
		}
		else{
			if(moreAirTowers){
				moreAirTowers = false;
				grade = c.getType() == Creature.TYPE_TERRIENNE ? grade + 1 : grade - 1;
			}
		}
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
		grade = wasChosen ? grade : grade + 1;
		return grade;
	}

	public Creature getCreature(){
		return c;
	}
	
	public void choose(){
		wasChosen = true;
	}
	
	public void unchoose(){
		wasChosen = false;
	}
	
	public boolean wasChosen(){
		return wasChosen;
	}
}
