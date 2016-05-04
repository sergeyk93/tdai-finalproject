package ai;

public class DdaEasy extends Dda{
	
	public DdaEasy(){
		health_coef = 0.75;
		speed_coef = 0.75;
		prevWavesConsideration = 8;
		dropValueCoef = 4;
		budgetPerWave = 300;
		//path_method = new PathEasy();
	}

	@Override
	public Dda previousDda() {
		return new DdaVeryEasy();
	}
	
	@Override
	public Dda nextDda() {
		return new DdaNormal();
	}

}
