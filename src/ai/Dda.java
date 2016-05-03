package ai;

public abstract class Dda {

	protected double health_coef;
	protected double speed_coef;
	protected int prevWavesConsideration;
	
	//protected Path path_method;
	
	public Dda(){}
	
	public abstract Dda previousDda();
	
	public abstract Dda nextDda();
	
	public double getHealthCoef(){
		return health_coef;
	}
	
	public double getSpeedCoef(){
		return speed_coef;
	}
	
//	public Path getPathMethod(){
//		return path_method;
//	}
	
}
