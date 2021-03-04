package graphs.student;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Vector;

import org.junit.Test;


public class AllTest {

	String PATH = "C:\\Users\\Skorgenator\\Desktop\\COMP\\ass2\\in1";
	String stopWordsName1 = "C:\\Users\\Skorgenator\\Desktop\\COMP\\ass2\\stop1\\stop1.txt";
	String PATH2 = "C:\\Users\\Skorgenator\\Desktop\\COMP\\ass2\\in2";

	/*
	 * Bipartite graph: small graph 
	 */
	
	@Test
	public void testNumVertices() {
		BipartiteGraph g = new BipartiteGraph();
		Vector<Edge> eList;
		try {
			eList = g.readFromDirectory(PATH);
			g.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Integer.valueOf(19), g.numVertices());
	}

	@Test
	public void testNumEdges() {
		BipartiteGraph g = new BipartiteGraph();
		Vector<Edge> eList;
		try {
			eList = g.readFromDirectory(PATH);
			g.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Integer.valueOf(22), g.numEdges());
	}

	@Test
	public void testNumWordTypes() {
		BipartiteGraph g = new BipartiteGraph();
		Vector<Edge> eList;
		try {
			eList = g.readFromDirectory(PATH);
			g.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Integer.valueOf(16), g.numWordTypes());
	}

	@Test
	public void testNumWordTokens() {
		BipartiteGraph g = new BipartiteGraph();
		Vector<Edge> eList;
		try {
			eList = g.readFromDirectory(PATH);
			g.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Integer.valueOf(25), g.numWordTokens());
	}

	@Test
	public void testNumTimesWordOccurs() {
		BipartiteGraph g = new BipartiteGraph();
		Vector<Edge> eList;
		try {
			eList = g.readFromDirectory(PATH);
			g.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Integer.valueOf(4), g.numTimesWordOccurs("cat"));
	}

	@Test
	public void testNumWordTypesInDoc() {
		BipartiteGraph g = new BipartiteGraph();
		Vector<Edge> eList;
		try {
			eList = g.readFromDirectory(PATH);
			g.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Integer.valueOf(7), g.numWordTypesInDoc("d1"));
	}

	@Test
	public void testNumWordTokensInDoc() {
		BipartiteGraph g = new BipartiteGraph();
		Vector<Edge> eList;
		try {
			eList = g.readFromDirectory(PATH);
			g.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Integer.valueOf(8), g.numWordTokensInDoc("d1"));
	}

	@Test
	public void testNumDocsWordOccursIn() {
		BipartiteGraph g = new BipartiteGraph();
		Vector<Edge> eList;
		try {
			eList = g.readFromDirectory(PATH);
			g.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Integer.valueOf(2), g.numDocsWordOccursIn("purple"));
	}

	@Test
	public void testNumTimesWordOccursInDoc() {
		BipartiteGraph g = new BipartiteGraph();
		Vector<Edge> eList;
		try {
			eList = g.readFromDirectory(PATH);
			g.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Integer.valueOf(2), g.numTimesWordOccursInDoc("cat", "d1"));
	}

	@Test
	public void testListOfWordsInSingleDocs() {
		BipartiteGraph g = new BipartiteGraph();
		String[] soloWords = {"all", "are", "best", "friend", "heard", "i", "is", "my", "that", "the", "was"};

		Vector<Edge> eList;
		try {
			eList = g.readFromDirectory(PATH);
			g.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertArrayEquals(soloWords, g.listOfWordsInSingleDocs().toArray());
	}

	@Test
	public void testPropnOfWordsInSingleDocs() {
		BipartiteGraph g = new BipartiteGraph();
		Double ans = 0.6875;

		Vector<Edge> eList;
		try {
			eList = g.readFromDirectory(PATH);
			g.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(ans, g.propnOfWordsInSingleDocs());
	}

	@Test
	public void testMostFreqWordToken() {
		BipartiteGraph g = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = g.readFromDirectory(PATH);
			g.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals("cat", g.mostFreqWordToken());
	}

	@Test
	public void testWordInMostDocs() {
		BipartiteGraph g = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = g.readFromDirectory(PATH);
			g.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals("cat", g.wordInMostDocs());
	}

	/*
	 * Unipartite graph tests: small graph
	 */
	
	@Test
	public void testGetEdgeWeight() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		ug.setGraph(bg);

		assertEquals(4.0, ug.getEdgeWeight("cat", "and"), 0.01);
	}
	
	@Test
	public void testUNumVertices() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		ug.setGraph(bg);

		assertEquals(Integer.valueOf(16), ug.numVertices());
	}

	@Test
	public void testUNumEdges() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		ug.setGraph(bg);

		assertEquals(Integer.valueOf(62), ug.numEdges());
	}

	@Test
	public void testDegreeWord() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		ug.setGraph(bg);

		assertEquals(Integer.valueOf(8), ug.degreeWord("heard"));
	}

	@Test
	public void testNumVerticesDegreeK() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		ug.setGraph(bg);

		assertEquals(Integer.valueOf(2), ug.numVerticesDegreeK(6));
	}

	@Test
	public void testNumVerticesConnectedAll() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		ug.setGraph(bg);

		assertEquals(Integer.valueOf(1), ug.numVerticesConnectedAll());
	}

	@Test
	public void testAvgDegreeOfGraph() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		ug.setGraph(bg);

		assertEquals(Double.valueOf(7.75), ug.avgDegreeOfGraph(), 0.01);
	}

	@Test
	public void testPropnPossibleEdges() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		ug.setGraph(bg);

		assertEquals(Double.valueOf(0.5166), ug.propnPossibleEdges(), 0.01);
	}

	@Test
	public void testUStopNumVertices() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		ug.setGraphMinusStopwords(bg, stopWordsName1);

		assertEquals(Integer.valueOf(14), ug.numVertices());
	}

	@Test
	public void testUStopNumEdges() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		ug.setGraphMinusStopwords(bg, stopWordsName1);

		assertEquals(Integer.valueOf(42), ug.numEdges());
	}

	@Test
	public void testUNullNumVertices() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		ug.setNullGraph(bg);

		assertEquals(Integer.valueOf(16), ug.numVertices());
	}

	@Test
	public void testUNullNumEdges() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		ug.setNullGraph(bg);

		assertEquals(Integer.valueOf(120), ug.numEdges());
	}

	@Test
	public void testUNullGetEdgeWeight() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		ug.setNullGraph(bg);

		assertEquals(1.4144, ug.getEdgeWeight("cat", "the"), 0.01);
	}

	@Test
	public void testUFilterBigPNumVertices() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		UnipartiteGraph ugNull = new UnipartiteGraph();
		ugNull.setNullGraph(bg);
		ug.setGraph(bg);
		ug.filterNoise(ugNull, 0.95);

		System.out.print(ug.numVertices());
		assertEquals(Integer.valueOf(16), ug.numVertices());
	}

	@Test
	public void testUFilterBigPNumEdges() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		UnipartiteGraph ugNull = new UnipartiteGraph();
		ugNull.setNullGraph(bg);
		ug.setGraph(bg);
		ug.filterNoise(ugNull, 0.95);

		assertEquals(Integer.valueOf(20), ug.numEdges());
	}

	@Test
	public void testUFilterBigPGetEdgeWeight() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		UnipartiteGraph ugNull = new UnipartiteGraph();
		ugNull.setNullGraph(bg);
		ug.setGraph(bg);
		ug.filterNoise(ugNull, 0.95);

		assertEquals(3.0, ug.getEdgeWeight("cat", "and"), 0.01);
	}

	@Test
	public void testUFilterBigPAvgDegreeOfGraph() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		UnipartiteGraph ugNull = new UnipartiteGraph();
		ugNull.setNullGraph(bg);
		ug.setGraph(bg);
		ug.filterNoise(ugNull, 0.95);

		assertEquals(Double.valueOf(2.5), ug.avgDegreeOfGraph(), 0.01);
	}

	@Test
	public void testUFilterBigPPropnPossibleEdges() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		UnipartiteGraph ugNull = new UnipartiteGraph();
		ugNull.setNullGraph(bg);
		ug.setGraph(bg);
		ug.filterNoise(ugNull, 0.95);

		System.out.print(ug.numVertices());
		assertEquals(Double.valueOf(0.1666), ug.propnPossibleEdges(), 0.01);
	}

	/*
	 * Bipartite: bigger graph
	 */
	
	@Test
	public void testNumVerticesBigger() {
		BipartiteGraph g = new BipartiteGraph();
		Vector<Edge> eList;
		try {
			eList = g.readFromDirectory(PATH2);
			g.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Integer.valueOf(433), g.numVertices());
	}

	@Test
	public void testNumEdgesBigger() {
		BipartiteGraph g = new BipartiteGraph();
		Vector<Edge> eList;
		try {
			eList = g.readFromDirectory(PATH2);
			g.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Integer.valueOf(531), g.numEdges());
	}

	@Test
	public void testUNumVerticesBigger() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH2);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		ug.setGraph(bg);

		assertEquals(Integer.valueOf(429), ug.numVertices());
	}

	@Test
	public void testUNumEdgesBigger() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH2);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		ug.setGraph(bg);

		assertEquals(Integer.valueOf(43788), ug.numEdges());
	}

	@Test
	public void testAvgDegreeOfGraphBigger() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH2);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		ug.setGraph(bg);

		assertEquals(Double.valueOf(204.13986), ug.avgDegreeOfGraph(), 0.01);
	}

	@Test
	public void testPropnPossibleEdgesBigger() {
		BipartiteGraph bg = new BipartiteGraph();

		Vector<Edge> eList;
		try {
			eList = bg.readFromDirectory(PATH2);
			bg.setGraph(eList);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}

		UnipartiteGraph ug = new UnipartiteGraph();
		ug.setGraph(bg);

		assertEquals(Double.valueOf(0.47696), ug.propnPossibleEdges(), 0.01);
	}

}
