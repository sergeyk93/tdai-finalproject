package ai.dda;

public class DdaNormal extends Dda{

	public DdaNormal(){
		health_coef = 1;
		speed_coef = 1;
		prevWavesConsideration = 5;
		dropValueCoef = 1.6;
		budgetPerWave = 400;
		thresholdTime = 2;
		thresholdHP = 2;
		turnsT = 5;
		waveSize = 12;
	}

	@Override
	public Dda previousDda() {
		return new DdaEasy();
	}

	@Override
	public Dda nextDda() {
		return new DdaHard();
	}

	@Override
	public String toString() {
		return "Normal";
	}

	@Override
	public DdaEnum getEnum() {
		return DdaEnum.DDA_NORMAL;
	}

}
