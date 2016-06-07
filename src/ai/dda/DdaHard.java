package ai.dda;

public class DdaHard extends Dda{

	public DdaHard(){
		health_coef = 1.5;
		speed_coef = 1.5;
		prevWavesConsideration = 5;
		dropValueCoef = 4;
		budgetPerWave = 600;
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
}
