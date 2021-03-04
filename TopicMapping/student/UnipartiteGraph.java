package graphs.student;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.Vector;
import java.util.TreeSet;

import org.apache.commons.math3.distribution.PoissonDistribution;


public class UnipartiteGraph {

	/*
	 * START: graph representation
	 */
	private TreeMap<String, Vertex> VertexMap;
	/*
	 * END: graph representation
	 */
	
	/*
	 * START: required functions
	 */

	public UnipartiteGraph() {
		// constructor
		VertexMap = new TreeMap<String, Vertex>();
	}

	public void setGraph(BipartiteGraph bpg) {
		// PRE: -
		// POST: instantiates the unipartite graph based on a bipartite graph of words and docs;
		//       creates vertices and edges, with edge weights the word similarity z_ab
		
		//Add all words to the VertexMap.
		Collection<Vertex> list = bpg.getMap("word").values();
		Iterator<Vertex> vertexIT = list.iterator();
		while(vertexIT.hasNext()){
			Vertex temp = (Vertex) vertexIT.next();
			VertexMap.put(temp.getID(), new Vertex(temp.getID()));
		}
		
		//Add adjacency list to all Vertices.
		list = VertexMap.values();
		vertexIT = list.iterator();
		//Go through every word Vertex.
		while(vertexIT.hasNext()){
			Vertex temp = (Vertex) vertexIT.next();
			//Get the list of adjacent vertices to this particular vertex.
			VertexIDList adjList = bpg.getMap("word").get(temp.getID()).getAdjs();
			Iterator<String> adjIT = adjList.iterator();
			//Go through the list of adjacent vertices (documents)
			while(adjIT.hasNext()){
				String tempString = adjIT.next();
				Collection <Vertex> docAdjList = bpg.getMap("word").values();
				Iterator<Vertex> docAdjListIT = docAdjList.iterator();
				//Go through words and find out if they have this particular document as a vertex.
				while(docAdjListIT.hasNext()){
					Vertex tempAdj = (Vertex) docAdjListIT.next();
					//If not the selected vertex (cant be neighbour of itself)
					if(tempAdj.getID().equals(temp.getID()) == false){
						//If this vertex's adjacent list contains this document then...
						if(tempAdj.getAdjs().containsKey(tempString)){
							//Add this vertex to the adjacency list for the super vertex being checked.
							// If temp already has this as an adjacent vertex.
							double x = (double) bpg.numTimesWordOccursInDoc(tempAdj.getID(), bpg.getMap("doc").get(tempString).getID());
							if(temp.getAdjs().containsKey(tempAdj.getID())){
								//Maybe add 1 because the double value will be 0 if there is only 1 connection.
								temp.addAdjWithVal(tempAdj.getID(), temp.getAdjs().get(tempAdj.getID()).doubleValue() + 1 + x);
							}
							//The temp hasn't been added to this adjacent vertex.
							else{
								temp.addAdjWithVal(tempAdj.getID(), x);								
							}
						}
					}
				}
			}
		}
	}

	public Double getEdgeWeight(String a, String b) {
		// PRE: a, b correspond to vertices in the graph
		// POST: returns the weight of the edge between vertices corresponding to words a and b
		// Get the edge weight by calling a then getting b inside the adjs. Then just return the double value.
		return VertexMap.get(a).getAdjs().get(b).doubleValue();
	}

	public void setEdgeWeight(String a, String b, Double val) {
		// PRE: a, b correspond to vertices in the graph
		// POST: sets the weight of the edge between vertices corresponding to words a and b to be val
		//Just set for both seperate words.
		VertexMap.get(a).addAdjWithVal(b, val);
		VertexMap.get(b).addAdjWithVal(a, val);
	}
	
	public void setGraphMinusStopwords(BipartiteGraph bpg, String stopFileName) {
		// PRE: stopFileName is the name of a file containing stopwords
		// POST: as the regular setGraph(), but stopwords do not become vertices
		Vector<String> stopWords = null;
		try {
			stopWords = readStopWordList(stopFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Add all words to the VertexMap.
				Collection<Vertex> list = bpg.getMap("word").values();
				Iterator<Vertex> vertexIT = list.iterator();
				while(vertexIT.hasNext()){
					Vertex temp = (Vertex) vertexIT.next();
					if(stopWords.contains(temp.getID()) == false){
					VertexMap.put(temp.getID(), new Vertex(temp.getID()));
					}
				}
				//Add adjacency list to all Vertices.
				list = VertexMap.values();
				vertexIT = list.iterator();
				//Go through every word Vertex.
				while(vertexIT.hasNext()){
					Vertex temp = (Vertex) vertexIT.next();
					//Get the list of adjacent vertices to this particular vertex.
					VertexIDList adjList = bpg.getMap("word").get(temp.getID()).getAdjs();
					Iterator<String> adjIT = adjList.iterator();
					//Go through the list of adjacent vertices (documents)
					while(adjIT.hasNext()){
						String tempString = adjIT.next();
						Collection <Vertex> docAdjList = bpg.getMap("word").values();
						Iterator<Vertex> docAdjListIT = docAdjList.iterator();
						//Go through words and find out if they have this particular document as a vertex.
						while(docAdjListIT.hasNext()){
							Vertex tempAdj = (Vertex) docAdjListIT.next();
							//If not the selected vertex (cant be neighbour of itself)
							if(tempAdj.getID().equals(temp.getID()) == false && stopWords.contains(tempAdj.getID()) == false){
								//If this vertex's adjacent list contains this document then...
								if(tempAdj.getAdjs().containsKey(tempString)){
									//Add this vertex to the adjacency list for the super vertex being checked.
									// If temp already has this as an adjacent vertex.
									double x = (double) bpg.numTimesWordOccursInDoc(tempAdj.getID(), bpg.getMap("doc").get(tempString).getID());
									if(temp.getAdjs().containsKey(tempAdj.getID())){
										//Maybe add 1 because the double value will be 0 if there is only 1 connection.
										temp.addAdjWithVal(tempAdj.getID(), temp.getAdjs().get(tempAdj.getID()).doubleValue() + 1 + x);
									}
									//The temp hasn't been added to this adjacent vertex.
									else{
										temp.addAdjWithVal(tempAdj.getID(), x);								
									}
								}
							}
						}
					}
				}
			}
	

	public void setNullGraph(BipartiteGraph bpg) {
		// PRE: -
		// POST: as the regular setGraph(),
		//         but sets the edge weights to be the values under the null model
		//         <z_ab> = s_a * s_b * L_C^2 + \sum_d L_d^2
		
		//s_a == number of times word (a) appears in corpus
		//s_b == number of times word (b) appears in corpus
		//L_C == total number of words in corpus
		//L_D ==  total number of words in document (d)
		setGraph(bpg);
		Collection<Vertex> list = VertexMap.values();
		Iterator<Vertex> vertexIT = list.iterator();
		//Go through every vertex.
		while(vertexIT.hasNext()){
			Vertex tempVertex = vertexIT.next();
			//Get adj list for this vertex.
			VertexIDList adjList = tempVertex.getAdjs();
			Iterator<String> adjIT = adjList.iterator();
			//Go through every adj.
			while(adjIT.hasNext()){
				Vertex adj = VertexMap.get(adjIT.next());
				//Get the occurrences of sA in the whole corpus. (+1 because 1 occurrence = 0 in getNum())
				double sA = bpg.getMap("word").get(tempVertex.getID()).getNum() + 1;
				double sB = bpg.getMap("word").get(adj.getID()).getNum() + 1;
				//Get total amount of words in corpus.
				double LC = bpg.numWordTokens();
				//square it.
				double LCsqrd = LC * LC;
				Collection<Vertex> docMap = bpg.getMap("doc").values();
				Iterator<Vertex> docIT = docMap.iterator();
				double LD = 0;
				//Get LD by going through all documents and getting the amount of words in each one.
				while(docIT.hasNext()){
					Collection<Double> tempadj = docIT.next().getAdjs().values();
					Iterator<Double> tempadjIT = tempadj.iterator();
					double ld = 0;
					while(tempadjIT.hasNext()){
						ld = ld + tempadjIT.next().doubleValue();
					}
					LD = LD + (ld * ld);
				}
				double newEdgeWeight = ((sA * sB) / LCsqrd) * LD; 
				this.setEdgeWeight(tempVertex.getID(), adj.getID(), newEdgeWeight);
			}
		}
		//Connect the rest of the vertex's and give them value 0. Since they don't occur together.
		list = VertexMap.values();
		vertexIT = list.iterator();
		while(vertexIT.hasNext()){
			Vertex temp = vertexIT.next();
			Collection<Vertex> list2 = VertexMap.values();
			Iterator<Vertex> vertexIT2 = list2.iterator();
			while(vertexIT2.hasNext()){
				Vertex temp2 = vertexIT2.next();
				if(temp2.equals(temp) == false){
					if(temp.getAdjs().containsKey(temp2.getID()) == false){
						temp.addAdjWithVal(temp2.getID(), 0.0);
						temp2.addAdjWithVal(temp.getID(), 0.0);
					}
				}
			}
		}
	}


	public void filterNoise(UnipartiteGraph nullGraph, Double p) {
		// PRE: 0 <= p <= 1
		// POST: reduce values on edges taking into account the null model graph;
		//         calculates Z_p(s_a, s_b) as per specs: the value which represents the 
		//         (1-p)-quantile of the Poisson distribution Pois_<z_ab>(z)
		
		// TODO
	}

	public Integer numVertices() {
		// PRE: -
		// POST: returns number of vertices in the graph
		return VertexMap.size();
	}

	public Integer numEdges() {
		// PRE: -
		// POST: returns number of edges in the graph
		int total = 0;
		Collection<Vertex> list = VertexMap.values();
		Iterator<Vertex> vertexIT = list.iterator();
		//Go through every word Vertex.
		while(vertexIT.hasNext()){
			Vertex temp = (Vertex) vertexIT.next();
			Iterator<String> adjIT = temp.getAdjs().iterator();
			while(adjIT.hasNext()){
				String thisAdj = adjIT.next();
				//If it hasn't been marked yet.
				if(VertexMap.get(thisAdj).isMarked() == false){
					total++;
				}				
			}
			VertexMap.get(temp.getID()).setMarked();
		}
		return total;
	}

	public Integer degreeWord(String w) {
		// PRE: w corresponds to a vertex in the graph
		// POST: returns the degree of the vertex corresponding to word w
		Double degree = 0.0;
		Collection<Double> adjDoubles = VertexMap.get(w).getAdjs().values();
		Iterator<Double> x = adjDoubles.iterator();
		//Add up all the adj values.
		while(x.hasNext()){
			degree = degree + x.next();
		}
		return VertexMap.get(w).getAdjs().size();
	}
	
	public Integer numVerticesDegreeK(Integer k) {
		// PRE: k >= 0
		// POST: returns the number of vertices that have degree k
		int total = 0;
		Collection<Vertex> list = VertexMap.values();
		Iterator<Vertex> vertexIT = list.iterator();
		//Go through all words and find the ones that have degree k. This is done by using the above method on every vertex.
		while(vertexIT.hasNext()){
			Vertex temp = (Vertex) vertexIT.next();
			if(this.degreeWord(temp.getID()) == k){
				total++;
			}
		}
		return total;
	}
	
	public Integer numVerticesConnectedAll() {
		// PRE: -
		// POST: returns the number of vertices that are connected to all other vertices
		int total = 0;
		Collection<Vertex> list = VertexMap.values();
		Iterator<Vertex> vertexIT = list.iterator();
		//Go through all the word and add 1 to total if the adj size is == to the vertexMap size - 1. (since a vertex can't be a neighbour to itself)
		while(vertexIT.hasNext()){
			Vertex temp = (Vertex) vertexIT.next();
			if(temp.getAdjs().size() == VertexMap.size() - 1){
				total++;
			}
		}
		return total;
	}

	public Double avgDegreeOfGraph() {
		// PRE: -
		// POST: returns the average degree of all vertices in the graph;
		//       returns zero for the null graph
		if(this.VertexMap == null){
			return 0.0;
		}
		else{
			double[] degrees = new double[VertexMap.size()];
			Collection<Vertex> list = VertexMap.values();
			Iterator<Vertex> vertexIT = list.iterator();
			int i = 0;
			while(vertexIT.hasNext()){
				Vertex temp = (Vertex) vertexIT.next();
				degrees[i] = degreeWord(temp.getID());
				i++;
			}
			double total = 0.0;
			for(int j = 0; j < degrees.length; j++){
				total = total + degrees[j];
			}
			total = total / degrees.length;
			return total;
		}
	}
	
	public Double propnPossibleEdges() {
		// PRE: -
		// POST: returns the proportion of actual edges to maximum possible edges;
		//       returns zero for a null graph or single-vertex graph
		if(VertexMap.size() == 1 || VertexMap == null){
			return 0.0;
		}
		else{
			int total = 0;
			int j = VertexMap.size() - 1;
			while(j > 0){
				total = total + j;
				j--;
			}
			int maxEdges = total;
			int actualEdges = numEdges();
			double propn = ((double)actualEdges / (double)maxEdges);
			return propn;
		}
	}
	
	/*
	 * END: required functions
	 */
	
	/*
	 * START: suggested functions
	 */
	
	public void addVertex(String s) {
		// Adds a new vertex s to the graph
		
		// TODO
	}
	
	public void deleteVertex(String s) {
		// Deletes a vertex n from the graph
		
		
		// TODO
	}

	public void deleteEdge(String a, String b) {
		// Delete an edge between a and b if present 
		
		// TODO
	}
	

	public Vector<String> readStopWordList (String fInName) throws IOException {
		// PRE: -
		// POST: reads in stopword list, returns words as vector of strings)

		Vector<String> eList = new Vector<String>();
		BufferedReader reader = new BufferedReader(new FileReader(fInName));
		String tempString = null;

			while ((tempString = reader.readLine()) != null) {
				java.util.StringTokenizer line = new java.util.StringTokenizer(tempString);
				while (line.hasMoreTokens()) {
					eList.add(new String(line.nextToken()));
				}
			}
			reader.close();
			return eList;
	}
	

	
	public void print() {
		
		// TODO
	}
	
	public static void main(String[] args) throws IOException {
		BipartiteGraph bg = new BipartiteGraph();
		Vector<Edge> eList;

		String inName = "C:\\Users\\Skorgenator\\Desktop\\COMP\\ass2\\in1";
		// replace this with your directory path
		try {
			eList = bg.readFromDirectory(inName);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		bg.print();

		UnipartiteGraph ug = new UnipartiteGraph();
		ug.setGraph(bg);
		
		String stopWordsName = "C:\\Users\\Skorgenator\\Desktop\\COMP\\ass2\\stop1\\stop1.txt";
		// replace this with your directory path
		UnipartiteGraph ug2 = new UnipartiteGraph();
		ug2.setGraphMinusStopwords(bg, stopWordsName);

		UnipartiteGraph ugNull = new UnipartiteGraph();
		ugNull.setNullGraph(bg);
		ugNull.print();
		System.out.println("edge weight btw cat, angry : " + Double.toString(ugNull.getEdgeWeight("cat","angry")));

		ug.filterNoise(ugNull, 0.05);
		ug.print();
	}

}
