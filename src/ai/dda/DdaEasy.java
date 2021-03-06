package ai.dda;

public class DdaEasy extends Dda{
	
	public DdaEasy(){
		health_coef = 0.8;
		speed_coef = 0.8;
		prevWavesConsideration = 8;
		dropValueCoef = 1.2;
		budgetPerWave = 300;
		thresholdTime = 1;
		thresholdHP = 2;
		turnsT = 3;
		waveSize = 10;
	}

	@Override
	public Dda previousDda() {
		return new DdaVeryEasy();
	}
	
	@Override
	public Dda nextDda() {
		return new DdaNormal();
	}

	@Override
	public String toString() {
		return "Easy";
	}

	@Override
	public DdaEnum getEnum() {
		return DdaEnum.DDA_EASY;
	}

}
