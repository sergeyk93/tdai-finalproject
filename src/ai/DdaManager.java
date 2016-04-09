package ai;

public class DdaManager {

	private int currentHP;
	private int CurrentLastCreatureTime;
	private int thresholdHP;
	private int thresholdTime;
	private Dda dda;
	private Boolean firstWave;
	private int chooseDda;
	
	public DdaManager(){
		currentHP = 20;
		CurrentLastCreatureTime = 0;
		thresholdHP = 3;
		thresholdTime = 2000;
		dda = new DdaNormal();
		firstWave = true;
		chooseDda = 0;
	}
	
	public void updateDda(int newHP, int newTime){
		
		if (firstWave == false){
			if (currentHP - newHP >= thresholdHP)
				chooseDda--;
			if (newTime - CurrentLastCreatureTime >= thresholdTime)
				chooseDda++;
			if (CurrentLastCreatureTime - newTime >= thresholdTime)
				chooseDda--;
		}
		
		else{
			firstWave = true;
			if (currentHP - newHP >= thresholdHP)
				chooseDda--;
		}
		
		if (chooseDda > 0){
			dda = dda.nextDda();
			thresholdHP = thresholdHP == 4 ? thresholdHP : thresholdHP++;
			thresholdTime = thresholdTime == 4000 ? thresholdTime : thresholdTime + 1000;
		}
		
		if (chooseDda < 0){
			dda = dda.previousDda();
			thresholdHP = thresholdHP == 1 ? thresholdHP : thresholdHP--;
			thresholdTime = thresholdTime == 1000 ? thresholdTime : thresholdTime - 1000;
		}
		
		chooseDda = 0;
		currentHP = newHP;
		CurrentLastCreatureTime = newTime;
	}
	
	public Dda getDda(){
		return dda;
	}
}
