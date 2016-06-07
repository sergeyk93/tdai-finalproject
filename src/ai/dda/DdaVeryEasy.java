package ai.dda;

public class DdaVeryEasy extends Dda{
	
	public DdaVeryEasy(){
		health_coef = 0.5;
		speed_coef = 0.5;
		prevWavesConsideration = 10;
		dropValueCoef = 5;
		budgetPerWave = 200;
		//path_method = new PathVeryEasy();
	}

	@Override
	public Dda previousDda() {
		return new DdaVeryEasy();
	}
	
	@Override
	public Dda nextDda() {
		return new DdaEasy();
	}

}
