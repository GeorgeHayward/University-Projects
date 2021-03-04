package graphs.student;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.math.*;

@SuppressWarnings("unused")
public class BipartiteGraph {

	/*
	 * START: graph representation
	 */
	private TreeMap<String, Vertex> WordMap;
	private TreeMap<String, Vertex> DocMap;
	private ArrayList<Edge> EdgeList;
	/*
	 * END: graph representation
	 */

	/*
	 * START: required functions
	 */

	public BipartiteGraph() {
		WordMap = new TreeMap<String, Vertex>();
		DocMap = new TreeMap<String, Vertex>();
		EdgeList = new ArrayList<Edge>();
	}
	
	public void setGraph(Vector<Edge> eList) {
		// PRE: -
		// POST: instantiates a bipartite graph based on a list of edges;
		// edges represent word-document pairs
		//Add to the Duplicate array list.
		Iterator<Edge> eIT = eList.iterator(); // Iterator to go through all the
											// inputs edges.
		while (eIT.hasNext()) {
			Edge tempEdge = (Edge) eIT.next(); // Create Temporary edge so that
												// information can be retrieved
												// from it.
			EdgeList.add(tempEdge);
			Vertex first = new Vertex(tempEdge.getFirst()); //Generate 2 Vertices based on that edge.
			Vertex second = new Vertex(tempEdge.getSecond());
			//If the wordmap already contains the word.
			if(getMap("word").containsKey(tempEdge.getFirst())){
				//Add 1 to the duplicate word count.
				getMap("word").get(tempEdge.getFirst()).incNum();
			}
			//Otherwise just add the new item to the TreeMap (wordMap)
			else{
				getMap("word").put(tempEdge.getFirst(), first);
			}
			//If the document already exists.
			if(getMap("doc").containsKey(tempEdge.getSecond())){
				//Add 1 to the duplicate doc count.
				getMap("doc").get(tempEdge.getSecond()).incNum();
			}
			//Just add the document to the doc map.
			else{
				getMap("doc").put(tempEdge.getSecond(), second);
			}
			//Create adjacency.
			getMap("word").get(tempEdge.getFirst()).addAdjAndInc(tempEdge.getSecond());
			getMap("doc").get(tempEdge.getSecond()).addAdjAndInc(tempEdge.getFirst());
			}			
		}
	

	public Integer numVertices() {
		// PRE: -
		// POST: returns number of vertices (both word and doc) in the graph
		//Simply add together the size of the two sets.
		return getMap("word").size() + getMap("doc").size();
	}

	public Integer numEdges() {
		// PRE: -
		// POST: returns number of edges in the graph
		int numEdges = 0;
		Collection<Vertex> list = getMap("word").values();
		Iterator<Vertex> wordIT = list.iterator();
		//Cycle through all words.
		while(wordIT.hasNext()){
			Vertex temp = (Vertex) wordIT.next();
			//Add number of adjacents to this particular vertex to the numEdges total.
			numEdges = numEdges + temp.getAdjs().size();
		}
		return numEdges;
	}

	public Integer numWordTypes() {
		// PRE: -
		// POST: returns number of word types in the graph (i.e. ignoring
		// duplicate words)
		//Return the wordMap size as this includes every word type.
		return  getMap("word").size();
	}

	public Integer numWordTokens() {
		// PRE: -
		// POST: returns number of word tokens in the graph (i.e. including
		// duplicate words)
		int totalWords = 0;
		Collection<Vertex> list = getMap("word").values();
		Iterator<Vertex> wordIT = list.iterator();
		//Go through all words.
		while(wordIT.hasNext()){
			Vertex temp = (Vertex) wordIT.next();
			//Add this word to the total.
			totalWords++;
			//Add the duplicate count as well.
			totalWords += temp.getNum();
		}
		return totalWords;
	}

	public Integer numTimesWordOccurs(String w) {
		// PRE: -
		// POST: returns the number of times word w occurs across all documents
		//Return the input word occurrence amount. The plus 1 is because if there is only 1 word, the getNum will be == to 0.
		return getMap("word").get(w).getNum() + 1;
	}

	public Integer numWordTypesInDoc(String d) {
		// PRE: d corresponds to a vertex in the graph
		// POST: returns number of different words (i.e. word types) that occur
		// in document d
		//Just get the adj size for the input document.
		return getMap("doc").get(d).getAdjs().size();
	}

	public Integer numWordTokensInDoc(String d) {
		// PRE: d corresponds to a vertex in the graph
		// POST: returns number of total words (i.e. including duplicates) that
		// occur in document d
		int total = 0;
		Iterator<String> adjIT = getMap("doc").get(d).getAdjs().iterator();
		while(adjIT.hasNext()){
			String temp = adjIT.next();
			// Go through all the adjs for the input doc and add the occurrences up in total.
			total = (int) (total + getMap("doc").get(d).getAdjs().get(temp).doubleValue());
		}
		return total;
	}

	public Integer numDocsWordOccursIn(String w) {
		// PRE: w corresponds to a vertex in the graph
		// POST: returns number of documents word w occurs in
		int total = 0;
		Collection<Vertex> list = getMap("doc").values();
		Iterator<Vertex> docIT = list.iterator();
		while(docIT.hasNext()){
			Vertex temp = (Vertex) docIT.next();
			//If this doc's adjacency list contains the input word. Add to total.
			if(temp.getAdjs().containsKey(w)){
				total++;
			}
		}
		return total;
	}

	public Integer numTimesWordOccursInDoc(String w, String d) {
		// PRE: d corresponds to a vertex in the graph
		// POST: returns number of times word w occurs in doc d
		int total = 0;
		//Go through the saved edge list and find out how many times the w occurs in a d.
		for(int i = 0; i < EdgeList.size(); i++){
			String Word = EdgeList.get(i).getFirst();
			String Doc = EdgeList.get(i).getSecond();
			if(Word.equals(w)){
				if(Doc.equals(d)){
					total++;
				}
			}
		}
		return total;
	}

	public ArrayList<String> listOfWordsInSingleDocs() {
		// PRE: -
		// POST: returns the words that only occur in single documents
		// the returned list should be ordered alphabetically and should contain
		// no duplicates.
		ArrayList<String> SingleDocWords = new ArrayList<String>();
		Collection<Vertex> list = getMap("word").values();
		Iterator<Vertex> wordIT = list.iterator();
		while(wordIT.hasNext()){
			Vertex temp = (Vertex) wordIT.next();
			//If the size is 1, that means it only occurs in 1 document since it only has 1 adjacency.
			if(temp.getAdjs().size() == 1){
				SingleDocWords.add(temp.getID());
			}
		}
		//Sort alphabetically.
		Collections.sort(SingleDocWords);
		return SingleDocWords;
	}

	public Double propnOfWordsInSingleDocs() {
		// PRE: -
		// POST: returns the words (out of all word types) that only occur in
		// single documents
		int allWords = numWordTypes(); //get total number of words.
		int singleWords = listOfWordsInSingleDocs().size(); //get words that appear in single docs.
		double propn = ((double)singleWords / (double)allWords); //divide the single words by all the words and return that double.
		return propn;
	}
	
	public String mostFreqWordToken() {
		// PRE: -
		// POST: returns the most frequent word
		// if there is more than one with maximum frequency, return the first
		// alphabetically
		int maxFreq = 0;
		int tempFreq = 0;
		ArrayList<String> maxFreqStrings = new ArrayList<String>();
		Collection<Vertex> list = getMap("word").values();
		Iterator<Vertex> wordIT = list.iterator();
		//Find the max frequency.
		while(wordIT.hasNext()){
			Vertex temp = (Vertex) wordIT.next();
			tempFreq = tempFreq + temp.getAdjs().size();
			tempFreq = tempFreq + temp.getNum();
			if(tempFreq > maxFreq){
				maxFreq = tempFreq;
			}
			tempFreq = 0;
		}
		list = getMap("word").values();
		wordIT = list.iterator();
		//Search all word for words with the max frequency.
		while(wordIT.hasNext()){
			Vertex temp = (Vertex) wordIT.next();
			tempFreq = tempFreq + temp.getAdjs().size();
			tempFreq = tempFreq + temp.getNum();
			if(tempFreq == maxFreq){
				//If this word has the max freq, add to the string list.
				maxFreqStrings.add(temp.getID());
			}
			tempFreq = 0;
		}
		//Sort alphabetically
		Collections.sort(maxFreqStrings);
		//Return the first alphabetically.
		return maxFreqStrings.get(0);
	}

	public String wordInMostDocs() {
		// PRE: -
		// POST: returns the word that occurs in the largest number of documents
		// if there is more than one with maximum frequency, return the first
		// alphabetically
		ArrayList<String> docFreqStrings = new ArrayList<String>();
		Collection<Vertex> list = getMap("word").values();
		Iterator<Vertex> wordIT = list.iterator();
		int maxDocs = 0;
		while(wordIT.hasNext()){
			Vertex temp = (Vertex) wordIT.next();
			if(temp.getAdjs().size() > maxDocs){
				//Get the max freq size.
				maxDocs = temp.getAdjs().size();
			}
		}
		list = getMap("word").values();
		wordIT = list.iterator();
		//Search all words to see if the have that max freq size. If so add to the list.
		while(wordIT.hasNext()){
			Vertex temp = (Vertex) wordIT.next();
			if(temp.getAdjs().size() == maxDocs){
				docFreqStrings.add(temp.getID());
			}
		}
		//Sort alphabetically and return the first.
		Collections.sort(docFreqStrings);
		return docFreqStrings.get(0);
	}

	/*
	 * END: required functions
	 */

	/*
	 * START: suggested functions
	 */

	public Vertex getVertex(String s, String t) {
		// Used for accessing vertex to e.g. add neighbours
		// t should be either "word" or "doc"
		if(t == "doc"){
			return DocMap.get(s);
		}
		else{
			return WordMap.get(s);			
		}
	}

	public NavigableSet<String> getVertexSet(String t) {
		// Returns ordered set of all vertices in the graph
		// t should be either "word" or "doc"
		NavigableSet<String> NS = new TreeSet<>();
		if(t == "doc"){
			Collection<Vertex> list = getMap("doc").values();
			Iterator<Vertex> IT = list.iterator();
			while(IT.hasNext()){
				Vertex temp = (Vertex) IT.next();
				NS.add(temp.getID());
			}
		}
		else{
			Collection<Vertex> list = getMap("word").values();
			Iterator<Vertex> IT = list.iterator();
			while(IT.hasNext()){
				Vertex temp = (Vertex) IT.next();
				NS.add(temp.getID());
			}
		}
		return NS;
	}

	public void addVertex(String s, String t) {
		// Adds a new vertex s to the graph
		// t should be either "word" or "doc"
		if(t == "doc"){
			getMap("doc").put(s, new Vertex(s));
		}
		else{
			getMap("word").put(s, new Vertex(s));
		}
	}

	public void deleteVertex(String s, String t) {
		// Deletes a vertex n from the graph
		// t should be either "word" or "doc"
		if(t == "doc"){
			getMap("doc").remove(s);
		}
		else{
			getMap("word").remove(s);
		}
	}

	public String getFirstVertexID(String t) {
		// Returns first vertex ID in TreeMap ordering
		// e.g. for starting a traversal
		// t should be either "word" or "doc"
		if(t == "doc"){
			return getMap("doc").firstKey();
		}
		else return getMap("word").firstKey();
	}

	public boolean containsVertex(String s, String t) {
		// Checks if s is a vertex in the graph
		// t should be either "word" or "doc"
		if(t == "doc"){
			if(getMap("doc").containsKey(s)){
				return true;
			}
			else return false;
		}
		else{
			if(getMap("word").containsKey(s)){
				return true;
			}
			else return false;
		}
	}

	public void print() {
		System.out.println("BipartiteGraph: ");
		System.out.println("DocMap: ");
		System.out.println(DocMap.toString());
		System.out.println("WordMap: ");
		System.out.println(WordMap.toString());
	}

	/*
	 * END: suggested functions
	 */

	/*
	 * START: added functions
	 */
	
	//Getter for the maps.
	public TreeMap<String, Vertex> getMap(String t){
		if(t == "word"){
			return this.WordMap;
		}
		else{
			return this.DocMap;
		}
	}
	
	/*
	 * END: added functions
	 */
	
	/*
	 * START: given functions
	 */

	public void setDefault() {
		// the sample set of edges (word-document pairs) from the specs

		Vector<Edge> eList = new Vector<Edge>();

		eList.add(new Edge("the", "d1"));
		eList.add(new Edge("purple", "d1"));
		eList.add(new Edge("and", "d1"));
		eList.add(new Edge("green", "d1"));
		eList.add(new Edge("cat", "d1"));
		eList.add(new Edge("was", "d1"));
		eList.add(new Edge("angry", "d1"));
		eList.add(new Edge("cat", "d1"));
		eList.add(new Edge("i", "d2"));
		eList.add(new Edge("heard", "d2"));
		eList.add(new Edge("that", "d2"));
		eList.add(new Edge("purple", "d2"));
		eList.add(new Edge("and", "d2"));
		eList.add(new Edge("green", "d2"));
		eList.add(new Edge("cat", "d2"));
		eList.add(new Edge("is", "d2"));
		eList.add(new Edge("angry", "d2"));
		eList.add(new Edge("and", "d2"));
		eList.add(new Edge("green", "d2"));
		eList.add(new Edge("all", "d3"));
		eList.add(new Edge("cat", "d3"));
		eList.add(new Edge("are", "d3"));
		eList.add(new Edge("my", "d3"));
		eList.add(new Edge("best", "d3"));
		eList.add(new Edge("friend", "d3"));

		this.setGraph(eList);
	}

	public Vector<Edge> readFromDirectory(String dirInName) throws IOException {
		// reads all documents from directory dirInName
		// returns vector of word-document pairs (e.g. the-d1)

		Vector<Edge> eList = new Vector<Edge>();

		File folder = new File(dirInName);
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isFile()) {
				BufferedReader fIn = new BufferedReader(new FileReader(fileEntry));
				String s;
				String w;

				String docFileName = fileEntry.getName();
				String docName = docFileName.substring(0, docFileName.lastIndexOf('.'));

				while ((s = fIn.readLine()) != null) {
					java.util.StringTokenizer line = new java.util.StringTokenizer(s);
					while (line.hasMoreTokens()) {
						w = line.nextToken();
						eList.add(new Edge(w, docName));
					}
				}
				fIn.close();
			}
		}
		return eList;
	}

	/*
	 * END: given functions
	 */

	public static void main(String[] args) {
		BipartiteGraph g = new BipartiteGraph();
		Vector<Edge> eList;

		String inName = "C:\\Users\\Skorgenator\\Desktop\\COMP\\ass2\\in1";
		// replace this with your directory path

		try {
			eList = g.readFromDirectory(inName);
			g.setGraph(eList);
		} catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		g.print();
	}
}
