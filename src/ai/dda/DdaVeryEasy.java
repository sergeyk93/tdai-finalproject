package ai.dda;

public class DdaVeryEasy extends Dda{

	public DdaVeryEasy(){
		health_coef = 0.7;
		speed_coef = 0.7;
		prevWavesConsideration = 10;
		dropValueCoef = 2;
		budgetPerWave = 200;
		thresholdTime = 1;
		thresholdHP = 3;
		turnsT = 15;
		//path_method = new PathVeryEasy();
	}

	@Override
	public Dda previousDda() {
		return new DdaVeryEasy();
	}

	@Override
	public Dda nextDda() {
		return this;
	}

	@Override
	public String toString() {
		return "Very Easy";
	}

	@Override
	public DdaEnum getEnum() {
		return DdaEnum.DDA_VERY_EASY;
	}

}
