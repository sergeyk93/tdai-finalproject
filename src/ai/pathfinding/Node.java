package ai.pathfinding;

import java.util.Set;
import java.awt.Point;

/**
 * A node in a graph, useful for pathfinding.
 * 
 * @author Ben Ruijl
 * 
 * @param <T>
 *            Actual type of the node
 */
public class Node extends Point{
	
	private static final long serialVersionUID = 8628318929701303111L;
	
	/**
	 * Fanion pour savoir si le noeud est actif ou pas. Inactif par défaut,
	 * obligatoirement.
	 */
	private boolean active = false;
	
	/**
	 * Largeur du noeud
	 */
	protected final int NODE_WIDTH;

	/**
	 * Construit à noeud à partir des paramètres donnés. Restriction des droits
	 * au paquet seulement.
	 * 
	 * @param x
	 *            La coordonnée x du centre du noeud, en pixel.
	 * @param y
	 *            La coordonnée y du centre du noeud, en pixel.
	 * @param width
	 */
	Node(int x, int y, int width)
	{
		this.x = center(x, width);
		this.y = center(y, width);
		this.NODE_WIDTH = width;
	}

	/**
	 * Constructeur de copie. Restriction des droits au paquet seulement.
	 * 
	 * @param noeud
	 *            Le noeud à copie.
	 */
	Node(Node node)
	{
		this.x = node.x;
		this.y = node.y;
		this.NODE_WIDTH = node.NODE_WIDTH;
		this.active = node.active;
	}

	/**
	 * Retourne True si le noeud est actif, False sinon.
	 * 
	 * @return True si le noeud est actif, False sinon.
	 */
	public boolean isActive()
	{
		return active;
	}

	/**
	 * Compare le noeud courant à un noeud donné en paramètre.
	 * 
	 * @param node
	 *            Le noeud à comparer.
	 * @return True si les deux noeuds sont égaux, false sinon.
	 * @see Object#equals(Object)
	 */
	public boolean equals(Node node)
	{
		/*
		 * Test du paramètre.
		 */
		if (node == null)
			throw new IllegalArgumentException(
					"The node is null");
		// Retour de la valeur boolean calculée.
		return x == node.x && y == node.y
				&& NODE_WIDTH == node.NODE_WIDTH;
	}

	@Override
	public String toString()
	{
		return super.toString() + " width : " + NODE_WIDTH + "active: "
				+ active;
	}

	/**
	 * Défini le noeud comme actif, ou pas. Restriction au droit de package, il
	 * ne faut pas que quelqu'un d'externe puisse désactiver un noeud sans
	 * passer par le maillage.
	 * 
	 * @param active
	 *            True si le noeud est actif, False sinon.
	 */
	void setActive(boolean active)
	{
		this.active = active;
	}

	/**
	 * Calcul le centre du noeud contenant la coordonnée passée en paramètre.
	 * 
	 * @param i
	 *            La coordonnée quelconque en pixel
	 * @param width
	 *            La largeur du noeud en pixel
	 * @return La coordonnée du centre du noeud en pixel
	 */
	public static int center(int i, int width)
	{
		return i - (i % width) + (width / 2);
	}

	/**
	 * Converti un point quelconque en pixel en la coordonnée modale du noeud
	 * correspondant.
	 * 
	 * @param x
	 *            Le point quelconque en pixel
	 * @param width
	 *            Le coté du noeud
	 * @return La coordonnée nodale correspondante.
	 */
	public static int pixelToNode(int x, int width)
	{
		return (center(x, width) - (width / 2)) / width;
	}

	/**
	 * Retourne les coordonnées nodales d'un noeud passé en paramètre, avec des
	 * deltas x et y.
	 * 
	 * @param noeud
	 *            Le noeud à convertir
	 * @param deltaX
	 *            Le delta sur l'axe des x
	 * @param deltaY
	 *            Le delta sur l'axe des y
	 * @return un tableau de deux éléments contenant les coordonnées converties.
	 */
	public static int[] coordinate(Node node, int deltaX, int deltaY)
	{
		/*
		 * Test du paramètre
		 */
		if (node == null)
			throw new IllegalArgumentException(
					"The node is null");
		// Calcul de la coordonnée
		int[] r = new int[2];
		r[0] = (node.x - deltaX) / node.NODE_WIDTH;
		r[1] = (node.y - deltaY) / node.NODE_WIDTH;
		return r;
	}
}
