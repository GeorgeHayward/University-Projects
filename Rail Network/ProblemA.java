package assg2p1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Set;

public class ProblemA {
    // connectionMap: Contains connected stations
    HashMap < String, ArrayList < String >> connectionMap;
    // allPaths: Contains all paths from outer key to inner key.
    HashMap < String, HashMap < String, ArrayList < ArrayList < String >>> > allPaths;
    // stationList: Contains a list of every input station.
    ArrayList < String > stationList;
    // cycleList: Contains every station which is apart of a cycle
    ArrayList < String > cycleList;
    // reversedMap = Connection map reversed.
	HashMap <String, ArrayList<String>> reversedMap;
    public ProblemA(String infile) {
        try {
            allPaths = new HashMap < String, HashMap < String, ArrayList < ArrayList < String >>> > ();
            stationList = new ArrayList < String > ();
            connectionMap = new HashMap < String, ArrayList < String >> ();
            processInput(infile);
            reversedMap = getReversedConnections();
            cycleList = getCycles();
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
        String currentLine;
        while ((currentLine = in .readLine()) != null) {
            String[] stations = currentLine.split(" ");
            connectionMap.putIfAbsent(stations[0], new ArrayList < String > ());
            connectionMap.get(stations[0]).add(stations[1]);
            if (!stationList.contains(stations[0])) {
                stationList.add(stations[0]);
            }
            if (!stationList.contains(stations[1])) {
                stationList.add(stations[1]);
            }
        }
        in .close();
    }

    /**
     * Returns the number of routes between two stations for all pairs 
     * of stations, as described in the assignment spec. 
     * 
     * @return the 2D hashmap containing the number of routes
     * 
     * 
    	1. Get all paths between all nodes
    	1.1. (optional) Do this using dynamic programming (trimming)
    	2. Get all cycles present in the graph and put the stations in those cycles into a set <Cycle Stations>
    	3. If any path contains a station from <Cycle Stations> then it is -1. Otherwise it is the amount of paths from a-b.
     *
     */
    public HashMap < String, HashMap < String, Integer >> findNumberOfRoutes() {
        HashMap < String, HashMap < String, Integer >> routeMap = new HashMap < String, HashMap < String, Integer >> ();
        for (int i = 0; i < stationList.size(); i++) {
            // Add the outer keys to both routeMap and allPaths
            allPaths.put(stationList.get(i), new HashMap < String, ArrayList < ArrayList < String >>> ());
            routeMap.put(stationList.get(i), new HashMap < String, Integer > ());
            // Add the inner keys to both routeMap and allPaths
            for (int j = 0; j < stationList.size(); j++) {
                // Inner key is now added and arrayList holding paths initialized. Can now insert paths into allPaths.
                allPaths.get(stationList.get(i)).put(stationList.get(j), new ArrayList < ArrayList < String >> ());
                routeMap.get(stationList.get(i)).put(stationList.get(j), getNumberDFS(stationList.get(i), stationList.get(j)));
            }
        }
        return routeMap;
    }

    // Finds number of routes from an origin station to a destination station.
    public int getNumberDFS(String origin, String destination) {
        // Get all paths from origin to destination.
        ArrayList < String > visited = new ArrayList < String > ();
        ArrayList < String > pathList = new ArrayList < String > ();
        pathList.add(origin);
        getPaths(origin, destination, visited, pathList);
        int numberOfPaths = allPaths.get(origin).get(destination).size();
        
        // No paths from origin to destination.
        if (allPaths.get(origin).get(destination).isEmpty()) {
            return 0;
        }
        // Either origin or destination is apart of the cycle list.
        if (cycleList.contains(origin) || cycleList.contains(destination)) {
        	return -1;
        }
        // Origin is equal to destination
        if (origin.equals(destination)) {
            return 0;
        }
        // There are paths from origin to destination
        else {
            // If there are cycles on any paths from origin to destination, return -1.
            if (containsCycles(origin, destination)) {
                return -1;
            }
            // Otherwise return the number of paths.
            else {
                return numberOfPaths;
            }
        }
    }

    // Recursive DFS. Get's all paths from origin to destination.
    public void getPaths(String origin, String destination, ArrayList < String > visited, ArrayList < String > pathList) {
        visited.add(origin);
        if (origin.equals(destination)) {
        	// Create new arraylist to avoid duplicate bug
        	ArrayList<String> newOne = new ArrayList<String>();
        	newOne.addAll(pathList);
            allPaths.get(pathList.get(0)).get(destination).add(newOne);
            visited.remove(origin);
            return;
        }
        if (connectionMap.containsKey(origin)) {
            for (int i = 0; i < connectionMap.get(origin).size(); i++) {
                if (!visited.contains(connectionMap.get(origin).get(i))) {
                    pathList.add(connectionMap.get(origin).get(i));
                    getPaths(connectionMap.get(origin).get(i), destination, visited, pathList);
                    pathList.remove(connectionMap.get(origin).get(i));
                }
            }
        }
        visited.remove(origin);
    }

    
    // Returns a list of every station in the current graph that is apart of a cycle.
    // Kosaraju's algorithm 
    public ArrayList< String > getCycles() {
    	// This will hold a list of strongly connected components.
    	ArrayList<ArrayList<String>> SCCList = new ArrayList<ArrayList<String>>();
    	// Holds stations by finish time in reverse order.
    	Deque<String> stack = new ArrayDeque<>();
    	// Holds visited vertices for DFS
    	ArrayList<String> visited = new ArrayList<String>();
    	// Populate stack with stations with the station finishing last at the top.
    	for (int i = 0; i < stationList.size(); i++) {
    		if(visited.contains(stationList.get(i))) {
    			continue;
    		}
    		DFSUtil(stationList.get(i), visited, stack);
    	}
    	    	
    	// Do a DFS based off vertex finish time in decreasing order on reversed graph
    	visited.clear();
    	while(!stack.isEmpty()) {
    		String station = stack.poll();
    		if(visited.contains(station)) {
    			continue;
    		}
    		ArrayList<String> SCC = new ArrayList<String>();
    		DFSUtilForReverseGraph(station, visited, SCC);
    		SCCList.add(SCC);
    	}
    	ArrayList< String > stationsInCycle = new ArrayList<String>();
    	for(int k = 0; k < SCCList.size(); k++) {
    		ArrayList<String> aSCC = SCCList.get(k);
    		// If there is a cycle
    		if(aSCC.size() > 1) {
    			// Add all parts of that cycle to stationInCycle.
    			for(int z = 0; z < aSCC.size(); z++) {
    				if(!stationsInCycle.contains(aSCC.get(z))) {
    					stationsInCycle.add(aSCC.get(z));
    				}
    			}
    		}
    	}
    	// Get all the SCC's greater than one and put into an array list.
    	return stationsInCycle;
    	
    }
    
    public void DFSUtil(String station, ArrayList<String> visited, Deque<String> stack) {
    	visited.add(station);
    	if(connectionMap.containsKey(station)){
    	for(int i = 0; i < connectionMap.get(station).size(); i++) {
    		if(visited.contains(connectionMap.get(station).get(i))) {
    			continue;
    		}
    		DFSUtil(connectionMap.get(station).get(i), visited, stack);
    	}
    	}
    	stack.offerFirst(station);
    }
    
    public void DFSUtilForReverseGraph(String station, ArrayList<String> visited, ArrayList<String> SCC) {
    	visited.add(station);
    	SCC.add(station);
    	
    	if(reversedMap.containsKey(station)) {
    	for(int i = 0; i < reversedMap.get(station).size(); i++) {
    		if (visited.contains(reversedMap.get(station).get(i))) {
    			continue;
    		}
    		DFSUtilForReverseGraph(reversedMap.get(station).get(i), visited, SCC);
    	}
    	}
    }
    
    // Returns true if any route from origin to destination contains a cycle station.
    public boolean containsCycles(String origin, String destination) {
    	// If either the origin or destination is inside cycle list. Return true.
    	if(cycleList.contains(origin) || cycleList.contains(destination)) {
    		return true;
    	}
    	// origin / destination are not in cycle list, check other elements
    	else {
    		// cycleList.get(i) = elements that are in the cycle list.
    		for(int i = 0; i < cycleList.size(); i++){
    			// If there are paths between the origin and destination
    			if(!allPaths.get(origin).get(destination).isEmpty()) {
    				// allPaths.get(origin).get(destination).get(j) == a path from origin to destination
    				for(int j = 0; j < allPaths.get(origin).get(destination).size(); j++) {
    					if(allPaths.get(origin).get(destination).get(j).contains(cycleList.get(i))) {
    						return true;
    					}
    				}
    			}
    		}
    	}
    	return false;
    }
    
    // Returns a reversed connectionMap. All directions are reveresed.
    public HashMap < String, ArrayList < String >> getReversedConnections(){
    	// Will contain the reversed map
    	HashMap <String, ArrayList < String>> reversedMap = new HashMap <String, ArrayList <String>>();
    	// Get the set of key strings
    	Set<String> keys = connectionMap.keySet();
    	// Iterate through them
    	for(String entry : keys) {
    		for(int i = 0; i < connectionMap.get(entry).size(); i++) {
    			// If the reversed map doesn't yet have this origin station
    			if(!reversedMap.containsKey(connectionMap.get(entry).get(i))) {
    				// Add it to the reversedMap and add the entry to it's array list.
    				reversedMap.put(connectionMap.get(entry).get(i), new ArrayList<String>());
    				reversedMap.get(connectionMap.get(entry).get(i)).add(entry);
    			}
    			// Reverse map already contains this origin key, so just add the entry to its destinations.
    			else {
    				reversedMap.get(connectionMap.get(entry).get(i)).add(entry);
    			}
    		}
    	}
    	return reversedMap;
    }
    
}