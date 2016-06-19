package ai.dda;

public class DdaHard extends Dda{

	public DdaHard(){
		health_coef = 1.5;
		speed_coef = 1.3;
		prevWavesConsideration = 5;
		dropValueCoef = 1.8;
		budgetPerWave = 600;
		thresholdTime = 4;
		thresholdHP = 2;
		turnsT = 10;
		//path_method = new PathHard();
	}

	@Override
	public Dda previousDda() {
		return new DdaNormal();
	}

	@Override
	public Dda nextDda() {
		return new DdaExpert();
	}

	@Override
	public String toString() {
		return "Hard";
	}

	@Override
	public DdaEnum getEnum() {
		return DdaEnum.DDA_HARD;
	}
}
