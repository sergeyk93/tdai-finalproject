package ai.pathfinding;

import models.maillage.Arc;
import models.maillage.Noued;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

public class BackTrackingRandomPath<T1, T2> {

	private Noued arrive;
	private Noued depart;
	private Graph<Noued, Arc> graph;

	public BackTrackingRandomPath(Graph<Noued, Arc> graph, Noued depart, Noued arrive){
		this.graph = graph;
		this.depart = depart;
		this.arrive = arrive;
	}
	
	public GraphPath<Noued, Arc> getPath(){
		return null;
	}
}
