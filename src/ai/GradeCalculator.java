package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
	private HashSet<String> prevWave;

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
		
		prevWave = new HashSet<String>();
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
	 * @return bestCreature - the best creature for the current wave
	 */
	public Creature getBestCreature(){
		Creature bestCreature = null;
		double maxGrade = Double.MIN_VALUE;

		for(String name : grades.keySet()){
			
			// Determining if the creature was chosen in the previous wave
			if(prevWave.contains(name)){
				grades.get(name).choose();
			}
			else{
				grades.get(name).unchoose();
			}
			
			double grade = getGrade(name);

			// If the grade difference is less or equal than 1 then we randomly pick the creature
			/*if(grade >= maxGrade - 1){
				int rand = (int)(Math.random() + 0.5);
				bestCreature = rand == 0 ? bestCreature : grades.get(name).getCreature();
				continue;
			}*/
			if(grade > maxGrade){
				maxGrade = grade;
				bestCreature = grades.get(name).getCreature();
			}
		}
		return bestCreature;
	}
	
	/**
	 * Initializes the previous wave and adds the new creatures
	 * @param name
	 */
	public void addCreaturesToWave(ArrayList<String> names){
		prevWave = new HashSet<String>();
		prevWave.addAll(names);
	}
}
