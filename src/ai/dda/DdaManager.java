package ai.dda;

import models.jeu.Jeu;

public class DdaManager {

	private static int currentHP;
	private static long CurrentLastCreatureTime;
	private static Dda dda;
	private static Boolean firstWave;
	private static Jeu jeu;
	private static int turns;

	public static void init(Jeu other) {
		currentHP = 20;
		CurrentLastCreatureTime = 0;
		dda = new DdaNormal();
		firstWave = true;
		jeu = other;
		turns = 0;
	}

	public static void updateDda(int newHP, long newTime){
		turns++;
		String curr = dda.toString();
		String next = "";
		int chooseDda = 0;
		System.out.println("+++++++++++++++++++++++++++++++++++++++++");
		System.out.println("DDA Current: HP = " + currentHP + ", Time = " + CurrentLastCreatureTime);
		System.out.println("DDA Update: HP = " + newHP + ", Time = " + newTime);
		System.out.println("DDA Difficulty: " + dda.toString());
		System.out.println("+++++++++++++++++++++++++++++++++++++++++");
		if (!firstWave){
			if (currentHP - newHP >= dda.thresholdHP)
				chooseDda--;
			if (currentHP - newHP >= 3 + dda.thresholdHP)
				chooseDda--;
			if (chooseDda == 0 && (CurrentLastCreatureTime - newTime) / 1000 >= dda.thresholdTime){
				chooseDda++;
			}
			
		}

		else{
			firstWave = false;
			if (currentHP - newHP >= dda.thresholdHP)
				chooseDda--;
		}

		if (chooseDda > 0){
			dda = dda.nextDda();
			next = dda.toString();
			turns = 0;
		}

		if (chooseDda < 0){
			dda = dda.previousDda();
			if (chooseDda == -2)
				dda = dda.previousDda();
			next = dda.toString();
			turns = 0;
		}

		if (next != "")
			print(curr, next);
		currentHP = newHP;
		CurrentLastCreatureTime = newTime;
		
		if (chooseDda == 0 && turns == dda.turnsT){
			turns = 0;
			dda = dda.nextDda();
		}
	}

	public static void print(String curr, String next){
		System.out.println("DDA has changed the difficulty from " + curr + " to: " + next);
	}
	
	public static double healthCoef(){
		return dda.health_coef;
	}

	public static double speedCoef(){
		return dda.speed_coef;
	}

	public static int prevWavesConsideration(){
		return dda.prevWavesConsideration;
	}

	public static double dropValueCoef(){
		return dda.dropValueCoef;
	}

	public static int budgetPerWave(){
		if (firstWave)
			return 700;
		return dda.budgetPerWave;
	}

	public static DdaEnum getDifficuly() {
		return dda.getEnum();
	}
	
	public static void updateTowers(){
		jeu.updateTowers();
	}

}
