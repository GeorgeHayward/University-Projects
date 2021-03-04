package assg2p1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ProblemB {
    String source;
    String sink;
    int noOfStations;
    int noOfSegments;
    HashMap < String, Integer > stations;
    HashMap < String, HashMap < String, Integer >> segments;
    ArrayList < String > stationIndexer;
    int finalResult;


    public ProblemB(String infile) {
        try {
            stations = new HashMap < String, Integer > ();
            segments = new HashMap < String, HashMap < String, Integer >> ();
            stationIndexer = new ArrayList < String > ();
            finalResult = 0;
            processInput(infile);
        } catch (IOException e) {
            System.out.println("Exception encountered: " + e);
        }
    }

    /**
     * A helper method to process the input file. 
     * 
     * @param infile the file containing the input
     * @throws IOException
     */
    public void processInput(String infile) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(infile));
        // Get first line: stationA and stationB
        String currentLine = in .readLine();
        String[] AandB = currentLine.split(" ");
        source = AandB[0];
        sink = AandB[1];
        // Add source to stationIndexer
        stationIndexer.add(source);
        // Get second line: 
        currentLine = in .readLine();
        AandB = currentLine.split(" ");
        noOfStations = Integer.parseInt(AandB[0]);
        noOfSegments = Integer.parseInt(AandB[1]);
        // Get stations and there devices. noOfStations lines.
        for (int i = 0; i < noOfStations; i++) {
            currentLine = in .readLine();
            AandB = currentLine.split(" ");
            stations.put(AandB[0], Integer.parseInt(AandB[1]));
            stationIndexer.add(AandB[0]);
        }
        // Get connections and the connections devices.
        for (int i = 0; i < noOfSegments; i++) {
            currentLine = in .readLine();
            String[] segment = currentLine.split(" ");
            // Add the segments to segments map.
            if (segments.containsKey(segment[0])) {
                segments.get(segment[0]).put(segment[1], Integer.parseInt(segment[2]));
            } else {
                HashMap < String, Integer > temp = new HashMap < String, Integer > ();
                temp.put(segment[1], Integer.parseInt(segment[2]));
                segments.put(segment[0], temp);
            }
        }
        // Add the sink to the end of the index. (right order)
        stationIndexer.add(sink); 
        in.close();
    }


    // Generates an adjacency matrix of the given graph
    public int[][] generateGraph() {
        int[][] matrix = new int[stationIndexer.size()][stationIndexer.size()];
        for (int i = 0; i < stationIndexer.size(); i++) {
            for (int j = 0; j < stationIndexer.size(); j++) {
                // If i = j. Then get the item in the stationIndexer and set to stations int.
                if (i == j) {
                    if (stations.containsKey(stationIndexer.get(i))) {
                        matrix[i][j] = stations.get(stationIndexer.get(i));
                    }
                }
                // Otherwise
                else {
                    matrix[i][j] = 0;
                    String stationA = stationIndexer.get(i);
                    String stationB = stationIndexer.get(j);
                    // Check if segments contains stationA
                    if (segments.containsKey(stationA)) {
                        if (segments.get(stationA).containsKey(stationB)) {
                            matrix[i][j] = segments.get(stationA).get(stationB);
                        }
                    }
                    // Check if segments contains stationB
                    if (segments.containsKey(stationB)) {
                        if (segments.get(stationB).containsKey(stationA)) {
                            matrix[i][j] = segments.get(stationB).get(stationA);
                        }
                    }
                }
            }
        }
        return matrix;
    }


    /**
     * Returns the minimum number of device failures that will cause 
     * the two stations in the input file to be disconnected 
     * (please refer to the assignment spec for the details)
     * 
     * @return an integer denoting the minimum number of device failures
     * 
     */
    
    public Integer computeMinDevice() {
        // Generate matrix, contains distances between every station. (if they are adjacent)
        int[][] matrix = generateGraph();
        // Map to hold the parents of each node. Used for backtracking path and updating. Get key will get key's parent in path.
        HashMap < String, String > parent = new HashMap < String, String > ();
        // Change the flow of the path (using min cuts) whilst there is still a way to get from source to sink. Add to final result with the value of every min cut made.   
        while (bfs(matrix, source, sink, parent)) {
            int pathFlow = Integer.MAX_VALUE;
            // Current station being viewed in the BFS path.
            String station = sink;
            // Will hold the previous node visited in the BFS of station.
            String parentStation;
            // While station isn't source (can't go further back in the path than source)
            while (!station.equals(source)) {
            	// Get the parent of the current station
                parentStation = parent.get(station);
                // Get minimum between current pathflow and the weight of the path between parentStation and station.
                pathFlow = Math.min(pathFlow, matrix[stationIndexer.indexOf(parentStation)][stationIndexer.indexOf(station)]);
                // If a station node (not edge), get the flow of that station.
                if (!station.equals(sink)) {
                    pathFlow = Math.min(pathFlow, matrix[stationIndexer.indexOf(station)][stationIndexer.indexOf(station)]);
                }
                // Update station to previous station in path
                station = parent.get(station);
            }
            
            // Reset station to start at sink (the end)
            station = sink;
            // Update weights of graph based on new pathFlow.
            while (!station.equals(source)) {
            	// Get the parent of the current station
                parentStation = parent.get(station);                
                // Update residual capacities of the edges and reverse edges along the path
                matrix[stationIndexer.indexOf(parentStation)][stationIndexer.indexOf(station)] = matrix[stationIndexer.indexOf(parentStation)][stationIndexer.indexOf(station)] - pathFlow;
                matrix[stationIndexer.indexOf(station)][stationIndexer.indexOf(parentStation)] = matrix[stationIndexer.indexOf(station)][stationIndexer.indexOf(parentStation)] + pathFlow;
                // Change the weight of station nodes
                matrix[stationIndexer.indexOf(station)][stationIndexer.indexOf(station)] = matrix[stationIndexer.indexOf(station)][stationIndexer.indexOf(station)] - pathFlow;
                // Update station to previous station in path
                station = parent.get(station);
            }
            // Add pathflow to final result.
            finalResult = finalResult + pathFlow;
        }
        // Return the final result, the total (minimum) amount cut value done to the graph.
        return finalResult;
    }

    // Returns true if there is a path from source origin to destination in residual graph. Also fills parent[] to store the path 
    public boolean bfs(int rGraph[][], String origin, String destination, HashMap < String, String > parent) {
        ArrayList < String > visited = new ArrayList < String > ();
        LinkedList < String > queue = new LinkedList < String > ();
        queue.add(origin);
        visited.add(origin);
        // Origin doesn't have a parent since it's the source. Make parent null.
        parent.put(origin, null);
        // BFS Loop
        while (!queue.isEmpty()) {
            // Get station to inspect.
            String station = queue.poll();
            // Cycle through every station
            for (int i = 0; i < stationIndexer.size(); i++) {
                // If station being inspected is the destination, then return true because there is a BFS path from origin to destination.
                if (station.equals(destination)) {
                    return true;
                }
                // If the index's weight is greater than 0 (it's viable for a path) AND it hasn't been visited yet.
                if ((rGraph[stationIndexer.indexOf(station)][stationIndexer.indexOf(stationIndexer.get(i))] > 0) && (!visited.contains(stationIndexer.get(i)))) {
                    // If station isn't origin: check for weight
                    if (!station.equals(origin)) {
                        // If the weight of the index is greater than 0, proceed.
                        if (rGraph[stationIndexer.indexOf(station)][stationIndexer.indexOf(station)] > 0) {
                        	// Add to queue, visit and make parent (for path backtracking in computeMinDevice).
                            queue.add(stationIndexer.get(i));
                            visited.add(stationIndexer.get(i));
                            parent.put(stationIndexer.get(i), station);
                        }
                        // Station is origin, don't check for weight
                    } else {
                    	// Add to queue, visit and make parent (for path backtracking in computeMinDevice).
                        queue.add(stationIndexer.get(i));
                        visited.add(stationIndexer.get(i));
                        parent.put(stationIndexer.get(i), station);
                    }
                }
            }
        }
        // No paths found
        return false;
    }
}