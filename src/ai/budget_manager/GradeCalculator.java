package ai.budget_manager;

import java.util.HashMap;
import java.util.Iterator;

import models.creatures.Creature;

public class GradeCalculator {

	/**
	 * This class maps between creatures and their current grades
	 * It also allows us to update the grades after each iteration
	 */
	private HashMap<String, Grade> grades;;

	/**
	 * Initializing a grade for each creature
	 */
	public GradeCalculator(){
		grades = new HashMap<String, Grade>();
		
		Iterator<Creature> iter = Menu.getIter();
		while(iter.hasNext()){
			Creature c = iter.next();
			grades.put(c.getNom(), new Grade(c));
		}
	}

	public void updateGradeTowers(String creatureName, int groundTowers, int airTowers){
		grades.get(creatureName).gradeTowerType(groundTowers, airTowers);

	}

	public void updateGradeDistance(String creatureName, long timeElapsed){
		if (creatureName.equals("Grande Araignee")) return;
		grades.get(creatureName).gradeDistance(timeElapsed);
	}

	public double getGrade(String creatureName){
		return grades.get(creatureName).getGrade();
	}

	/**
	 * 
	 * @return bestCreature - the best creature given the budget
	 */
	public Creature getBestCreature(double budget){
		Creature bestCreature = null;
		double maxGrade = -1000;

		for(String name : grades.keySet()){
			
			if(Menu.getPrice(name) > budget){
				continue;
			}
			
			double grade = getGrade(name);
			
			/* If the grade difference is less or equal than 1/2 then 
			 a random creature is chosen */
			if(Math.abs(grade - maxGrade) <= 0.5){
				int rand = (int)(Math.random() + 0.5);
				bestCreature = rand == 0 ? bestCreature : grades.get(name).getCreature();
				continue;
			} else if(grade > maxGrade){
				maxGrade = grade;
				bestCreature = grades.get(name).getCreature();
			}
		}
		if (bestCreature != null){
			bestCreature.updateData();
			return bestCreature.copier();
		}
		
		return null;
	}
	
	public void incGrade(String creatureName){
		grades.get(creatureName).incGrade();
	}
	
	public void addAliveTime(String creatureName, long timeAlive){
		grades.get(creatureName).addAliveTime(timeAlive);
	}
	
	public long getLastAliveTime(String creatureName){
		return grades.get(creatureName).getLastAliveTime();
	}
}
