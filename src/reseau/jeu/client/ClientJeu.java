/*
  Copyright (C) 2010 Aurelien Da Campo, Romain Poulain
  
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

package reseau.jeu.client;

import i18n.Langue;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import reseau.CanalTCP;
import reseau.CanalException;
import reseau.jeu.serveur.ConstantesServeurJeu;
import reseau.jeu.serveur.Protocole;
import models.animations.GainDePiecesOr;
import models.creatures.*;
import models.jeu.Jeu_Client;
import models.joueurs.*;
import models.terrains.Terrain;
import models.tours.*;
import exceptions.*;

import org.json.*;

/**
 * Classe de gestion du moteur de jeu réseau (partie client).
 * 
 * @author Romain Poulain
 * @author Da Campo Aurélien
 * @version 1.1 | mai 2010
 * @since jdk1.6.0_16
 */
public class ClientJeu implements ConstantesServeurJeu, Runnable{
	
    /**
     * Canal de ping-pong envoie / reception
     */
	private CanalTCP canalEnvoi;
	
	/**
	 * Canal d'écoute asynchrone du serveur
	 */
	private CanalTCP canalEcoute;
	
	/**
	 * Le jeu du client
	 */
	private Jeu_Client jeu;
	
	/**
	 * Mode verbeux
	 */
    private final boolean verbeux = false;
    
    /**
     * Ecouteur de client jeu pour les notifications
     */
    private EcouteurDeClientJeu edcj;
    
    /**
     * Constructeur
     * 
     * @param jeu le moteur de jeu
     */
	public ClientJeu(Jeu_Client jeu)                
	{
		this.jeu = jeu;
	}
	
	/**
	 * Permet d'établir une connexion avec le serveur
	 * 
	 * @param IP l'ip du serveur
	 * @param port le port du serveur
	 * 
	 * @throws ConnectException
	 * @throws CanalException
	 * @throws AucunEmplacementDisponibleException
	 */
	public void etablirConnexion(String IP, int port) 
	    throws ConnectException, CanalException, AucunEmplacementDisponibleException 
    {
       
	    // création du canal 1 (Requête / réponse)
        canalEnvoi = new CanalTCP(IP, port);
        
        // demande de connexion au serveur (canal 1)
        canalEnvoi.envoyerString(jeu.getJoueurPrincipal().getPseudo());
        
        // le serveur nous retourne notre identificateur
        JSONObject msg;
        
        try{
            msg = new JSONObject(canalEnvoi.recevoirString());
            receptionJoueurInitialisation(msg);
        } 
        catch (JSONException e1){
            e1.printStackTrace(); 
        } 
        
        // reception de la version du serveur
        String version = canalEnvoi.recevoirString();
        log("Version du jeu : "+version);
        
        // reception du port du canal 2
        int portCanal2 = canalEnvoi.recevoirInt();
        
        // création du canal 2 (Reception asynchrone)
        canalEcoute = new CanalTCP(IP, portCanal2);

        // lancement de la tache d'écoute du canal 2
        (new Thread(this)).start();
    }
	
    /**
	 * Envoyer un message chat
	 * 
	 * TODO controler les betises de l'expediteur (guillemets, etc..)
	 * 
	 * @param message le message
	 * @param cible le joueur visé
     * @throws CanalException 
     * @throws MessageChatInvalide 
	 */
	public void envoyerMessage(String message, int cible) throws CanalException, MessageChatInvalide
	{
	    // pré controle
	    if(message.indexOf('<') != -1 || message.indexOf('>') != -1)
	        throw new MessageChatInvalide("Quotes interdites");

	    canalEnvoi.envoyerString(Protocole.construireMsgChat(message, cible));
	}
	
	/**
	 * Permet d'envoyer une vague de créatures
	 * 
	 * @param nbCreatures le nombre de créatures
	 * @param typeCreature le type des créatures
	 * @throws ArgentInsuffisantException 
	 * @throws CanalException 
	 */
	public void envoyerVague(VagueDeCreatures vague) throws ArgentInsuffisantException, CanalException
	{
		try
		{
			JSONObject json = new JSONObject();
			json.put("TYPE", VAGUE);
			json.put("TYPE_CREATURE", TypeDeCreature.getTypeCreature(vague.getNouvelleCreature()));
			json.put("NB_CREATURES", vague.getNbCreatures());
			
			canalEnvoi.envoyerString(json.toString());
			
			// attente de la réponse
            String resultat = canalEnvoi.recevoirString();
            JSONObject resultatJSON = new JSONObject(resultat);
            switch(resultatJSON.getInt("STATUS"))
            {
                case ARGENT_INSUFFISANT :
                    throw new ArgentInsuffisantException("Pas assez d'argent");
                case JOUEUR_INCONNU :
                    logErreur("Joueur inconnu");
            }	
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet de demander au serveur de jeu la pose d'une tour
	 * 
	 * @param tour la tour a poser
	 * 
	 * @throws ArgentInsuffisantException si pas assez d'argent
	 * @throws ZoneInaccessibleException si la pose est impossible 
	 * @throws CanalException 
	 */
	public void demanderPoseTour(Tour tour) 
	    throws ArgentInsuffisantException, ZoneInaccessibleException, CanalException
	{
		try 
		{
		    // envoye de la requete d'ajout
		    JSONObject json = new JSONObject();
			json.put("TYPE", TOUR_AJOUT);
			json.put("X", tour.x);
			json.put("Y", tour.y);
			json.put("TYPE_TOUR", TypeDeTour.getTypeDeTour(tour));
			
            log("Envoye d'une demande de pose d'une tour");
			
			canalEnvoi.envoyerString(json.toString());
			
			// attente de la réponse
			String resultat = canalEnvoi.recevoirString();
			JSONObject resultatJSON = new JSONObject(resultat);
			switch(resultatJSON.getInt("STATUS"))
			{
			    case ARGENT_INSUFFISANT : 
			        throw new ArgentInsuffisantException(Langue.getTexte(Langue.ID_ERROR_POSE_IMPOSSIBLE_PAS_ASSEZ_D_ARGENT));
			    case ZONE_INACCESSIBLE :
                    throw new ZoneInaccessibleException(Langue.getTexte(Langue.ID_ERROR_POSE_IMPOSSIBLE_ZONE_INACCESSIBLE));
			    case CHEMIN_BLOQUE :
                    throw new ZoneInaccessibleException(Langue.getTexte(Langue.ID_ERROR_POSE_IMPOSSIBLE_CHEMIN_BLOQUE));
                // TODO case JOUEUR_HORS_JEU:
                //   throw new JoueurHorsJeu("Tour inconnue");
			}
		} 
		catch (JSONException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Permet de demander au serveur de jeu d'améliorer une tour
	 * 
	 * @param tour la tour
	 * @throws ArgentInsuffisantException
	 * @throws ActionNonAutoriseeException 
	 * @throws CanalException 
	 * @throws NiveauMaxAtteintException 
	 * @throws JoueurHorsJeu 
	 */
	public void demanderAmeliorationTour(Tour tour) throws ArgentInsuffisantException, ActionNonAutoriseeException, CanalException, NiveauMaxAtteintException, JoueurHorsJeu
	{
		try 
		{
		    // envoye de la requete de vente
		    JSONObject json = new JSONObject();
			json.put("TYPE", TOUR_AMELIORATION);
			json.put("ID_TOWER", tour.getId());
			
			log("Envoye d'une demande de vente d'une tour");
                
			canalEnvoi.envoyerString(json.toString());
			
			String resultat = canalEnvoi.recevoirString(); // pas d'erreur possible...

			JSONObject resultatJSON = new JSONObject(resultat);
            switch(resultatJSON.getInt("STATUS"))
            {
                case TOUR_INCONNUE:
                    throw new NullPointerException("Unknown tower");
                case ARGENT_INSUFFISANT :
                    throw new ArgentInsuffisantException(Langue.getTexte(Langue.ID_ERROR_AMELIORATON_IMPOSSIBLE_PAS_ASSEZ_D_ARGENT));
                case   NIVEAU_MAX_ATTEINT:
                    throw new NiveauMaxAtteintException(Langue.getTexte(Langue.ID_ERROR_AMELIORATON_IMPOSSIBLE_NIVEAU_MAX_ATTEINT));
                case ACTION_NON_AUTORISEE :
                    throw new ActionNonAutoriseeException("Non authorized");
                case JOUEUR_HORS_JEU:
                    throw new JoueurHorsJeu("Unknown tower");
            }
			
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
     * Permet de demander au serveur de jeu la vente d'une tour
     * 
     * @param tour la tour a vendre
	 * @throws ActionNonAutoriseeException 
	 * @throws CanalException 
     */
	public void demanderVenteTour(Tour tour) throws ActionNonAutoriseeException, CanalException
	{
		try 
		{
			// demande
		    JSONObject json = new JSONObject();
			json.put("TYPE", TOUR_SUPRESSION);
			json.put("ID_TOWER", tour.getId());
			canalEnvoi.envoyerString(json.toString());
			
			// reponse
			String resultat = canalEnvoi.recevoirString();
            JSONObject resultatJSON = new JSONObject(resultat);
            switch(resultatJSON.getInt("STATUS"))
            {
                case ACTION_NON_AUTORISEE :
                    throw new ActionNonAutoriseeException("Non authorized");
                    
                // TODO case JOUEUR_HORS_JEU:
                //   throw new JoueurHorsJeu("Tour inconnue");
            
            }
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
     * Analyse d'un message d'ajout d'une tour
     * 
     * @param message le message
     * @throws TypeDeTourInvalideException 
     */
	private void receptionTourAjoutee(JSONObject message) throws JSONException
	{
	   
	    log("Réception d'un objet de type : Tour.");
	    
	    Tour tour = null;

	    // création de la tour en fonction de son type
        try
        {
            int typeDeCreature = message.getInt("TYPE_TOUR");
            tour = TypeDeTour.getTour(typeDeCreature);
            
            // initialisation des tours
            tour.x = message.getInt("X");
            tour.y = message.getInt("Y");
            tour.setId(message.getInt("ID_TOUR"));
            int idJoueur = message.getInt("ID_PROPRIETAIRE");
            tour.setProprietaire(jeu.getJoueur(idJoueur));
            
            jeu.poserTourDirect(tour);  
        } 
        catch (TypeDeTourInvalideException e)
        {
            logErreur("Ajout d'une tour : Tour de type inconnu");
        }
	}

	/**
	 * Tache de gestion du canal de réception des messages asynchrones
	 */
	public void run() 
	{
	    while(true)
	    {
            try 
            {
                attendreMessageCanalAsynchrone();
            } 
	        catch (CanalException e) {
	            logErreur("Canal erroné",e);
                return;
            } 
	        catch (JSONException e) {
	            logErreur("Format JSON erroné",e);
            }
	    }
	}

    private void attendreMessageCanalAsynchrone() throws CanalException, JSONException
    {
	    JSONObject resultat = new JSONObject(canalEcoute.recevoirString());
        
        switch(resultat.getInt("TYPE"))
        {
            
            // PARTIE
            case PARTIE_ETAT : 
                receptionPartieEtatChange(resultat);
            break;
            
            // JOUEURS
            case JOUEUR_ETAT :    
                receptionJoueurEtatChange(resultat);
            break; 
            
            case JOUEURS_ETAT :    
                receptionJoueursEtatChange(resultat);
            break;
            
            case JOUEUR_MESSAGE :    
                receptionJoueurMessage(resultat);
            break;
            
            case JOUEUR_DECONNEXION :    
                receptionJoueurDeconnecte(resultat);
            break;
            
            case EQUIPE_A_PERDUE :    
                receptionEquipeAPerdue(resultat);
            break;
            
            
            // TOURS
            case TOUR_AJOUT :
                receptionTourAjoutee(resultat);
                break;
            
            case TOUR_AMELIORATION :
                receptionTourAmelioree(resultat);
                break;    
                
            case TOUR_SUPRESSION :
                receptionTourVendue(resultat);
                break;
                
            // CREATURES
            case CREATURE_AJOUT :    
                receptionCreatureAjoutee(resultat);
                break;

            case CREATURE_ETAT :    
                receptionCreatureEtatChange(resultat);
                break;
                
            case CREATURE_SUPPRESSION :    
                receptionCreatureTuee(resultat);
                break;  

            case CREATURE_ARRIVEE : 
                receptionCreatureArrivee(resultat);
                break;
                
            default :
                logErreur("Réception d'un objet de type : Inconnu.");      
        }
    }

    private void receptionEquipeAPerdue(JSONObject resultat) throws JSONException
    {
        int idEquipe = resultat.getInt("ID_EQUIPE");

        Equipe equipe = jeu.getEquipe(idEquipe);

        if(equipe != null)
        {
            equipe.setNbViesRestantes(0);
            
            if(edcj != null)
                edcj.receptionEquipeAPerdue(equipe);
        }
        else
            logErreur("Equipe à perdue recu : Equipe inconnue"); 
    }

    private void receptionJoueurDeconnecte(JSONObject resultat) throws JSONException
    {
        log("Réception d'une déconnexion d'un joueur");
        
        int idJoueur    = resultat.getInt("ID_JOUEUR");
       
        Joueur joueur = jeu.getJoueur(idJoueur);
        
        if(joueur != null)
        {
            if(edcj != null)
                edcj.joueurDeconnecte(joueur);
        }
        else
            logErreur("Deconnexion recu : Auteur inconnu");
    }

    private void receptionJoueurMessage(JSONObject resultat) throws JSONException
    {
        log("Réception d'une message");
        
        int idAuteur    = resultat.getInt("ID_JOUEUR");
        String message  = resultat.getString("MESSAGE");
       
        Joueur joueur = jeu.getJoueur(idAuteur);
        
        if(joueur != null)
            edcj.messageRecu(message, joueur);
        else
            logErreur("Message recu : Auteur inconnu");    
    }

    private void receptionJoueursEtatChange(JSONObject resultat) throws JSONException
    {
        JSONArray joueurs = resultat.getJSONArray("JOUEURS");
        
        jeu.viderEquipes();
        
        Joueur joueur;
        for (int i = 0; i < joueurs.length(); i++)
        {
            JSONObject JSONjoueur = (JSONObject) joueurs.get(i);
            
            int idJoueur        = JSONjoueur.getInt("ID_JOUEUR");
            String nomJoueur    = JSONjoueur.getString("NOM_JOUEUR");
            int idEquipe        = JSONjoueur.getInt("ID_EQUIPE");
            int idEmplacement   = JSONjoueur.getInt("ID_EMPLACEMENT");

            // création du joueur
            joueur = new Joueur(nomJoueur);
            joueur.setId(idJoueur);
            
            // le joueur avec le meme id que l'ancien joueur principal
            // devient le joueur principal
            if(jeu.getJoueurPrincipal().getId() == idJoueur)
                jeu.setJoueurPrincipal(joueur);
               
            // ajout dans l'equipe
            try
            {
                jeu.getEquipe(idEquipe).ajouterJoueur(joueur,jeu.getEmplacementJoueur(idEmplacement));
            } 
            catch (EmplacementOccupeException e)
            {
                // ca n'arrivera pas, les infos viennent du serveur
                e.printStackTrace();
            }
        }
        
        if(edcj != null)
            edcj.joueursMisAJour();
    }

    private void receptionPartieEtatChange(JSONObject resultat) throws JSONException, CanalException
    {
        switch(resultat.getInt("ETAT"))
        {
            case PARTIE_INITIALISEE :
                
                log("Partie initialisée");
                jeu.initialiser();
                
                // envoye de la requete d'ajout
                JSONObject json = new JSONObject();
                json.put("TYPE", JOUEUR_PRET);

                canalEnvoi.envoyerString(json.toString());
 
                break;
            
            case PARTIE_LANCEE :
                
                jeu.demarrer();
                log("Partie lancee");
                break;
                
            case PARTIE_TERMINEE :
 
                System.out.println("PARTIE_TERMINEE : "+resultat);
 
                // mise a jour des equipes locales !
                Equipe equipe;
                JSONArray JSONequipes = resultat.getJSONArray("EQUIPES");
                for (int i = 0; i < JSONequipes.length(); i++)
                {
                    JSONObject JSONequipe = JSONequipes.getJSONObject(i);
                    
                    int idEquipe = JSONequipe.getInt("ID_EQUIPE");
                    
                    equipe = jeu.getEquipe(idEquipe);
                    
                    if(equipe != null)
                        equipe.setNbViesRestantes(JSONequipe.getInt("NB_VIES_RESTANTES"));
                    else
                        logErreur("Partie terminée : Equipe inconnue");      
                }

                // FIXME mise a jour des joueurs locales !
                // IMPORTANT POUR LE SCORE DE L'EQUIPE !
                
                
                jeu.terminer();
                
                log("Partie terminee");
                break;
                
                
                
            default :
                logErreur("Etat d'une partie : Etat inconnu");
        }
        
        
    }

    private void receptionCreatureArrivee(JSONObject message) throws JSONException
    {
	    log("Réception de l'arrivée d'une créature");
	    
        int idCreature = message.getInt("ID_CREATURE");
        
        Creature creature = jeu.getCreature(idCreature);
        
        if(creature != null)
            jeu.supprimerCreatureDirect(creature);
        else
            logErreur("Créature arrivée : Créature inconnue");      
    }

    private void receptionJoueurInitialisation(JSONObject message) throws JSONException, AucunEmplacementDisponibleException
    {
	    log("Réception des donnees d'initialisation du joueur");
        
        switch(message.getInt("STATUS"))
        {
            case OK :
                
                int idJoueur = message.getInt("ID_JOUEUR");
                int idEquipe = message.getInt("ID_EQUIPE");
                int idEmplacement = message.getInt("ID_EMPLACEMENT");
                String nomFichierTerrain = message.getString("NOM_FICHIER_TERRAIN");
                 
                log("Reception d'une initialisation de joueur [idJ:"+idJoueur+" idE:"+idEquipe+" idEJ:"+idEmplacement+" terrain:"+nomFichierTerrain+"]");
                
                Terrain terrain;
                
                try 
                {
                    terrain = Terrain.charger(new File(Terrain.EMPLACEMENT_TERRAINS_MULTI+nomFichierTerrain));
                    jeu.setTerrain(terrain);
                    terrain.setJeu(jeu);
                    
                    jeu.getJoueurPrincipal().setId(idJoueur);
                    
                    Equipe equipe = jeu.getEquipe(idEquipe);
                    EmplacementJoueur emplacementJoueur = jeu.getEmplacementJoueur(idEmplacement);

                    if(equipe != null && emplacementJoueur != null)
                    {
                        equipe.ajouterJoueur(jeu.getJoueurPrincipal(),emplacementJoueur);  
                        
                        if(edcj != null)
                            edcj.joueurInitialise();  
                    }
                    else
                        logErreur("Initialisation du joueur : Equipe ou emplacementJoueur inconnu");
                    
                } 
                catch (IOException e) {
                    logErreur("Initialisation du joueur : Terrain inconnu",e);
                } 
                catch (ClassNotFoundException e) {
                    logErreur("Initialisation du joueur : "+e.getMessage(),e);
                }
                catch (ClassCastException e)
                {
                    logErreur("Initialisation du joueur : Format de terrain erroné",e);
                } 
                catch (EmplacementOccupeException e)
                {
                    // ca n'arrivera pas, l'info vient du serveur
                    logErreur("Initialisation du joueur : Emplacement occupé",e);
                }
 
                break;
        
            case PAS_DE_PLACE :    
                log("Reception d'un refu");
                throw new AucunEmplacementDisponibleException("Aucun emplacement disponible");
        } 
    }
	
	 /**
     * Analyse d'un message d'état d'un joueur
     * 
     * @param message le message
     */
	private void receptionJoueurEtatChange(JSONObject message) throws JSONException
    {
        int idJoueur = message.getInt("ID_JOUEUR");
        
        log("Réception de l'état d'un joueur (id:"+idJoueur+")");
        
        double nbPiecesDOr = message.getDouble("NB_PIECES_OR");
        int score = message.getInt("SCORE");
        double revenu = message.getDouble("REVENU");
        int nbViesRestantes = message.getInt("NB_VIES_RESTANTES_EQUIPE");
        
        Joueur joueur = jeu.getJoueur(idJoueur);
        
        if(joueur != null)
        { 
            joueur.setNbPiecesDOr(nbPiecesDOr);
            joueur.setScore(score);
            joueur.getEquipe().setNbViesRestantes(nbViesRestantes);
            joueur.setRevenu(revenu);
        }
        else
            logErreur("Etat d'un joueur : Joueur inconnu (id:"+idJoueur+")");
    }

    /**
     * Analyse d'un message d'amélioration d'une tour
     * 
     * @param message le message
     */
    private void receptionTourAmelioree(JSONObject message) throws JSONException
    {
        log("Réception de l'amélioration d'une tour");
        
        int idTour = message.getInt("ID_TOUR");
        
        Tour tour = jeu.getTour(idTour);
        
        if(tour != null)
            jeu.ameliorerTourDirect(tour); 
        else
            logErreur("Amélioration d'une tour : Tour inconnue");
    }

    /**
     * Analyse d'un message de vente d'une tour
     * 
     * @param message le message
     */
    private void receptionTourVendue(JSONObject message) throws JSONException
    {
        log("Réception de la suppression d'une tour");
        
        int idTour = message.getInt("ID_TOUR");
        Tour tour = jeu.getTour(idTour);
        
        if(tour != null)
            jeu.supprimerTourDirect(tour); 
        else
            logErreur("Vente d'une tour : Tour inconnue");
    }
    
    /**
     * Analyse d'un message d'ajout d'une créature
     * 
     * @param message le message
     */
    private void receptionCreatureAjoutee(JSONObject message) throws JSONException
    {
        log("Réception de l'ajout d'une créature.");
        
        int id = message.getInt("ID_CREATURE");
        int typeCreature = message.getInt("TYPE_CREATURE");
        
        int idProprio = message.getInt("ID_PROPRIETAIRE");
        int idEquipe = message.getInt("ID_EQUIPE_CIBLEE");
        
        int x = message.getInt("X");
        int y = message.getInt("Y");
        long santeMax = message.getLong("SANTE_MAX");
        int nbPiecesDOr = message.getInt("NB_PIECES_OR");
        double vitesse = message.getDouble("VITESSE");
        
        
        
        
        Creature creature   = TypeDeCreature.getCreature(typeCreature, 1, true);
        Equipe equipeCiblee = jeu.getEquipe(idEquipe);
        Joueur proprio      = jeu.getJoueur(idProprio);
        
        if(creature != null)
        {
            creature.setId(id);
            creature.setX(x);
            creature.setY(y);
            creature.setEquipeCiblee(equipeCiblee);
            creature.setProprietaire(proprio);
            creature.misAJour();
            creature.setSanteMax(santeMax);
            creature.setNbPiecesDOr(nbPiecesDOr);
            creature.setVitesse(vitesse);
            
            jeu.ajouterCreatureDirect(creature);
        }
        else
            logErreur("Ajout d'une créature : Créature de type inconnu (type : "+typeCreature+")");
    }
    
    /**
     * Analyse d'un message d'état d'une créature
     * 
     * @param message le message
     */
    private void receptionCreatureEtatChange(JSONObject message) throws JSONException
    {
        int idCreature = message.getInt("ID_CREATURE");
        int x = message.getInt("X");
        int y = message.getInt("Y");
        int sante = message.getInt("SANTE");
        double angle = message.getDouble("ANGLE");
        
        Creature creature = jeu.getCreature(idCreature);
        
        // Elle peut avoir été détruite entre-temps.
        if(creature != null)
        {
            creature.x = x;
            creature.y = y;
            creature.setSante(sante);
            creature.setAngle(angle);
            creature.misAJour();
        }
        else
           logErreur("Etat d'une créature : Créature inconnue (id : "+idCreature+")");
    }
    
    /**
     * Analyse d'un message de suppression d'une creature
     * 
     * @param message le message
     */
    private void receptionCreatureTuee(JSONObject message) throws JSONException
    {
        int idCreature = message.getInt("ID_CREATURE");
        Creature creature = jeu.getCreature(idCreature);
        
        int idTueur = message.getInt("ID_TUEUR");
        Joueur joueur = jeu.getJoueur(idTueur);
         
        if(creature != null)
        {
            jeu.ajouterAnimation(new GainDePiecesOr((int)creature.getCenterX(),
                                                (int)creature.getCenterY(),
                                                creature.getNbPiecesDOr()));
            creature.mourrir(joueur);
        }
        else
            logErreur("Créature tuée : Créature inconnue (id : "+idCreature+")");
    }
    
    /**
     * Permet d'afficher des message log
     * 
     * @param msg le message
     */
    public void log(String msg)
    {
        if(verbeux)
            System.out.println("[CLIENT][JOUEUR "+jeu.getJoueurPrincipal().getId()+"] "+msg);
    }
    
    /**
     * Permet d'afficher des message log d'erreur
     * 
     * @param msg le message
     */
    private void logErreur(String msg)
    {
        System.err.println("[CLIENT][ERREUR][JOUEUR "+jeu.getJoueurPrincipal().getId()+"] "+msg);
    }

    /**
     * Permet d'afficher des message log d'erreur
     * 
     * @param msg le message
     */
    private void logErreur(String msg, Exception e)
    {
        System.err.println("[CLIENT][ERREUR][JOUEUR "+jeu.getJoueurPrincipal().getId()+"] "+msg);
    
        //e.printStackTrace();
    }
    
    public void demanderChangementEquipe(Joueur joueur, Equipe equipe) throws AucunEmplacementDisponibleException, CanalException
    {
        try 
        {
            // envoye de la requete de vente
            JSONObject json = new JSONObject();
            json.put("TYPE", JOUEUR_CHANGER_EQUIPE);
            json.put("ID_JOUEUR", joueur.getId());
            json.put("ID_EQUIPE", equipe.getId());
            
            log("Envoye d'une demande de changement d'équipe");
                
            canalEnvoi.envoyerString(json.toString());
            
            String resultat = canalEnvoi.recevoirString();
            JSONObject resultatJSON = new JSONObject(resultat);
            switch(resultatJSON.getInt("STATUS"))
            {
                case PAS_DE_PLACE :
                    throw new AucunEmplacementDisponibleException("Pas de place dans cette équipe");
            }
        } 
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
     
    public void setEcouteurDeClientJeu(EcouteurDeClientJeu edcj)
    {
        this.edcj = edcj;
    }

    public void annoncerDeconnexion() throws CanalException
    {
       try{
        
        // envoye de la requete de vente
        JSONObject json = new JSONObject();
        json.put("TYPE", JOUEUR_DECONNEXION);
        
        log("Envoye d'une deconnexion");
            
        canalEnvoi.envoyerString(json.toString());
  
        } 
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
