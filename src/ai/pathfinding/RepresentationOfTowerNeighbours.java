package ai.pathfinding;

import javax.swing.*;

public class RepresentationOfTowerNeighbours extends JFrame{

	private static final long serialVersionUID = 1L;
	  
	  public RepresentationOfTowerNeighbours(TowerNeighbour tn)
	  {
	      super("Representation of tower neighbour"); 
	      setDefaultCloseOperation(EXIT_ON_CLOSE);

	      add(tn.getRect());
	      
	      setVisible(true);
	      pack();
	      setLocationRelativeTo(null);
	  }
}