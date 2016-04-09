package ai;

public class DdaHard extends Dda{

	public DdaHard(){
		health_coef = 1.5;
		speed_coef = 1.5;
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
