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

package vues;

import java.awt.*;
import java.io.File;
import javax.swing.*;

import exceptions.AucunePlaceDisponibleException;
import models.jeu.Jeu;
import models.jeu.Jeu_Solo;
import models.joueurs.Equipe;
import models.joueurs.Joueur;
import models.outils.GestionnaireSons;
import models.outils.Son;
import models.terrains.ElementTD;
import models.terrains.Terrain;
import vues.solo.Fenetre_JeuSolo;

/**
 * Fenetre du menu principal du jeu.
 * <p>
 * Affiche un menu permettant au joueur de choisir son mode de jeu.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | 20 avril 2010
 * @since jdk1.6.0_16
 */
public class Fenetre_MenuPrincipal extends JFrame
{
    // constantes statiques
    private static final long serialVersionUID = 1L;
    private static final ImageIcon I_FENETRE = new ImageIcon(
            "img/icones/icone_pgm.png");

    public static final int LARGEUR_FENETRE = 800;
    public static final int HAUTEUR_FENETRE = 600;
    
    public static final File FICHIER_MUSIQUE_MENU 
    = new File("snd/ambient/Jazzduphoodlum - Medieval In vestri Ass.mp3");
    
    /**
     * Constructeur de la fenetre du menu principal
     */
    public Fenetre_MenuPrincipal()
    {
        // -------------------------------
        // -- preferences de le fenetre --
        // -------------------------------
        //setSize(LARGEUR_FENETRE, HAUTEUR_FENETRE);
//        setIconImage(I_FENETRE.getImage());
//        setResizable(false);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // ------------------------
        // -- musique d'ambiance --
        // ------------------------
        Son musiqueDAmbiance = new Son(FICHIER_MUSIQUE_MENU);
        GestionnaireSons.ajouterSon(musiqueDAmbiance);
        musiqueDAmbiance.lire(0); // lecture infinie
        
        // ---------------------
        // -- panel principal --
        // ---------------------
        add(new Panel_MenuPrincipal(this), BorderLayout.CENTER);
        

        // --------------------------
        // -- dernieres proprietes --
        // --------------------------
        setPreferredSize(new Dimension(800,600));
//        pack();
//        setLocationRelativeTo(null);
        revalidate();
        setVisible(true);
    }
}
