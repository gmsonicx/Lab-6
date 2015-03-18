/*
 * Graph.java
 *
 * Using stacks and queues for DFS and BFS.
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

/**
 * Graph class.  Holds representation of a graph as well as functions to 
 * interact with the graph.
 * 
 * @author atd Aaron T Deever
 * @author sps Sean Strout
 *
 */
public class Graph {

	/*
	 * graph is represented using a map (dictionary).
	 */
	private Map<String, Node> graph;
	
	List<Node> path = new LinkedList<Node>();

	/**
	 * Constructor.  Loads graph from a given filename.  Assumes that each line
	 * in the input file contains the names of two nodes.  Creates nodes
	 * as necessary as well as undirected edges between the nodes.
	 * Returns the graph in the form of a map having the names of the
	 * nodes as keys, and the nodes themselves as values.
	 * 
	 * @param filename name of the input graph specification file
	 * @throws FileNotFoundException if file not found
	 */
	public Graph(String filename) throws FileNotFoundException { 

		// open the file for scanning
		File file = new File(filename);
		Scanner in = new Scanner(file);

		// create the graph
		graph = new HashMap<String, Node>();

		// loop over and parse each line in the input file
		while (in.hasNextLine()) {
			// read and split the line into an array of strings
			// where each string is separated by a space.
			Node n1, n2;
			String line = in.nextLine();
			String[] fields = line.split(" ");

			// creates new nodes as necessary
			if (graph.containsKey(fields[0])) { 
				n1 = graph.get(fields[0]);
			}
			else { 
				n1 = new Node(fields[0]);
				graph.put(fields[0],  n1);
			}
			if (graph.containsKey(fields[1])) { 
				n2 = graph.get(fields[1]);
			}
			else { 
				n2 = new Node(fields[1]);
				graph.put(fields[1],  n2);
			}

			n1.addNeighbor(n2);
			n2.addNeighbor(n1);
		}
		in.close();
	}

	/**
	 * Method to generate a string associated with the graph.  The string
	 * comprises one line for each node in the graph. Overrides
	 * Object toString method.
	 * 
	 * @return string associated with the graph.
	 */
	public String toString() { 
		String result = "";
		for (String name : graph.keySet()) { 
			result = result + graph.get(name) + "\n";
		}
		return result;
	}

	/**
	 * Method to check if a given String node is in the graph.
	 * @param nodeName: string name of a node
	 * @return boolean true if the graph contains that key; false otherwise
	 */
	public boolean isInGraph(String nodeName) { 
		return graph.containsKey(nodeName);
	}

	/**
	 * For a given start and finish node, we simply want to know whether
	 * a path exists, or not, between them. This is the precursor to 
	 * searchDFS().
	 * @param start the name associated with the node from which to start the search
	 * @param finish the name associated with the destination node	 
	 * @return boolean true if a path exists; false otherwise
	 */
	public boolean canReachDFS(String start, String finish) {
		// assumes input check occurs previously
		Node startNode, finishNode;
		startNode = graph.get(start);
		finishNode = graph.get(finish);

		// prime the stack with the starting node
		Stack<Node> stack = new Stack<Node>();
		stack.push(startNode);

		// create a visited set to prevent cycles
		Set<Node> visited = new HashSet<Node>();
		// add start node to it
		visited.add(startNode);

		// loop until either the finish node is found (path exists), or the 
		// dispenser is empty (no path)
		while (!stack.isEmpty()) { 
			Node current = stack.pop();
			if (current == finishNode) {
				return true;    
			}
			// loop over all neighbors of current
			for (Node nbr : current.getNeighbors()) { 
				// process unvisited neighbors
				if (!visited.contains(nbr)) {
					visited.add(nbr);
					stack.push(nbr);
				}
			}
		}
		return false;
	}

	/**
	 * Method that visits all nodes reachable from the given starting node
	 * in depth-first search fashion using a stack, stopping only if the finishing
	 * node is reached or the search is exhausted.  A predecessors map
	 * keeps track of which nodes have been visited and along what path
	 * they were first reached.
	 * 
	 * @param start the name associated with the node from which to start the search
	 * @param finish the name associated with the destination node
	 * @return path the path from start to finish.  Empty if there is no such path.
	 * 
	 * Precondition: the inputs correspond to nodes in the graph. 
	 */
	public List<Node> searchDFS(String start) { 

		// assumes input check occurs previously
		Node startNode, finishNode;
		startNode = graph.get(start);

		// prime the dispenser (stack) with the starting node
		List<Node> dispenser = new LinkedList<Node>();
		dispenser.add(0, startNode);

		// construct the predecessors data structure
		Map<Node, Node> predecessors = new HashMap<Node,Node>();
		// put the starting node in, and just assign itself as predecessor
		predecessors.put(startNode, startNode);

		// loop until either the finish node is found, or the 
		// dispenser is empty (no path)
		while (!dispenser.isEmpty()) { 
			Node current = dispenser.remove(0); 
			// loop over all neighbors of current
			for (Node nbr : current.getNeighbors()) { 
				// process unvisited neighbors
				if(!predecessors.containsKey(nbr)) { 
					predecessors.put(nbr, current);
					dispenser.add(0, nbr);
				}
			}
		}

		return constructPath(predecessors, startNode);
	}

	/**
	 * Method that visits all nodes reachable from the given starting node
	 * in breadth-first search fashion using a queue, stopping only if the finishing
	 * node is reached or the search is exhausted.  A predecessors map
	 * keeps track of which nodes have been visited and along what path
	 * they were first reached.
	 * 
	 * @param start the name associated with the node from which to start the search
	 * @param finish the name associated with the destination node
	 * @return path the path from start to finish.  Empty if there is no such path.
	 * 
	 * Precondition: the inputs correspond to nodes in the graph. 
	 */
	public List<Node> searchBFS(String start) { 

		// assumes input check occurs previously
		Node startNode, finishNode;
		startNode = graph.get(start);

		// prime the dispenser (queue) with the starting node
		List<Node> dispenser = new LinkedList<Node>();
		dispenser.add(startNode);

		// construct the predecessors data structure
		Map<Node, Node> predecessors = new HashMap<Node,Node>();
		// put the starting node in, and just assign itself as predecessor
		predecessors.put(startNode, startNode);

		// loop until either the finish node is found, or the 
		// dispenser is empty (no path)
		while (!dispenser.isEmpty()) { 
			Node current = dispenser.remove(0);
			// loop over all neighbors of current
			for (Node nbr : current.getNeighbors()) { 
				// process unvisited neighbors
				if(!predecessors.containsKey(nbr)) { 
					predecessors.put(nbr, current);
					dispenser.add(nbr);
				}
			}
		}

		return constructPath(predecessors, startNode);
	}


	/**
	 * Method to return a path from the starting to finishing node.
	 * 
	 * @param predecessors Map used to reconstruct the path
	 * @param startNode starting node
	 * @param finishNode finishing node
	 * @return a list containing the sequence of nodes comprising the path.
	 * Empty if no path exists.
	 */
	@SuppressWarnings("unchecked")
	private List<Node> constructPath(Map<Node,Node> predecessors,
			Node startNode) { 

		// use predecessors to work backwards from finish to start, 
		// all the while dumping everything into a linked list

		if(predecessors.containsKey(startNode)) { 
			Node currNode = startNode;
			LinkedList<Node> jeff = (LinkedList<Node>) predecessors.keySet();
			for (int a = 0; a<jeff.size(); a++){
				while (currNode != predecessors.get(a)) { 
					path.add(0, currNode);
					currNode = predecessors.get(currNode);
				}			
			}
			path.add(0, startNode);
		}
		return path;
	}
	

	public List<Node> computeAllPrereqs(String name) {
		// TODO Auto-generated method stub
		Node current;
		current = graph.get(name);
		// prime the dispenser (queue) with the starting node
		List<Node> dispenser = new LinkedList<Node>();
		dispenser.add(current);

		// construct the predecessors data structure
		Map<Node, Node> predecessors = new HashMap<Node,Node>();
		// put the starting node in, and just assign itself as predecessor
		predecessors.put(current, current);

			// loop over all neighbors of current
			for (Node nbr : current.getNeighbors()) { 
				// process unvisited neighbors
				if(!predecessors.containsKey(nbr)) { 
					predecessors.put(nbr, current);
					dispenser.add(nbr);
				}
			} return dispenser;
		}
		
	

	public Set<String> getAllCourseNames() {
		return graph.keySet();
		
	}

	public List<Node> longestChainOfPrereqs() {
		// TODO Auto-generated method stub
		return null;
	}
}