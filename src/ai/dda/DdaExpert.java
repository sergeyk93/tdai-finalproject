package ai.dda;

public class DdaExpert extends Dda{
	
	public DdaExpert(){
		health_coef = 2;
		speed_coef = 1.5;
		prevWavesConsideration = 3;
		dropValueCoef = 2;
		budgetPerWave = 800;
		thresholdTime = 4;
		thresholdHP = 1;
		turnsT = 12;
		//path_method = new PathExpert();
	}

	@Override
	public Dda previousDda() {
		return new DdaNormal();
	}
	
	@Override
	public Dda nextDda() {
		health_coef += 0.4;
		speed_coef += 0.1;
		return this;
	}

	@Override
	public String toString() {
		return "Expert";
	}

	@Override
	public DdaEnum getEnum() {
		return DdaEnum.DDA_EXPERT;
	}
}
