package graphs.student;

import java.util.*;


public class VertexIDList extends TreeMap<String, Double> {

	/*
	 * List of vertices, to be used predominantly to represent
	 * the neighbours of a vertex; 
	 * can store counts for each neighbouring vertex
	 * 
	 * As an extension of TreeMap, inherits its methods, such
	 * as size()
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Iterator<Map.Entry<String, Double>> it; 
	
	public VertexIDList() {
		// constructor
		super();
	}
	
	/*
	public String firstKey() {
		// returns first element of list, treating as a queue
		return this.firstKey();
	}
	*/
	
	public void pop() {
		// deletes first element of list, treating as a queue
		this.remove(this.firstKey());
	}
		
	public void put(String s) {
		// inserts element, sets value to 0
		this.put(s, 0.0);
	}
	
	public void inc(String s) {
		this.put(s, this.get(s)+1);
	}
	
	public void print() {
		it = this.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Double> pairs = it.next();
			System.out.print(" " + pairs.getKey() + "(" + pairs.getValue() + ")");
		}		
		System.out.println();
	}
	
	public Iterator<String> iterator() {
		// returns an iterator to use to iterate over the list
		return this.keySet().iterator();
	}

	public static void main(String[] args) {
		VertexIDList v = new VertexIDList();
		
		v.put("the");
		v.put("purple");
		v.put("and");
		v.print();
		v.pop();
		System.out.println(v.firstKey());
		v.print();
		
		Iterator<String> vIt = v.iterator();
		
		System.out.println("testing iterator ...");
		while (vIt.hasNext()) {
			System.out.println(vIt.next());
		}
		v.print();
	}
}
