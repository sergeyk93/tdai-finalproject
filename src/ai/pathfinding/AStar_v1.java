package ai.pathfinding;


import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.SimpleWeightedGraph;

import models.maillage.PathNotFoundException;


/**
 * Helper class containing pathfinding algorithms.
 * 
 * @author Ben Ruijl
 * 
 */
public class AStar_v1 {

	 /*
		 * Constantes
		 */
	  private final int NODES_NUMBER;
	  
		/**
		 * La largeur en pixel de chaque maille, ou noeud
		 */
		private final int NODE_HEIGHT;
		/**
		 * Le poid d'un arc diagonal
		 */
		private final int DIANGONAL_WEIGHT;
		/**
		 * La demi-distance entre un point et un autre
		 */
		private final int HALF_NODE;
		/**
		 * La largeur en pixel totale du maillage (axe des x)
		 */
		private final int WIDTH_IN_PIXELS;
		/**
		 * La hauteur en pixel totale du maillage (axe des y)
		 */
		private final int HEIGHT_IN_PIXELS;
		/**
		 * Les dimensions en maille (ou noeuds) du maillage
		 */
		private final int NODES_NUMBER_X, NODES_NUMBER_Y;

		/*
		 * Attributs
		 */
		/**
		 * Le graphe
		 */
		private SimpleWeightedGraph<Node, Edge> graph = new SimpleWeightedGraph<Node, Edge>(
				new GenerateEdges());
		/**
		 * Le tableau des noeuds : Noeud[x][y]
		 */
		private Node[][] nodes;
		/**
		 * Le decalage de base.
		 */
		private int xOffset, yOffset;

		/**
		 * Un maillage dynamique représentant une aire de jeu.
		 * 
		 * @param widthPixels
		 *            Largeur en pixel de la zone.
		 * @param heightPixels
		 *            Hauteur en pixel de la zone.
		 * @param heightOfNode
		 *            La largeur en pixel de chaque maille.
		 * @param xOffset
		 *            Le décalage en x du maillage, en pixels.
		 * @param yOffset
		 *            Le décalage en y du maillage, en pixels.
		 * @return 
		 * @throws IllegalArgumentException
		 *             Levé si les dimensions ne correspondent pas.
		 */

 	public AStar_v1(final int widthPixels, final int heightPixels,
			final int heightOfNode, int xOffset, int yOffset)
			throws IllegalArgumentException
	{
		/*
		 * Test des arguments.
		 */
		testInt(heightOfNode);
		testInt(widthPixels);
		testInt(heightPixels);

		// Assignation de la largeur du noeud (ou de la maille).
		NODE_HEIGHT = heightOfNode;

		// Calcule une fois pour toute la distance diagonale
		DIANGONAL_WEIGHT = (int) Math.sqrt(2 * NODE_HEIGHT * NODE_HEIGHT);

		// Largeur du demi noeud
		HALF_NODE = NODE_HEIGHT / 2;

		// Assignation de la dimension en pixel unitaire du maillage
		WIDTH_IN_PIXELS = widthPixels;
		HEIGHT_IN_PIXELS = heightPixels;

		// Conversion en dimension en maille.
		NODES_NUMBER_X = (widthPixels / NODE_HEIGHT);
		NODES_NUMBER_Y = (heightPixels / NODE_HEIGHT);

		NODES_NUMBER = NODES_NUMBER_X * NODES_NUMBER_Y;
		
		
		// Les offsets du décalage
		this.xOffset = xOffset;
		this.yOffset = yOffset;

		// Initialisation du champs de noeuds
		nodes = new Node[NODES_NUMBER_X][NODES_NUMBER_Y];

		// Construction du graphe
		constructGraph();
		
		// Affichage d'un petit truc dans la console
		//System.out.println(this);
	}

	/**
	 * 
	 * @param widthPixels
	 * @param heightPixels
	 * @param nodeWidth
	 * @throws IllegalArgumentException
	 */
	public AStar_v1(final int widthPixels, final int heightPixels,
			final int nodeWidth) throws IllegalArgumentException
	{
		this(widthPixels, heightPixels, nodeWidth, 0, 0);
	}

	public synchronized ArrayList<Point> shortestPath(int xDepart,
			int yDepart, int xArrive, int yArrive)
			throws PathNotFoundException, IllegalArgumentException
	{
		/*
		 * Test des arguments
		 */
		if (xDepart >= WIDTH_IN_PIXELS-xOffset || xArrive >= WIDTH_IN_PIXELS-xOffset
				|| xDepart-xOffset < 0 || xArrive-xOffset < 0)
			throw new IllegalArgumentException("x value is invalid");

		if (yDepart-yOffset >= HEIGHT_IN_PIXELS || yArrive-yOffset >= HEIGHT_IN_PIXELS
				|| yDepart-yOffset < 0 || yArrive-yOffset < 0)
			throw new IllegalArgumentException("y value is invalid");

		/*
		 * Calcul par Dijkstra du chemin le plus cours d'un point à un autre.
		 */
		GraphPath<Node, Edge> aStarPath;
		try
		{
			aStarPath = (new DijkstraShortestPath<Node, Edge>(
					graph,
					nodeContentThePoint(xDepart - xOffset, yDepart - yOffset),
					nodeContentThePoint(xArrive - xOffset, yArrive
							- yOffset))).getPath();
		} catch (IllegalArgumentException e)
		{
			// Retour de null en cas de levée d'exception de la part du graphe.
			throw new PathNotFoundException("The Path is Invalid.");
		}
		/*
		 * S'il n'y a pas de chemin
		 */
		if (aStarPath == null)
			throw new PathNotFoundException("The Path isn't Found");

		// Retourne l'ArrayList des points.
		return new ArrayList<Point>(Graphs.getPathVertexList(aStarPath));
	}

	/* (non-Javadoc)
   * @see models.maillage.MaillageI#activerZone(java.awt.Rectangle)
   */
	synchronized public void activerZone(Rectangle rectangle, boolean miseAJour)
			throws IllegalArgumentException
	{
		zoneActive(rectangle, true);
	}

	/* (non-Javadoc)
   * @see models.maillage.MaillageI#desactiverZone(java.awt.Rectangle)
   */
	synchronized public void desactiverZone(Rectangle rectangle, boolean miseAJour)
			throws IllegalArgumentException
	{
		zoneActive(rectangle, false);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Largeur du maillage : " + WIDTH_IN_PIXELS + " pixels\n"
				+ "Hauteur du maillage : " + HEIGHT_IN_PIXELS + " pixels\n"
				+ "Représentation      : 1 noeud = " + NODE_HEIGHT + "x"
				+ NODE_HEIGHT + " pixels\n" + "Nombre de noeuds    : "
				+ graph.vertexSet().size() + "\n" + "Nombre d'arcs       : "
				+ graph.edgeSet().size() + "\nDécalage            : X="
				+ xOffset + " Y=" + yOffset
				+"\n";
	}

	/* (non-Javadoc)
   * @see models.maillage.MaillageI#getLargeurPixels()
   */
	public int getLargeurPixels()
	{
		return WIDTH_IN_PIXELS;
	}

	/* (non-Javadoc)
   * @see models.maillage.MaillageI#getHauteurPixels()
   */
	public int getHauteurPixels()
	{
		return HEIGHT_IN_PIXELS;
	}

	/* (non-Javadoc)
   * @see models.maillage.MaillageI#getNoeuds()
   */
	public Node[] getNodes()
	{
	    Node[] tabNodes = new Node[NODES_NUMBER];
	    
	    int iNode = 0;
		for (Node[] line : nodes)
			for (Node node : line)
			    tabNodes[iNode++] = new Node(node);
		
		return tabNodes;
	}

	/* (non-Javadoc)
   * @see models.maillage.MaillageI#getArcs()
   */
	public Line2D[] getArcs()
	{
	    Line2D[] arcs = new Line2D[graph.edgeSet().size()];
	   
	    int iArc = 0;
		for (Edge edge : graph.edgeSet())
		    arcs[iArc++] = edge.toLine2D();
		
		return arcs;
	}

	/**
	 * Méthode de service pour activer ou désactiver une zone.
	 * 
	 * @param rectangle
	 *            La zone concernée.
	 * @param active
	 *            True s'il faut l'activer, False s'il faut la désactiver.
	 * @throws IllegalArgumentException
	 *             Levé si la zone est hors champs.
	 */
	private void zoneActive(final Rectangle rectangle, final boolean active)
			throws IllegalArgumentException
	{
		// Vérification de la validité du rectangle
		rectangleIsInMap(rectangle);

		/*
		 * Pour chaque noeuds on vérifie s'il intersect avec la zone concernée.
		 */
		for (Node[] line : nodes)
			for (Node node : line)
			{
				if (rectangle.intersects(new Rectangle(node.x - HALF_NODE,
						node.y - HALF_NODE, NODE_HEIGHT, NODE_HEIGHT)))
					if (active)
						activate(node);
					else if (node.isActive())
						disactivate(node);
			}
	}

	/**
	 * Active l'ensemble des arcs d'un noeud, marque le noeud comme actif, puis
	 * ajoute le noeud dans le graphe.
	 * 
	 * @param noeud
	 *            Le noeud dont on active les arcs.
	 * @throws IllegalArgumentException
	 *             Levé si le noeud est null ou s'il est déjà actif.
	 */
	private void activate(Node node) throws IllegalArgumentException
	{
		// Vérifie si le noeud n'est pas null
		if (node == null)
			throw new IllegalArgumentException(
					"The node is null");
		// Vérifie si le noeud n'est pas déjà actif.
		if (node.isActive())
			throw new IllegalArgumentException(
					"Impossible to activate an activated node");
		// Activation du noeud
		node.setActive(true);
		// Replanter le noeud dans le graphe, s'il n'est pas déjà présent
		graph.addVertex(node);

		/*
		 * Ajouter les arcs manquants
		 */
		int[] xy = Node.coordinate(node, xOffset, yOffset);
		int x, y;
		Node target;
		Edge edge;
		for (int i = -1; i <= 1; i++)
		{
			x = xy[0] + i;
			// Si le noeud n'est pas hors cadre
			if (x < 0 || x >= nodes.length)
				continue;
			for (int j = -1; j <= 1; j++)
			{
				y = xy[1] + j;
				// Si le noeud n'est pas hors cadre
				if (y < 0 || y >= nodes[x].length)
					continue;
				/*
				 * Extraction de la cible
				 */
				target = nodes[x][y];
				if (target == null)
					throw new IllegalArgumentException(
							"The node is null");
				// Si le noeud cible n'est pas actif ou s'il s'agit du noeud
				// courant
				if (!target.isActive() || target.equals(node))
					continue;
				// Ajout du noeud à l'ensemble. La méthode test si le noeud est
				// déjà présent
				graph.addVertex(target);
				// Calcul du nouvel arc
				edge = graph.addEdge(node, target);
				// Ajout du poids à l'arc
				graph.setEdgeWeight(edge,
						(Math.abs(i) != Math.abs(j)) ? NODE_HEIGHT
								: DIANGONAL_WEIGHT);
			}
		}
	}

	/**
	 * Désactive l'ensemble des arcs du noeud, le marque comme inactif et le
	 * retire du graphe.
	 * 
	 * @param noeud
	 *            Le noeud dont on désactive les arcs.
	 * @throws IllegalArgumentException
	 *             Levé si le noeud est null ou s'il est déjà inactif.
	 */
	private void disactivate(Node node) throws IllegalArgumentException
	{
		// Vérifie si le noeud n'est pas null
		if (node == null)
			throw new IllegalArgumentException(
					"The node is null");
		// Vérifie si le noeud n'est pas déjà inactif.
		if (!node.isActive())
			throw new IllegalArgumentException(
					"Impossible to disactivate a disactivated node");
		// Désactivation du noeud
		node.setActive(false);
		// Supprimer le noeud ainsi que tous les arcs relatifs
		graph.removeVertex(node);
	}

	private void constructGraph()
	{
		/*
		 * Ajouter les noeuds au graphe.
		 */
		for (int x = 0; x < NODES_NUMBER_X; x++)
			for (int y = 0; y < NODES_NUMBER_Y; y++)
				nodes[x][y] = new Node((x * NODE_HEIGHT) + xOffset,
						(y * NODE_HEIGHT) + yOffset, NODE_HEIGHT);

		/*
		 * Active tout les noeuds (et calcul les vertex)
		 */
		for (Node[] line : nodes)
			for (Node node : line)
				activate(node);
	}

	/**
	 * Méthode de service pour faire la relation Point -> Noeud.
	 * 
	 * Retourne le noeud actif le plus proche de la position x,y.
	 * 
	 * @param p
	 *            Le point à chercher
	 * @return Le noeud correspondant.
	 * @throws IllegalArgumentException
	 *             Levé si le noeud demandé est hors champs.
	 */
	private Node nodeContentThePoint(int x, int y)
	{
		// Calcul des coordonnées.
		int x_node = Node.pixelToNode(x, NODE_HEIGHT);
		int y_node = Node.pixelToNode(y, NODE_HEIGHT);
		// Vérification de la plausibilité des coordonnées.
		if (x_node < 0 || x_node > nodes.length)
			throw new IllegalArgumentException(
					"Noeud hors champs. Coordonnée x invalide : " + x);
		if (y_node < 0 || y_node > nodes[x_node].length)
			throw new IllegalArgumentException(
					"Noeud hors champs. Coordonnée y invalide : " + y);
		// Retour du noeud correspondant
		return nodes[x_node][y_node];
	}

	/**
	 * Méthode de service pour tester si le rectangle passé en paramètre est
	 * dans le champs.
	 * 
	 * @param rectangle
	 *            Le rectangle à tester.
	 * @throws IllegalArgumentException
	 *             Levé si le rectangle est hors chamos.
	 */
	private void rectangleIsInMap(Rectangle rectangle)
			throws IllegalArgumentException
	{
		if (rectangle == null)
			throw new IllegalArgumentException("Argument null");
		/*if (rectangle.getX() + rectangle.getWidth() > LARGEUR_EN_PIXELS)
			throw new IllegalArgumentException("Largeur hors cadre");
		if (rectangle.getY() + rectangle.getHeight() > HAUTEUR_EN_PIXELS)
			throw new IllegalArgumentException("Hauteur hors cadre");*/
	}

	/**
	 * Test si la valeur est valude.
	 * 
	 * @param value
	 *            La valeur à tester
	 * @throws IllegalArgumentException
	 *             Levé si la valeur est négative.
	 */
	private void testInt(int value) throws IllegalArgumentException
	{
		if (value < 0)
			throw new IllegalArgumentException("Valeur invalide (négative) : "
					+ value);
	}

	/**
	 * Calcul la distance entre chaque point
	 * 
	 * @param path une collection de point
	 * @return la longueur du chemin
	 */
  public double getPathLength(ArrayList<Point> path)
  {
      double length = 0.0;
      
      Point pCurrent;
      Iterator<Point> i = path.iterator();
      
      // first point
      if(i.hasNext())
          pCurrent = i.next();
      else
          return 0;
      
      // for all other...
      Point pNext;
      while(i.hasNext())
      {
          pNext = i.next();
          length += pCurrent.distance(pNext);
          pCurrent = pNext;
      }
      
      return length;
  }

  public int getNbNoeuds()
  {
      return NODES_NUMBER;
  }

  public void ajouterPointdeSortie(int x, int y)
  {
      // pas utilisé
  }

  public void miseAJourTDA()
  {
      // pas utilisé
  }
}
