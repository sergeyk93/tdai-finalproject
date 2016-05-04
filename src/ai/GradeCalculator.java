package ai;

import java.util.HashMap;

import models.creatures.Aigle;
import models.creatures.Araignee;
import models.creatures.Creature;
import models.creatures.Elephant;
import models.creatures.Mouton;
import models.creatures.MoutonNoir;
import models.creatures.Paysan;
import models.creatures.Pigeon;
import models.creatures.Rhinoceros;

public class GradeCalculator {

	/**
	 * This class maps between creatures and their current grades
	 * It also allows us to update the grades after each iteration
	 */
	private HashMap<String, Grade> grades = new HashMap<String, Grade>();

	/**
	 * Initializing a grade for each creature
	 */
	public GradeCalculator(){
		grades.put(Constants.AIGLE, new Grade(new Aigle()));
		grades.put(Constants.ARAIGNEE, new Grade(new Araignee()));
		grades.put(Constants.ELEPHAN, new Grade(new Elephant()));
		grades.put(Constants.MOUTON, new Grade(new Mouton()));
		grades.put(Constants.MOUTON_NOIR, new Grade(new MoutonNoir()));
		grades.put(Constants.PAYSAN, new Grade(new Paysan()));
		grades.put(Constants.PIGEONN, new Grade(new Pigeon()));
		grades.put(Constants.RHINOCEROS, new Grade(new Rhinoceros()));
	}

	public void updateGradeTowers(String creatureName, int groundTowers, int airTowers){
		grades.get(creatureName).gradeTowerType(groundTowers, airTowers);

	}

	public void updateGradeDistance(String creatureName, double timeElapsed){
		grades.get(creatureName).gradeDistance(timeElapsed);
	}

	public double getGrade(String creatureName){
		return grades.get(creatureName).getGrade();
	}

	/**
	 * 
	 * @return bestCreature - the best creature given the budget
	 */
	public Creature getBestCreature(double budget, Menu menu){
		Creature bestCreature = null;
		double maxGrade = Double.MIN_VALUE;

		for(String name : grades.keySet()){
			
			if(menu.getPrice(name).getPrice()>budget){
				continue;
			}
			
			double grade = getGrade(name);
			
			/* If the grade difference is less or equal than 1/2 then 
			 a random creature is chosen */
			if(Math.abs(grade - maxGrade) <= 0.5){
				int rand = (int)(Math.random() + 0.5);
				bestCreature = rand == 0 ? bestCreature : grades.get(name).getCreature();
				continue;
			}
			
			if(grade > maxGrade){
				maxGrade = grade;
				bestCreature = grades.get(name).getCreature();
			}
		}
		return bestCreature;
	}
	
	public void incGrade(String creatureName){
		grades.get(creatureName).incGrade();
	}
}
