package ai;

import java.util.ArrayList;
import models.creatures.Creature;

public class Grade {
	private Creature c;
	private double grade;
	private double avgDistance;
	private boolean moreAirTowers;
	private ArrayList<Long> timeAliveValues;

	public Grade(Creature c){
		this.c = c;
		// First grade value is based on max HP and speed
		grade = (c.getSanteMax() * c.getVitesseNormale()) / (new Price(c).getPrice());
		avgDistance = 0;
		moreAirTowers = false;

		timeAliveValues = new ArrayList<Long>();

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
				grade = c.getType() == Creature.TYPE_AERIENNE ? grade + 1 : grade;
			}
		}
		else{
			if(moreAirTowers){
				moreAirTowers = false;
				grade = c.getType() == Creature.TYPE_TERRIENNE ? grade + 1 : grade;
			}
		}
	}

	/**
	 * Updating the new average distance and updating the grade accordingly
	 * @param timeElapsed
	 */
	public void gradeDistance(double timeElapsed){
		if(avgDistance==0){
			return;
		}
		
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

	public Creature getCreature(){
		return c;
	}

	public void incGrade(){
		grade++;
	}

	/**
	 * Adds the last alive time to the computation
	 * Considers 5 last values
	 * @param timeAlive
	 */
	public void addAliveTime(long timeAlive){
		if(timeAliveValues.size()>=5){
			timeAliveValues.remove(0);
		}
		timeAliveValues.add(timeAlive);
		
		avgDistance = 0;
		for(long time : timeAliveValues){
			avgDistance += time;
		}
		avgDistance /= timeAliveValues.size();
	}
	
	public long getLastAliveTime(){
		if(timeAliveValues.size()>0){
			return timeAliveValues.get(timeAliveValues.size() - 1);
		}
		return 0;
	}
}
