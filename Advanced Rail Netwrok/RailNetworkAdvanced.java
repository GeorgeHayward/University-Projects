
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class RailNetworkAdvanced {

	private TreeMap<String,Station> stationList;
	private TreeMap<String, String[]> trainLineList;
	private HashMap<String, HashMap<String, Double>> allDistances;
	private HashMap<String, HashMap<String, List<String>>> allMinDistances;
	
	public RailNetworkAdvanced(String trainData, String connectionData, String lineData) {
		stationList = new TreeMap<>();
		trainLineList = new TreeMap<>();
		
		try {	
			readLinesData(lineData);
			readStationData(trainData);
			readConnectionData(connectionData);
//			allDistances = generateAllDistances();
//			allMinDistances = generateAllRouteMinDistances();
		}
		catch (IOException e) {
			System.out.println("Exception encountered: " + e);
		}
	}
	
	/**
	 * Reads the CSV file containing information about the lines
	 * 
	 * @param infile
	 * @throws IOException
	 */
	public void readLinesData(String infile) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(infile));
		
		//initial variable
		String s;
		//skip the first line - header
		s = in.readLine();
		//running the loop through all data - each line
		while((s = in.readLine()) != null) {
			//split the line into sub-components
			String[] data = s.split(",");
			//initial the list contain the list of station on the same line
			String[] trainLine = new String[Integer.parseInt(data[4])];
			//assign data into the trainLineList
			String code = data[0];
			trainLine[0] = data[2];
			trainLine[trainLine.length -1] = data[3];
			trainLineList.put(code, trainLine);
		}
		in.close();
	}
	/**
	 * Reads the CSV file containing information about the stations and 
	 * populate the TreeMap<String,Station> stationList. Each row of 
	 * the CSV file contains the name, latitude and longitude coordinates
	 * of the station.
	 * 
	 * You need to make a Station object for each row and add it to the 
	 * TreeMap<String,Station> stationList where the key value is the 
	 * name of the station (as a String).
	 * 
	 * @param infile	   the path to the file
	 * @throws IOException if the file is not found
	 */
	public void readStationData(String infile) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(infile));

		//initial variable
		String s;
		// Get the header of each column
		String[] property = in.readLine().split(",");
		//running the loop through all data - each line
		while((s = in.readLine()) != null) {
			//split the line into sub-components
			String[] data = s.split(",");
			//create the new station
			Station station = new Station(data[0], Double.parseDouble(data[1]), Double.parseDouble(data[2]));
			//assign the data into stationList
			stationList.put(data[0], station);
			
			//running the loop to get the line data based on each station (from cell 4 to 19 in csv file)
			for(int i = 3; i < 19; i++){
				//if there is information about line
				if(!data[i].equals("")){
					//using function addLine from station.java to add line information according with each station
					station.addLine(property[i]);
					//getting the list of station on the selected line
					String[] trainLine = trainLineList.get(property[i]);
					//assign the station to the correct position/location on the line
					trainLine[Integer.parseInt(data[i])-1] = station.getName();
					trainLineList.put(property[i], trainLine);
				}
			}
		}
		in.close();
	}
	/**
	 * Reads the CSV file containing information about connectivity between 
	 * adjacent stations, and update the stations in stationList so that each
	 * Station object has a list of adjacent stations.
	 * 
	 * Each row contains two Strings separated by comma. To obtain the distance
	 * between the two stations, you need to use the latitude and longitude 
	 * coordinates together with the computeDistance() methods defined below
	 * 
	 * @param infile	   the path to the file
	 * @throws IOException if the file is not found
	 */	
	public void readConnectionData(String infile) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(infile));

		//initial variable
		String s;
		//running the loop through all data - each line (there is no header in the file)
		while((s = in.readLine()) != null) {
			//split the line into sub-components
			String[] data = s.split(",");
			//assign the sub-components into variables
			String stationA = data[0];
			String stationB = data[1];
			//compute the distance between 2 stations using given function computeDistance(String a, String b)
			int distance = computeDistance(stationA, stationB);
			//using addNeighbour function from Station.java to add adjacent stations
			stationList.get(stationA).addNeighbour(stationList.get(stationB), distance);
			stationList.get(stationB).addNeighbour(stationList.get(stationA), distance);
		}
		in.close();
	}
	
	//Populate neighbors based on the train lines
	public void populateNeighbors(){
		for (Map.Entry<String, String[]> entry : trainLineList.entrySet()) {
			//getting line code and its station list
			String line = entry.getKey();
			String[] stationLine = entry.getValue();
			//running loop through each station in the list of line
			for(int i =0; i < stationLine.length; i++){
				//getting the selected station
				Station station = stationList.get(stationLine[i]);
				//adding its adjacent stations including previous and next stations (each station has 2 neighbors)
				//checking whether selected station is not first station in the list
				if(i > 0){
					Station prev = stationList.get(stationLine[i-1]);
					station.addLineNeighbour(prev, line);
				}
				//checking whether selected station is not second station in the list
				if(i < stationLine.length-1){
					Station next = stationList.get(stationLine[i+1]);
					station.addLineNeighbour(next, line);
				}
			}
		}
	}
	
	/**
	 * Given the latitude and longitude coordinates of two locations x and y, 
	 * return the distance between x and y in metres using Haversine formula,
	 * rounded down to the nearest integer.
	 * 
	 * Note that two more methods are provided below for your convenience 
	 * and you should not directly call this method
	 * 
	 * source://www.geeksforgeeks.org/haversine-formula-to-find-distance-between-two-points-on-a-sphere/
	 * 
	 * @param lat1 latitude coordinate of x
	 * @param lon1 longitude coordinate of x
	 * @param lat2 latitude coordinate of y
	 * @param lon2 longitude coordinate of y
	 * @return distance betwee
	 */
	public static int computeDistance(double lat1, double lon1, double lat2, double lon2) {
        // distance between latitudes and longitudes 
        double dLat = Math.toRadians(lat2 - lat1); 
        double dLon = Math.toRadians(lon2 - lon1); 
  
        // convert to radians 
        lat1 = Math.toRadians(lat1); 
        lat2 = Math.toRadians(lat2); 
  
        // apply formulae 
        double a = Math.pow(Math.sin(dLat / 2), 2) +  
                   Math.pow(Math.sin(dLon / 2), 2) *  
                   Math.cos(lat1) *  
                   Math.cos(lat2); 
        double rad = 6371.0; 
        Double c = 2 * Math.asin(Math.sqrt(a)); 
        Double distance = rad * c * 1000;
        return distance.intValue(); 
	}	
	
	/**
	 * Compute the distance between two stations in metres, where the stations
	 * are given as String objects
	 * 
	 * @param a		the first station
	 * @param b		the second station
	 * @return		the distance between the two stations in metres
	 */
	public int computeDistance(String a, String b) {
		Station u = stationList.get(a);
		Station v = stationList.get(b);
		return computeDistance(u.getLatitude(),u.getLongitude(),
							   v.getLatitude(),v.getLongitude());
	}
	
	/**
	 * Compute the distance between two stations in metres, where the stations
	 * are given as Station objects
	 * 
	 * @param a		the first station
	 * @param b		the second station
	 * @return		the distance between the two stations in metres
	 */
	public int computeDistance(Station a, Station b) {
		return computeDistance(a.getLatitude(),a.getLongitude(),
							   b.getLatitude(),b.getLongitude());
	}

	/**
	 * The method finds the shortest route (in terms of distance travelled) 
	 * between the origin station and the destination station.
	 * The route is returned as an ArrayList<String> containing the names of 
	 * the stations along the route, including the origin and the destination 
	 * stations.
	 * 
	 * If the route cannot be completed (there is no path between origin and
	 * destination), then return an empty ArrayList<String>
	 * 
	 * If the origin or the destination stations are not in the list of stations,
	 * return an empty ArrayList<String>. 
	 * 
	 * If the origin and the destination stations are the same, return an 
	 * ArrayList<String> containing the station.
	 * 
	 * @param origin		the starting station
	 * @param destination	the destination station
	 * @return
	 */
	public ArrayList<String> routeMinDistance(String origin, String destination){
		if (!stationList.containsKey(origin) || !stationList.containsKey(destination)) {
			return new ArrayList<String>();
		}
		if (origin.equals(destination)) {
			ArrayList<String> ans = new ArrayList<String>();
			ans.add(origin);
			return ans;
		}
		/*
		 * INSERT YOUR CODE HERE
		 */
	
		//initial all variables
		ArrayList<String> result = new ArrayList<String>();
		int size = stationList.size();
		//create the treemap holding the key is station name and value is the shortest distance from  
		//origin station to each stations
		TreeMap<String,Integer> distanceList = new TreeMap<String,Integer>();
		//create the treemap holding the key is station name and value is true or false, which means 
		//whether current station has been visited and has the shortest distance between it and origin station
		TreeMap<String,Boolean> visited = new TreeMap<String,Boolean>();
		//create the treemap holding the nearest station of the current station between it and origin station
		TreeMap<String,String> path = new TreeMap<String,String>();
		//compute the list of station names
		String[] stationNameList = stationList.keySet().toArray(new String[stationList.keySet().size()]);
		//initial value for all variables
		for(int i = 0; i < size; i++) {
			distanceList.put(stationNameList[i], Integer.MAX_VALUE);
			visited.put(stationNameList[i], false);
			path.put(stationNameList[i], "Empty");
		}
		//assign the distance for origin station (origin to origin is 0 distance)
		distanceList.put(origin, 0);
		//assign the nearest station to origin station (origin to origin has 0 station in the middle)
		path.put(origin,"None");
		//running the loop to find the shortest path from origin station to all stations
		for(int i = 0; i < size; i++) {
			//initial variable
			String nextStation = null;
			int shortestDistance = Integer.MAX_VALUE;
			//choosing the station has the minimum distance between it and origin, which has not been visited yet
			for(int x = 0; x < size; x++) {
				if(!visited.get(stationNameList[x]) && distanceList.get(stationNameList[x]) < shortestDistance) {
					nextStation = stationNameList[x];
					shortestDistance = distanceList.get(stationNameList[x]);
				}
			}
			//if all station has been visited, do nothing and start the next loop
			if(nextStation != null) {
				//if the needed station is found, mark it as visited
				if(!visited.get(nextStation)) {
					visited.put(nextStation, true);
				}
				//initial variable to get and store data of adjacent stations of current station
				TreeMap<Station,Integer> temp = stationList.get(nextStation).getAdjacentStations();
				TreeMap<String, Integer> adjStation = new TreeMap<String, Integer>();
				//assign value into the adjStation, where key is the adjacent station names and 
				//value is the distance between current station and adjacent stations
				for(Map.Entry<Station, Integer> entry: temp.entrySet()) {
					adjStation.put(entry.getKey().getName(), entry.getValue());
				}
				//compute the adjacent station name as a list of string to use
				String[] adjStationName = adjStation.keySet().toArray(new String[adjStation.keySet().size()]);
				//running loop through each adjacent stations
				for(int y = 0; y < adjStation.size(); y++) {
					int distance = adjStation.get(adjStationName[y]);
					//update the shortest distance between adjacent stations and origin station and
					//update the nearest station between adjacent stations and origin station
					if((distance + shortestDistance) < distanceList.get(adjStationName[y])) {
						path.put(adjStationName[y], nextStation);
						distanceList.put(adjStationName[y], distance + shortestDistance);
					}
				}
			}
		}
		
		//construct the result which is the shortest path from origin station to destination station	
		String name = destination;
		while(path.get(name) != null) {
			result.add(0,name);
			name = path.get(name);
		}
		
		//System.out.println(result);
		
		//return the empty list if the first and last item of the result is incorrect (the path cannot be completed)
		if(!origin.equals(result.get(0)) || !destination.equals(result.get(result.size()-1))) {
			return new ArrayList<String>();
		}
		
		return result;
	}

	
	/**
	 * Given a route between two stations, compute the total distance 
	 * of this route.
	 * 
	 * @param path	the list of stations in the route (as String objects)
	 * @return		the length of the route between the first station
	 * 				and last station in the list	
	 */
	public int findTotalDistance(ArrayList<String> path) {
		int distance = 0;
		/*
		 * INSERT YOUR CODE HERE
		 */
		
		//return 0 if the given path is empty or null
		if (path == null || path.isEmpty())
			return 0;
		//return 0 if there is one or less station in the path
		if(path.size() <= 1) {
			return 0;
		}
		//running the loop through the path
		for(int i = 0; i < path.size() - 1; i++) {
			//initial variables to store data to use
			String name = path.get(i);
			Station adjStation = stationList.get(path.get(i+1));
			//adding the distance of between each station together
			distance += stationList.get(name).getAdjacentStations().get(adjStation);
		}
		return distance;
	}

	//this is the implementation for stage 3 that doesn't working well with timeout limited
	/*	
	// Get's actual distances for every station combo.
	public HashMap<String, HashMap<String, Double>> generateAllDistances () {
		HashMap<String, HashMap<String, Double>> allDistances = new HashMap<String, HashMap<String, Double>>();
		ArrayList<String> stationlist = new ArrayList<String>(stationList.keySet());
		// Add all stations to the outer HashMap.
		for(int i = 0; i < stationlist.size(); i++) {
			allDistances.put(stationlist.get(i), new HashMap<String, Double>());
		}
		// Get route min distance for every pair once.
		for(int i = 0; i < stationlist.size() - 1; i++) {
			for(int j = i + 1; j < stationlist.size() - 1; j++) {
				// Add the total distances
				double minDistance = (double) computeDistance(stationlist.get(i), stationlist.get(j));
				allDistances.get(stationlist.get(i)).put(stationlist.get(j), minDistance);
				allDistances.get(stationlist.get(j)).put(stationlist.get(i), minDistance);
			}
		}
		return allDistances;
	}
	
	// Get's minimum route distances for every station combo.
	public HashMap<String, HashMap<String, List<String>>> generateAllRouteMinDistances (){
		HashMap<String, HashMap<String, List<String>>> allRouteMinDistances = new HashMap<String, HashMap<String, List<String>>>();
		ArrayList<String> stationlist = new ArrayList<String>(stationList.keySet());
		// Add all stations to the outer HashMap.
		for(int i = 0; i < stationlist.size(); i++) {
			allRouteMinDistances.put(stationlist.get(i), new HashMap<String, List<String>>());
		}
		// Get route min distance for every pair once.
		// i = Outer layer of allRouteMinDistances station strings.
		// j = Inner layer of allRouteMinDistances station strings. i's counterpoint.
		for(int i = 0; i < stationlist.size() - 1; i++) {
			for(int j = i + 1; j < stationlist.size() - 1; j++) {
				// Check if i to j has already been stored somewhere
				List<String> subrouteij = new ArrayList<String>();
				// i2 = check all previous i's.
				for(int i2 = 0; i2 < i; i2++) {
					// j2 = check all stations that have been completed inside this iteration of i2.
					for(int j2 = 0; j2 < allRouteMinDistances.get(stationlist.get(i2)).size() - 1; j2++) {
						// If there exists a sub route that contains the current target i and j. Then just use that sub route.
						if(allRouteMinDistances.get(stationlist.get(i2)).containsKey(stationlist.get(j2))) {
						subrouteij = getSubList(stationlist.get(i), stationlist.get(j), allRouteMinDistances.get(stationlist.get(i2)).get(stationlist.get(j2)));
						// If there is a subroute.
						if(!subrouteij.isEmpty()) {
							allRouteMinDistances.get(stationlist.get(i)).putIfAbsent(stationlist.get(j), subrouteij);
							allRouteMinDistances.get(stationlist.get(j)).putIfAbsent(stationlist.get(i), subrouteij);
							break;
						}
					}
					}
				}
				allRouteMinDistances.get(stationlist.get(i)).putIfAbsent(stationlist.get(j), routeMinDistance(stationlist.get(i), stationlist.get(j)));
				allRouteMinDistances.get(stationlist.get(j)).putIfAbsent(stationlist.get(i), allRouteMinDistances.get(stationlist.get(i)).get(stationlist.get(j)));
			}
		}
		return allRouteMinDistances;
	}
	
	// Sublist of best route if contains one, otherwise empty arrayList.
	public List<String> getSubList(String origin, String destination, List<String> list) {
		if(list.contains(origin) && list.contains(destination)) {
			if(list.indexOf(origin) > list.indexOf(destination)) {
				// Add in reverse
				List<String> reverseList = new ArrayList<String>();
				for(int iter = list.indexOf(destination); iter > list.indexOf(origin); iter--) {
					reverseList.add(list.get(iter));
				}
				reverseList.add(origin);
				return reverseList;
			}
			List<String> newList = list.subList(list.indexOf(origin), list.indexOf(destination));
			return newList;
		}
		else {
			return new ArrayList<String>();
		}
	}
	*/
	
	/**
	 * Return the ratio between the length of the shortest route between two
	 * stations (in terms of distance) and the actual distance between the 
	 * two stations (computed using computeDistance())
	 * 
	 * In other words, 
	 * let d1 = distance of shortest route between the two stations as computed
	 *          by the method routeMinStop() (from Stage 1).
	 * let d2 = distance between two stations as computed by the method
	 *          computeDistance() 
	 *          
	 * The method returns d1/d2 (as a double)
	 * 
	 * @param origin		the starting station
	 * @param destination 	the ending station
	 * @return	s			the ratio d1/d2 as explained above
	 */
	public double computeRatio(String origin, String destination) {
		// The actual minimum distance between the origin and destination.
        double actualDistance = findTotalDistance(routeMinDistance(origin, destination));
        double newDistance = computeDistance(origin, destination);
        return actualDistance / newDistance;
	}
	
	
	/**
	 * Return the ratio as computed by computeRatio() method for all 
	 * pairs of station in the rail network.
	 * 
	 * The ratios should be stored in a HashMap<String,HashMap<String,Double>>,
	 * that is, the ratio between station a and b can be obtained by calling
	 * 
	 *    computeAllRatio().get(a).get(b)
	 * 
	 * @return a hashmap containing the ratios
	 */
	public HashMap<String,HashMap<String,Double>> computeAllRatio() {
		ArrayList<String> stationStrings = new ArrayList<String>(stationList.keySet());
		HashMap<String, HashMap<String,Double>> ratioMap = new HashMap<String, HashMap<String,Double>>();
		//initialize the ratioMap with key is the station names and value is empty hash map 
		for(int i = 0; i < stationStrings.size() - 1; i++) {
			ratioMap.put(stationStrings.get(i), new HashMap<String, Double>());
		}
		//assign value for ratioMap by using the supporting function computeRatio
		for(int i = 0; i < stationStrings.size() - 1; i++) {
			for(int j = i; j < stationStrings.size() - 1; j++) {
				double ratio = (double) computeRatio(stationStrings.get(i), stationStrings.get(j));
				ratioMap.get(stationStrings.get(i)).put(stationStrings.get(j), ratio);
				ratioMap.get(stationStrings.get(j)).put(stationStrings.get(i), ratio);
			}
		}
		return ratioMap;
	}
	
	/**
	 * The method finds the shortest route (in terms of number of stops)
	 * between the origin station and the destination station, taking 
	 * into account the available routes in the rail network.
	 * 
	 * The route is returned as an ArrayList<String> containing the lines taken,
	 * any transfer between lines, and the names of the stations on each line,
	 * including the origin and the destination stations.
	 * 
	 * Please see the assignment specification for more information.
	 * 
	 * If the route cannot be completed (there is no path between origin and
	 * destination), then return an empty ArrayList<String>
	 * 
	 * If the origin or the destination stations are not in the list of stations,
	 * return an empty ArrayList<String>. 
	 * 
	 * If the origin and the destination stations are the same, return an 
	 * ArrayList<String> containing the station.
	 * 
	 * @param origin		the starting station
	 * @param destination	the end station
	 * @return				the route taken
	 */
	public ArrayList<String>routeMinStopWithRoutes(String origin, String destination){
		//if origin and destination stations are the same, return an ArrayList containing the station
		if(origin.equals(destination)) {
			ArrayList<String> result = new ArrayList<String>();
			result.add(origin);
			return result;
		}
		//if the original or the destination stations are not in the list of stations, return an empty ArrayList
		if (!stationList.containsKey(origin) || !stationList.containsKey(destination)) {
			return new ArrayList<String>();
		}
		//initial variable result
		ArrayList<String> result = new ArrayList<String>();;
		//initial the queue holding temporary data for BFS
		LinkedList<String> queue = new LinkedList<String>();
		//create the variable that store the nearest adjacent station between origin and destination stations of each 
		//stations based on line information
		TreeMap<String, String> path = new TreeMap<String, String>();
		//set all stations to unmarked
		for (Map.Entry<String, Station> entry : stationList.entrySet()) {
			String key = entry.getKey();
			stationList.get(key).setUnmarked();
		}
		//initial value for the origin station
		stationList.get(origin).setMarked();
		queue.add(origin);
		path.put(origin,"None");

		//Running the standard BFS
		while (queue.size() != 0) {
			//selected the station from the queue
			String station = queue.poll();
			//getting all adjacent stations based on line information of selected station 
			TreeMap<Station, String> adjStation = stationList.get(station).getAdjacentLineStation();
			//running the loop through all adjacent stations
			for (Entry<Station, String> list : adjStation.entrySet()) {
				//store the name of station in variable
				Station name = list.getKey();
				//if the station has not visited yet
				if (!name.isMarked()) {
					//mark it as visited
					name.setMarked();
					//update the nearest adjacent station between origin and destination stations 
					path.put(name.getName(), station);
					//adding adjacent stations of selected station to the queue for the process
					queue.add(name.getName());
					//stop the loop when it reaches the destination
					if (name.getName().equals(destination))
						break;
				}

			}
		}
		
		// Traversing backward from the destination with the list of preceeding
		// station and store as an ArrayList of String
		String name = destination;
		while (path.get(name) != null) {
			result.add(0,name);
			name = path.get(name);
		}
		//return the empty list if the first and last item of the result is incorrect (the path cannot be completed)
		if(!origin.equals(result.get(0)) || !destination.equals(result.get(result.size()-1))) {
			return new ArrayList<String>();
		}
		//construct the list of line based on the path - list of station
		ArrayList<String> lineList = buildLine(result);
		//construct the final result with line information and list of station from origin to destination
		result = buildRoute(result, lineList);
		System.out.println(result);
		return result;
	}
	
	//constructing the list of line that are used based on the shortest path from origin to destination stations
	public ArrayList<String> buildLine(ArrayList<String> path){
		//initial variables
		ArrayList<String> trainLines = new ArrayList<String>();
		//running the loop to construct the list of line
		for(int i = 0; i < path.size()-1; i++){
			//initial variables
			Station station = stationList.get(path.get(i));
			Station adjStation = stationList.get(path.get(i+1));
			//adding data (lines) to the ArrayList
			trainLines.add(station.getAdjacentLineStation().get(adjStation));
		}
		return trainLines;
	}
	
	//constructing the final result including lien information and the shortest path
	public ArrayList<String> buildRoute(ArrayList<String> path, ArrayList<String> lineList){
		//initial variables
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<Integer> target = new ArrayList<Integer>();
		ArrayList<ArrayList<String>> myRoute = new ArrayList<ArrayList<String>>();
		ArrayList<String> myNote = new ArrayList<String>();
		//assign value for list target which will contain the location of first station, last station 
		//and the location of station that need to stop at and change lines
		target.add(0);
		for(int i = 0; i < lineList.size()-1; i++){
			if(!lineList.get(i).equals(lineList.get(i+1)))
				target.add(i+1);
		}
		target.add(path.size()-1);
		//constructing the lines information and store in myNote list
		for(int i = 0; i < target.size()-1; i++){
			int index =  target.get(i);
			String x = findDirection(path.get(index), path.get(index+1), lineList.get(index));
			myNote.add(x);
		}
		//separate the shortest path into smaller sub-list related to lines information
		for(int i = 0; i < target.size()-1; i++){
			ArrayList<String> temp = new ArrayList<String>(path.subList(target.get(i), target.get(i+1)+1));
			myRoute.add(temp);
		}
		//combine line information and sub-lists into final result
		for(int i = 0; i < myRoute.size(); i++){
			result.add(myNote.get(i));
			result.addAll(myRoute.get(i));
		}
		return result;
	}
	
	//finding the location/position or order of station in the list of station on the selected line
	public int findPosition(String station, String line){
		//initial variables
		String[] lines = trainLineList.get(line);
		//running loop and return the location of station in the list
		for(int i = 0; i < lines.length; i++){
			if(lines[i].equals(station))
				return i;
		}
		//if the station not in the list, return -1
		return -1;
	}
	
	//construct the line information
	public String findDirection(String start, String end, String path){
		//initial variables
		String lineInfo = "";
		//finding the position/location or order of station in the list of station on the selected line
		int first = findPosition(start,path);
		int second = findPosition(end,path);
		//create the variable holding the list of station on the selected line 
		String[] trainList = trainLineList.get(path);
		//if the line code is 'M' change it to Metro
		if(path.equals("M")) path = "Metro";
		//checking the station locations then create the lien information
		if(first < second) lineInfo = path + " towards " + trainList[trainList.length-1] + " from " + trainList[0];
		if(second < first) lineInfo = path + " towards " + trainList[0] + " from " + trainList[trainList.length-1];
		return lineInfo;
	}
}
