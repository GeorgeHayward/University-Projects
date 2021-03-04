package graphs.student;

import java.util.Iterator;

public class Vertex {
	/*
	 * class Vertex represents a vertex in a graph, containing the
	 * vertex label and other fields
	 * 
	 * contains a list of the vertex's neighbours, giving an adjacency
	 * list representation for the graph
	 */

	String id; // Vertex label
	VertexIDList adjs; // List of neighbours
	Boolean marked;  // Used to indicate previously visited
	Integer intval;  // For storing arbitrary integer values
	
	public Vertex(String s) {
		// Constructor
		id = s;
		marked = false;
		adjs = new VertexIDList();
		intval = 0;
	}
	
	public void setMarked() {
		marked = true;
	}

	public void setUnmarked() {
		marked = false;
	}
	
	public boolean isMarked() {
		return marked;
	}
	
	public void addAdjWithVal (String s, Double n) {
		// Adds a neighbour s to the current vertex, with value n
		adjs.put(s, n);
	}
	
	public void addAdjAndInc (String s) {
		// Adds a neighbour s to the current vertex
		// If it doesn't exist, add to adjacency list;
		// increment count
		if (!adjs.containsKey(s))
			adjs.put(s);
		adjs.inc(s);
	}
	
	public VertexIDList getAdjs() {
		return adjs;
	}

	public String getID() {
		return id;
	}
	
	public Integer getNum() {
		return intval;
	}
	
	public void setNum(Integer n) {
		intval = n;
	}
	
	public void incNum () {
		intval += 1;
	}
	
	public void print() {
		System.out.print("Node " + id + " (" + marked + "," + intval + ") :");
		adjs.print();
	}
	
	public static void main(String[] args) {
		Vertex v = new Vertex("nodeName");
		v.addAdjAndInc("some_element");
		v.addAdjAndInc("another");
		v.print();

		System.out.println("testing iterator ...");
		VertexIDList vAdjs = v.getAdjs();
		Iterator<String> it = vAdjs.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
}
