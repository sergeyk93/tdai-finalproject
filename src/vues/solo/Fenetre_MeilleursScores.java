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

package vues.solo;

import i18n.Langue;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import vues.GestionnaireDesPolices;
import vues.LookInterface;
import models.outils.*;

/**
 * Fenetre de gestion de l'affichage des meilleurs scores
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | 17 decembre 2009
 * @since jdk1.6.0_16
 */
public class Fenetre_MeilleursScores extends JDialog
{
    // constantes statiques
    private static final long serialVersionUID  = 1L;
    private static final ImageIcon I_FENETRE    = new ImageIcon("img/icones/star.png");

    // membre graphiques
    private JButton bFermer = new JButton(Langue.getTexte(Langue.ID_TXT_BTN_FERMER));
    
    // autres membres
    private MeilleursScores ms;
   
    /**
     * Constructeur de la fenetre des meilleurs scores d'un terrain avec une 
     * boite de dialogue comme parent.
     * 
     * @param nomTerrain le nom du terrain dont on veut voir les meilleurs scores
     * @param parent le Dialog parent
     */
    public Fenetre_MeilleursScores(String nomTerrain, Dialog parent)
    {
        // preference de la fenetre
        super(parent,String.format(Langue.getTexte(Langue.ID_TXT_LES_X_MEILLEURS_SCORES),MeilleursScores.NOMBRE_MAX_SCORES),true);
        
        construire(nomTerrain);
    }
    
    /**
     * Constructeur de la fenetre des meilleurs scores d'un terrain avec une 
     * fenetre comme parent.
     * 
     * @param nomTerrain le nom du terrain dont on veut voir les meilleurs scores
     * @param parent la Frame parent
     */
    public Fenetre_MeilleursScores(String nomTerrain, Frame parent)
    {
        super(parent,String.format(Langue.getTexte(Langue.ID_TXT_LES_X_MEILLEURS_SCORES),MeilleursScores.NOMBRE_MAX_SCORES),true);
  
        construire(nomTerrain);
    }

    /**
     * Permet de construire le contenu de la fenetre.
     * 
     * @param nomTerrain le nom du terrain
     */
    @SuppressWarnings("serial")
    private void construire(String nomTerrain)
    { 
        setIconImage(I_FENETRE.getImage());
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel pFormulaire = new JPanel(new BorderLayout());
        pFormulaire.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        pFormulaire.setBackground(LookInterface.COULEUR_DE_FOND_PRI);
        
        //------------------------------------
        //-- creation de la table de scores --
        //------------------------------------
        DefaultTableModel model = new DefaultTableModel();
        
        // nom de colonnes
        model.addColumn("");
        model.addColumn(Langue.getTexte(Langue.ID_TXT_JOUEUR));
        model.addColumn(Langue.getTexte(Langue.ID_TXT_SCORE));
        model.addColumn(Langue.getTexte(Langue.ID_TXT_DUREE));
        model.addColumn(Langue.getTexte(Langue.ID_TXT_DATE));
        
        // création de la table avec boquage des editions
        JTable tbScores = new JTable(model)
        {
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return false; // toujours désactivé
            }
        };

        tbScores.setEnabled(false);
        tbScores.setCellSelectionEnabled(true);
       
        // taille des colonnes
        tbScores.getColumnModel().getColumn(0).setPreferredWidth(10);
        tbScores.getColumnModel().getColumn(1).setPreferredWidth(100);
        tbScores.getColumnModel().getColumn(2).setPreferredWidth(50);
        tbScores.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbScores.getColumnModel().getColumn(4).setPreferredWidth(120);
  
        
        ms = new MeilleursScores(nomTerrain);
        
        int i=1;
        for(Score score : ms.getScores())
        {
            Object[] obj = new Object[] { (i++)+".", score.getNomJoueur(), score.getValeur()+"", score.getHMS(),
                    DateFormat.getInstance().format(score.getDate()) };
            
            model.addRow(obj);
        }
         
        JLabel lTitreForm = new JLabel(nomTerrain);
        lTitreForm.setFont(GestionnaireDesPolices.POLICE_TITRE);
        pFormulaire.add(lTitreForm,BorderLayout.NORTH);
        pFormulaire.add(new JScrollPane(tbScores),BorderLayout.CENTER);
        pFormulaire.add(bFermer,BorderLayout.SOUTH);
        getContentPane().add(pFormulaire,BorderLayout.CENTER);
         
        getRootPane().setDefaultButton(bFermer); // def button
        GestionnaireDesPolices.setStyle(bFermer);
        bFermer.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                dispose();
            }
        });
        
        // derniers parametres de la fenetre
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
