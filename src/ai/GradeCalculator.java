package ai;

import java.util.ArrayList;

import models.creatures.Aigle;
import models.creatures.Araignee;
import models.creatures.Elephant;
import models.creatures.Mouton;
import models.creatures.MoutonNoir;
import models.creatures.Paysan;
import models.creatures.Pigeon;
import models.creatures.Rhinoceros;

public class GradeCalculator {
	ArrayList<Grade> grades;
	
	public GradeCalculator(){
		grades = new ArrayList<Grade>();
		grades.add(new Grade(new Aigle()));
		grades.add(new Grade(new Araignee()));
		grades.add(new Grade(new Elephant()));
		grades.add(new Grade(new Mouton()));
		grades.add(new Grade(new MoutonNoir()));
		grades.add(new Grade(new Paysan()));
		grades.add(new Grade(new Pigeon()));
		grades.add(new Grade(new Rhinoceros()));
	}
}
