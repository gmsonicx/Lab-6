/*
 * Prerequisite.java
 *
 * Author:  Aaron Deever atd@cs.rit.edu
 * 
 * Main program for Graduation simulation.
 *
 */

import java.io.FileNotFoundException;
import java.util.List;

/**
 * This program takes as input data from a file corresponding
 * to a directed graph.  For the particular application, we assume
 * the data corresponds to courses and prerequisites.  But in
 * general, the concept applies to any directed graph.
 * 
 * We assume one feature of the graph, and 
 * using graph methods, we determine two other features of the graph:
 * <p>
 * ASSUMED: The nodes of the graph (courses) can be ordered
 * in such as way that every class can be taken after its prerequisites
 * have been met.  That is, there are no CYCLES in the directed graph.
 * Determining whether this is true for an arbitrary input graph is a problem
 * for a different day, a different class.
 * <p>
 * 1) Given such an ordering exists, we compute for each course how many other
 * courses must be completed before that particular course can be taken.
 * This corresponds not just to direct prerequisites, but to prerequisites
 * of the prerequisites, etc.
 * <p>
 * 2) Given such an ordering exists, what is the longest chain of
 * prerequisites in the graph?
 * This corresponds to how long it will take to graduate - given that you can't
 * take a course until its prerequisites are met (can't take simultaneously), 
 * it will take at least this many semesters to graduate.
 * 
 * @author atd Aaron T Deever
 *
 */
public class Prerequisite {

	/**
	 * Main method for the driver program.
	 * 
	 * @param args the name of the file containing the course and
	 * prerequisite information
	 * 
	 * @throws FileNotFoundException if input file not found
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		// Check for correct number of arguments
		if(args.length != 1) { 
			String us = "Usage: java Prerequisite <input file>";
			System.out.println(us);
			return;
		}
		
		// create a new graph and load the information
		// Graph constructor from lecture notes should
		// be modified to handle input specifications
		// for this lab.
		Graph graph = new Graph(args[0]);
		
		// print out the graph information
		System.out.println("Courses and Prerequisites");
		System.out.println("=========================");
		System.out.println(graph);
	
		// ASSUMPTION:  we assume there are no cycles in the graph
	    
		// Part I:
	    // compute how many (and which) courses must be taken before it is
	    // possible to take any particular course
   
    	System.out.println("How many courses must I take "
    			+ "before a given course?!?!?");
    	for(String name : graph.getAllCourseNames()) { 
    		List<Node> allPrereqs = graph.computeAllPrereqs(name);
    		System.out.print(String.valueOf(allPrereqs.size()));
    		System.out.print(" courses must be taken before " + name + ": ");
    		for(Node el : allPrereqs) { 
    			System.out.print(el.getName() + " ");
    		}
    		System.out.println();
    	}
	  
	    // Part II:
	    // now figure out how long it will take to "graduate"
    	System.out.println();
    	System.out.println("How quickly can I graduate?!?!?");
    	//List<Node> longestChain = graph.longestChainOfPrereqs();
    	
    	System.out.print("A longest chain of prerequisites is: ");
		//for(Node n : longestChain) { 
		//	System.out.print(n.getName() + " ");
		//}
		System.out.println();
    	// now just print out the value
		//System.out.print("It will take at least " + longestChain.size());
		System.out.println(" semesters to graduate.");		
	}

}