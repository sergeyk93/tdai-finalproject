package ai.dda;

public class DdaExpert extends Dda{
	
	public DdaExpert(){
		health_coef = 3;
		speed_coef = 1.5;
		prevWavesConsideration = 3;
		dropValueCoef = 3;
		budgetPerWave = 800;
		thresholdTime = 3;
		thresholdHP = 1;
		//path_method = new PathExpert();
	}

	@Override
	public Dda previousDda() {
		return new DdaHard();
	}
	
	@Override
	public Dda nextDda() {
		health_coef++;
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
