package ai.pathfinding;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.SimpleWeightedGraph;

import models.maillage.PathNotFoundException;

public class AStarPath<Node, Arc> implements GraphPath<Node, Arc>{

	SimpleWeightedGraph<Node, Arc> graph;
	Node depart;
	Node arrive;
	
	public AStarPath(SimpleWeightedGraph<Node, Arc> graph,
			Node depart, Node arrive){
		this.graph = graph;
		this.depart = depart;
		this.arrive = arrive;
	}

	public GraphPath<Node, Arc> getPath() throws IllegalArgumentException, PathNotFoundException{
		/**
		 * A Star pathfinding. Note that the heuristic has to be monotonic:
		 * {@code h(x) <=
		 * d(x, y) + h(y)}.
		 * 
		 * @param depart
		 *            Starting node
		 * @param arrive
		 *            Goal node
		 * @return Shortest path from start to goal, or null if none found
		 */
//		public static <T extends Node<T>> List<T> doAStar(T depart, T arrive) {
//			Set<T> closed = new HashSet<T>();
//			Map<T, T> fromMap = new HashMap<T, T>();
//			List<T> route = new LinkedList<T>();
//			Map<T, Double> gScore = new HashMap<T, Double>();
//			final Map<T, Double> fScore = new HashMap<T, Double>();
//			PriorityQueue<T> open = new PriorityQueue<T>(11, new Comparator<T>() {
//
//				public int compare(T nodeA, T nodeB) {
//					return Double.compare(fScore.get(nodeA), fScore.get(nodeB));
//				}
//			});
//
//			gScore.put(depart, 0.0);
//			fScore.put(depart, depart.getHeuristic(arrive));
//			open.offer(depart);
//
//			while (!open.isEmpty()) {
//				T current = open.poll();
//				if (current.equals(arrive)) {
//					while (current != null) {
//						route.add(0, current);
//						current = fromMap.get(current);
//					}
//
//					return route;
//				}
//
//				closed.add(current);
//
//				for (T neighbour : current.getNeighbours()) {
//					if (closed.contains(neighbour)) {
//						continue;
//					}
//
//					double tentG = gScore.get(current)
//							+ current.getTraversalCost(neighbour);
//
//					boolean contains = open.contains(neighbour);
//					if (!contains || tentG < gScore.get(neighbour)) {
//						gScore.put(neighbour, tentG);
//						fScore.put(neighbour, tentG + neighbour.getHeuristic(arrive));
//
//						if (contains) {
//							open.remove(neighbour);
//						}
//
//						open.offer(neighbour);
//						fromMap.put(neighbour, current);
//					}
//				}
//			}

			return null;
		}

		@Override
		public List<Arc> getEdgeList() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Node getEndVertex() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Graph<Node, Arc> getGraph() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Node getStartVertex() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public double getWeight() {
			// TODO Auto-generated method stub
			return 0;
		}

}
