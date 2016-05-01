package ai;

import java.util.HashMap;

import models.creatures.*;

public class GradeCalculator {

	/**
	 * This class maps between creatures and their current grades
	 * It also allows us to update the grades after each iteration
	 */
	private static HashMap<String, Grade> grades = new HashMap<String, Grade>();

	public static void init(){
		grades.put(Constants.AIGLE, new Grade(new Aigle()));
		grades.put(Constants.ARAIGNEE, new Grade(new Araignee()));
		grades.put(Constants.ELEPHAN, new Grade(new Elephant()));
		grades.put(Constants.MOUTON, new Grade(new Mouton()));
		grades.put(Constants.MOUTON_NOIR, new Grade(new MoutonNoir()));
		grades.put(Constants.PAYSAN, new Grade(new Paysan()));
		grades.put(Constants.PIGEONN, new Grade(new Pigeon()));
		grades.put(Constants.RHINOCEROS, new Grade(new Rhinoceros()));
	}

	public static void updateGradeTowers(String creatureName, int groundTowers, int airTowers){
		if(grades.containsKey(creatureName)){
			// WILL BE CHANGED ONCE WE FIGURE OUT HOW TO GET THE NUMBER OF EACH TOWER
			grades.get(creatureName).gradeTowerType(groundTowers, airTowers);
		}
	}

	public static void updateGradeDistance(String creatureName, double timeElapsed){
		grades.get(creatureName).gradeDistance(timeElapsed);
	}

	public static double getGrade(String creatureName){
		return grades.get(creatureName).getGrade();
	}
	
	public static void incGrade(String creatureName){
		grades.get(creatureName).addGrade();
	}
	
	/**
	 * 
	 * @return the best creature for the wave
	 */
	public static Creature getBestCreature(){
		Creature bestCreature = null;
		double maxGrade = Double.MIN_VALUE;
		
		for(String name : grades.keySet()){
			double grade = GradeCalculator.getGrade(name);
			
			// If the grade difference is less or equal than 1 then we randomly pick the creature
			if(grade <= maxGrade - 1 || grade >= maxGrade + 1){
				int rand = (int)(Math.random() + 0.5);
				bestCreature = rand == 0 ? bestCreature : grades.get(name).getCreature();
				continue;
			}
			if(grade > maxGrade){
				maxGrade = grade;
				bestCreature = grades.get(name).getCreature();
			}
		}
		
		grades.get(bestCreature.getNom()).incQuantity();
		return bestCreature;
	}
}
