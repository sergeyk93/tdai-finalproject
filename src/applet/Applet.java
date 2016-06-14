package applet;

import i18n.Langue;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;

import javax.swing.JApplet;
import javax.swing.UIManager;

import models.outils.GestionnaireSons;
import driver.Game;

public class Applet extends JApplet{
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 813;
	private static final int HEIGHT = 652;
	
	private Container container;
	
	public void init() { 
		System.out.println("Applet initializing");
		
		// Removing applet default menu
        Frame[] frames = Frame.getFrames();
        for (Frame frame : frames) {
            frame.setMenuBar(null);
            frame.pack();
        }

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

		//	ChooseGame choice = new ChooseGame();
		//	
		//	getContentPane().add(choice);

		Langue.initaliser("..\\lang\\en_EN.json");
		// new Fenetre_ChoixLangue();
		
		container = getContentPane();
		
		container.setSize(WIDTH, HEIGHT);
		
		container.setLayout(new BorderLayout());

		// creation du menu principal
		container.add(new Game().getPanel());
	}

	public void start() {
		System.out.println("Applet starting");
	}
	public void stop() {
		System.out.println("Applet stopping");
	}
	public void destroy() {
		System.out.println("Applet destroyed");
	}

}
