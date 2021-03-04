package graphs.student;

public class Edge {
	/*
	 * class Edge consists only of a pair of integers, to represent
	 * edges in a graph with vertices labelled with integers; there are
	 * corresponding get and set methods for those
	*/
	 
	private String first;
	private String second;
	private Integer count;
	
	public Edge(String f, String s) {
		first = f;
		second = s;
		count = 0;
	}
	
	public String getFirst() {
		return first;
	}

	public String getSecond() {
		return second;
	}

	public void setFirst(String f) {
		first = f;
	}
	
	public void setSecond(String s) {
		second = s;
	}
	
	public void setCount (Integer n) {
		count = n;
	}
	
	public Integer getCount () {
		return count;
	}
	
	public void incCount() {
		count += 1;
	}
}
