package ai.dda;

public class DdaNormal extends Dda{
	
	public DdaNormal(){
			health_coef = 1;
			speed_coef = 1;
			prevWavesConsideration = 5;
			dropValueCoef = 3;
			budgetPerWave = 400;
			//path_method = new PathNormal();
	}
	
	@Override
	public Dda previousDda() {
		return new DdaEasy();
	}
	
	@Override
	public Dda nextDda() {
		return new DdaHard();
	}

}
