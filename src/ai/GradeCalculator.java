package ai;

import java.util.HashMap;

import models.creatures.Aigle;
import models.creatures.Araignee;
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
	
	public static void updateGradeTowers(String creatureName){
		if(grades.containsKey(creatureName)){
			// WILL BE CHANGED ONCE WE FIGURE OUT HOW TO GET THE NUMBER OF EACH TOWER
			grades.get(creatureName).gradeTowerType(0, 0);
		}
	}
	
	public static void updateGradeDistance(String creatureName, long timeElapsed){
		if(grades.containsKey(creatureName)){
			grades.get(creatureName).gradeDistance(timeElapsed);
		}
	}
	
	public static double getGrade(String creatureName){
		return grades.get(creatureName).getGrade();
	}
}
