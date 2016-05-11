/*
  Copyright (C) 2010 Aurelien Da Campo

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/

import javax.swing.UIManager;

import exceptions.AucunePlaceDisponibleException;
import i18n.Langue;
import models.jeu.Jeu;
import models.jeu.Jeu_Solo;
import models.joueurs.Equipe;
import models.joueurs.Joueur;
import models.outils.GestionnaireSons;
import models.terrains.ElementTD;
import models.terrains.Terrain;
import vues.Fenetre_MenuPrincipal;
import vues.solo.Fenetre_JeuSolo;

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
       // essaye de mettre le nouveau look and feel "Nimbus" fourni par Java
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
      
      new ChooseGame();
      
      Langue.initaliser("lang/en_EN.json");
      // new Fenetre_ChoixLangue();
      
      // creation du menu principal
      new Game();
	  
      // A shutdown hook that closes the logger's file handler
      Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
          public void run() {
              ai.AILogger.closeLogger();
          }
      }, "Shutdown-thread"));
   }
}
