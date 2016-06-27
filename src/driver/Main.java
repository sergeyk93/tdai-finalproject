package driver;

import java.io.File;

import javax.swing.UIManager;

import i18n.Langue;
import models.outils.GestionnaireSons;
import vues.commun.Fenetre_HTML;

/**
 * Classe principale du jeu Tower Defense.
 * 
 * Cette classe contient la methode main du programme.
 * Elle s'occupe de configurer le style de l'interface graphique
 * et d'ouvrir le menu principal du programme.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 */
public class Main
{
	/**
	 * Programme principal.
	 * 
	 * Configure le style de l'interface et ouvre le menu principal.
	 * 
	 * @param args Les arguments fournis au programme principal.
	 */
	public static void main(String[] args) 
	{
		for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels())
			if ("Nimbus".equals(laf.getName())) 
				try 
		{
					UIManager.setLookAndFeel(laf.getClassName());
		}
		catch (Exception e) 
		{
			/* 
			 * On fait rien, c'est pas grave. 
			 * C'est juste le look and feel qui n'est pas installe.
			 */ 
		}    

		//Langue.creerFichiersDeLangue();

		GestionnaireSons.setVolumeSysteme(GestionnaireSons.VOLUME_PAR_DEFAUT);     
		
		Langue.initaliser("lang/en_EN.json");
//		new ChooseGame();
		new Game();

		// A shutdown hook that closes the logger's file handler
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				ai.utils.AILogger.closeLogger();
			}
		}, "Shutdown-thread"));
	}
}
