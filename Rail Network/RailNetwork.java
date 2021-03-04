package assg1p1;

import java.io.*;
import java.util.*;

public class RailNetwork {

	// private final double THRESHOLD = 0.000001;

	private TreeMap<String, Station> stationList;

	public RailNetwork(String trainData, String connectionData) {
		stationList = new TreeMap<>();

		try {
			readStationData(trainData);
			readConnectionData(connectionData);
		} catch (IOException e) {
			System.out.println("Exception encountered: " + e);
		}
	}

	/**
	 * Reads the CSV file containing information about the stations and populate the
	 * TreeMap<String,Station> stationList. Each row of the CSV file contains the
	 * name, latitude and longitude coordinates of the station.
	 * 
	 * You need to make a Station object for each row and add it to the
	 * TreeMap<String,Station> stationList where the key value is the name of the
	 * station (as a String).
	 * 
	 * @param infile the path to the file
	 * @throws IOException if the file is not found
	 */
	public void readStationData(String infile) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(infile));
		// Skip first row (STATION_NAME, LATITUDE, LONGITUDE etc...)
		String currentLine = in.readLine();
		// Loop through every row
		while ((currentLine = in.readLine()) != null) {
			// Split columns into an array.
			String[] items = currentLine.split(",");
			// Create new station object and place it into the stationList
			stationList.put(items[0], new Station(items[0], new Double(items[1]), new Double(items[2])));
		}
		in.close();
	}

	/**
	 * Reads the CSV file containing information about connectivity between adjacent
	 * stations, and update the stations in stationList so that each Station object
	 * has a list of adjacent stations.
	 * 
	 * Each row contains two Strings separated by comma. To obtain the distance
	 * between the two stations, you need to use the latitude and longitude
	 * coordinates together with the computeDistance() methods defined below
	 * 
	 * @param infile the path to the file
	 * @throws IOException if the file is not found
	 */
	public void readConnectionData(String infile) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(infile));
		// This will be the string representation of each row.
		String currentLine;
		// Loop through every row
		while ((currentLine = in.readLine()) != null) {
			// Split columns into an array.
			String[] items = currentLine.split(",");
			int distance = computeDistance(items[0], items[1]);
			stationList.get(items[0]).addNeighbour(stationList.get(items[1]), distance);
			stationList.get(items[1]).addNeighbour(stationList.get(items[0]), distance);
		}
		in.close();
	}

	/**
	 * Given the latitude and longitude coordinates of two locations x and y, return
	 * the distance between x and y in metres using Haversine formula, rounded down
	 * to the nearest integer.
	 * 
	 * Note that two more methods are provided below for your convenience and you
	 * should not directly call this method
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
		double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
		double rad = 6371.0;
		Double c = 2 * Math.asin(Math.sqrt(a));
		Double distance = rad * c * 1000;
		return distance.intValue();
	}

	/**
	 * Compute the distance between two stations in metres, where the stations are
	 * given as String objects
	 * 
	 * @param a the first station
	 * @param b the second station
	 * @return the distance between the two stations in metres
	 */
	public int computeDistance(String a, String b) {
		Station u = stationList.get(a);
		Station v = stationList.get(b);
		return computeDistance(u.getLatitude(), u.getLongitude(), v.getLatitude(), v.getLongitude());
	}

	/**
	 * Compute the distance between two stations in metres, where the stations are
	 * given as Station objects
	 * 
	 * @param a the first station
	 * @param b the second station
	 * @return the distance between the two stations in metres
	 */
	public int computeDistance(Station a, Station b) {
		return computeDistance(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude());
	}

	/**
	 * The method finds the shortest route (in terms of distance travelled) between
	 * the origin station and the destination station. The route is returned as an
	 * ArrayList<String> containing the names of the stations along the route,
	 * including the origin and the destination stations.
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
	 * @param origin      the starting station
	 * @param destination the destination station
	 * @return
	 */
	public ArrayList<String> routeMinDistance(String origin, String destination) {
		if (!stationList.containsKey(origin) || !stationList.containsKey(destination)) {
			return new ArrayList<String>();
		}
		if (origin.equals(destination)) {
			ArrayList<String> ans = new ArrayList<String>();
			ans.add(origin);
			return ans;
		}
		// Stack which will contain stations that still need to be visited.
		Stack<String> stationStack = new Stack<String>();
		// Push origin station to start traversal, create it's best route and mark it as
		// visited.
		stationStack.push(origin);
		stationList.get(origin).addStationToRoute(origin);
		// While there are still stations to visit:
		while (!stationStack.empty()) {
			// Inspect the station at the top of the stack
			String currentStation = stationStack.pop();
			// Get this stations neighbors
			TreeMap<Station, Integer> adjacentStations = stationList.get(currentStation).getAdjacentStations();
			// Iterate over each adjacent station
			for (Map.Entry<Station, Integer> entry : adjacentStations.entrySet()) {
				String adjStation = entry.getKey().getName();
				// Get the path, if we choose to go to this adjacent station.
				ArrayList<String> adjacentStationPath = new ArrayList<String>();
				adjacentStationPath.addAll(stationList.get(currentStation).getBestRoute());
				adjacentStationPath.add(adjStation);
				// Is adjStation the destination?
				if (adjStation.equals(destination)) {
					// Check if this is the current shortest route to the destination.
					if ((stationList.get(destination).getBestRoute().isEmpty()) || (findTotalDistance(
							stationList.get(destination).getBestRoute()) > findTotalDistance(adjacentStationPath))) {
						// This path is better than the destinations current path
						stationList.get(destination).setBestRoute(adjacentStationPath);
					}
				}
				// adjStation is not the destination, keep looking.
				else {
					// Only keep looking if destination hasn't been reached yet OR
					if ((stationList.get(destination).getBestRoute().isEmpty()) ||
					// This adjacent path is less than the current bestRoute for the destination.
							(findTotalDistance(adjacentStationPath) < findTotalDistance(
									stationList.get(destination).getBestRoute()))) {
						// Only search this adjacent station if we havn't searched it yet OR
						if (stationList.get(adjStation).getBestRoute().isEmpty() ||
						// If this path is smaller than the current path.
								(findTotalDistance(stationList.get(adjStation).getBestRoute()) > findTotalDistance(
										adjacentStationPath))) {
							stationList.get(adjStation).setBestRoute(adjacentStationPath);
							stationStack.push(adjStation);
						}
					}
				}
			}
		}
		return stationList.get(destination).getBestRoute();
	}

	/**
	 * The method finds the shortest route (in terms of distance travelled) between
	 * the origin station and the destination station under the condition that the
	 * route must not pass through any stations in TreeSet<String> failures
	 * 
	 * The route is returned as an ArrayList<String> containing the names of the
	 * stations along the route, including the origin and the destination stations.
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
	 * @param origin      the starting station
	 * @param destination the destination station
	 * @param failures    the list of stations that cannot be used
	 * @return
	 */
	public ArrayList<String> routeMinDistance(String origin, String destination, TreeSet<String> failures) {
		if (!stationList.containsKey(origin) || !stationList.containsKey(destination) || failures.contains(origin)
				|| failures.contains(destination)) {
			return new ArrayList<String>();
		}
		if (origin.equals(destination)) {
			ArrayList<String> ans = new ArrayList<String>();
			ans.add(origin);
			return ans;
		}
		// Stack which will contain stations that still need to be visited.
		Stack<String> stationStack = new Stack<String>();
		// Push origin station to start traversal, create it's best route and mark it as
		// visited.
		stationStack.push(origin);
		stationList.get(origin).addStationToRoute(origin);
		// While there are still stations to visit:
		while (!stationStack.empty()) {
			// Inspect the station at the top of the stack
			String currentStation = stationStack.pop();
			// Get this stations neighbors
			TreeMap<Station, Integer> adjacentStations = stationList.get(currentStation).getAdjacentStations();
			// Iterate over each adjacent station
			for (Map.Entry<Station, Integer> entry : adjacentStations.entrySet()) {
				String adjStation = entry.getKey().getName();
				// IF THE ADJSTATION ISN"T IN THE FAILURES TREESET, PROCEED. This is the only
				// change from the original routeMinDistance
				if (!failures.contains(adjStation)) {
					// Get the path, if we choose to go to this adjacent station.
					ArrayList<String> adjacentStationPath = new ArrayList<String>();
					adjacentStationPath.addAll(stationList.get(currentStation).getBestRoute());
					adjacentStationPath.add(adjStation);
					// Is adjStation the destination?
					if (adjStation.equals(destination)) {
						// Check if this is the current shortest route to the destination.
						if ((stationList.get(destination).getBestRoute().isEmpty())
								|| (findTotalDistance(stationList.get(destination).getBestRoute()) > findTotalDistance(
										adjacentStationPath))) {
							// This path is better than the destinations current path
							stationList.get(destination).setBestRoute(adjacentStationPath);
						}
					}
					// adjStation is not the destination, keep looking.
					else {
						// Only keep looking if destination hasn't been reached yet OR
						if ((stationList.get(destination).getBestRoute().isEmpty()) ||
						// This adjacent path is less than the current bestRoute for the destination.
								(findTotalDistance(adjacentStationPath) < findTotalDistance(
										stationList.get(destination).getBestRoute()))) {
							// Only search this adjacent station if we havn't searched it yet OR
							if (stationList.get(adjStation).getBestRoute().isEmpty() ||
							// If this path is smaller than the current path.
									(findTotalDistance(stationList.get(adjStation).getBestRoute()) > findTotalDistance(
											adjacentStationPath))) {
								stationList.get(adjStation).setBestRoute(adjacentStationPath);
								stationStack.push(adjStation);
							}
						}
					}
				}
			}
		}
		return stationList.get(destination).getBestRoute();
	}

	/**
	 * The method finds the shortest route (in terms of number of stops) between the
	 * origin station and the destination station. The route is returned as an
	 * ArrayList<String> containing the names of the stations along the route,
	 * including the origin and the destination stations.
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
	 * @param origin      the starting station
	 * @param destination the destination station
	 * @return
	 */
	public ArrayList<String> routeMinStop(String origin, String destination) {
		if (!stationList.containsKey(origin) || !stationList.containsKey(destination)) {
			return new ArrayList<String>();
		}
		if (origin.equals(destination)) {
			ArrayList<String> ans = new ArrayList<String>();
			ans.add(origin);
			return ans;
		}
		// Queue which contains all stations left to inspect.
		Queue<String> stationQueue = new LinkedList<String>();
		// Add origin station to start traversal, create it's best route and mark it as
		// visited.
		stationQueue.add(origin);
		stationList.get(origin).addStationToRoute(origin);
		// While there are still stations to visit:
		while (!stationQueue.isEmpty()) {
			// Inspect the station at the top of the stack
			String currentStation = stationQueue.poll();
			// Get this stations neighbors
			TreeMap<Station, Integer> adjacentStations = stationList.get(currentStation).getAdjacentStations();
			// Iterate over each adjacent station
			for (Map.Entry<Station, Integer> entry : adjacentStations.entrySet()) {
				String adjStation = entry.getKey().getName();
				// Get the path, if we choose to go to this adjacent station.
				ArrayList<String> adjacentStationPath = new ArrayList<String>();
				adjacentStationPath.addAll(stationList.get(currentStation).getBestRoute());
				adjacentStationPath.add(adjStation);
				// Is adjStation the destination?
				if (adjStation.equals(destination)) {
					// Check if this is the current shortest route to the destination.
					if ((stationList.get(destination).getBestRoute().isEmpty())
							|| (stationList.get(destination).getBestRoute().size() > adjacentStationPath.size())) {
						// This path is better than the destinations current path
						stationList.get(destination).setBestRoute(adjacentStationPath);
					}
				}
				// adjStation is not the destination, keep looking.
				else {
					// Only keep looking if destination hasn't been reached yet OR
					if ((stationList.get(destination).getBestRoute().isEmpty()) ||
					// This adjacent path is less than the current bestRoute for the destination.
							(adjacentStationPath.size() < stationList.get(destination).getBestRoute().size())) {
						// Only search this adjacent station if we havn't searched it yet OR
						if (stationList.get(adjStation).getBestRoute().isEmpty() ||
						// If this path is smaller than the current path.
								(stationList.get(adjStation).getBestRoute().size() > adjacentStationPath.size())) {
							stationList.get(adjStation).setBestRoute(adjacentStationPath);
							stationQueue.add(adjStation);
						}
					}
				}
			}
		}
		return stationList.get(destination).getBestRoute();
	}

	/**
	 * The method finds the shortest route (in terms of number of stops) between the
	 * origin station and the destination station under the condition that the route
	 * must not pass through any stations in TreeSet<String> failures (i.e. the rail
	 * segment cannot be travelled on)
	 * 
	 * The route is returned as an ArrayList<String> containing the names of the
	 * stations along the route, including the origin and the destination stations.
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
	 * @param origin      the starting station
	 * @param destination the destination station
	 * @param failures    the list of stations that cannot be used
	 * @return
	 */
	public ArrayList<String> routeMinStop(String origin, String destination, TreeSet<String> failures) {
		if (!stationList.containsKey(origin) || !stationList.containsKey(destination)) {
			return new ArrayList<String>();
		}
		if (origin.equals(destination)) {
			ArrayList<String> ans = new ArrayList<String>();
			ans.add(origin);
			return ans;
		}
		// Queue which contains all stations left to inspect.
		Queue<String> stationQueue = new LinkedList<String>();
		// Add origin station to start traversal, create it's best route and mark it as
		// visited.
		stationQueue.add(origin);
		stationList.get(origin).addStationToRoute(origin);
		// While there are still stations to visit:
		while (!stationQueue.isEmpty()) {
			// Inspect the station at the top of the stack
			String currentStation = stationQueue.poll();
			// Get this stations neighbors
			TreeMap<Station, Integer> adjacentStations = stationList.get(currentStation).getAdjacentStations();
			// Iterate over each adjacent station
			for (Map.Entry<Station, Integer> entry : adjacentStations.entrySet()) {
				String adjStation = entry.getKey().getName();
				// IF THE ADJSTATION ISN"T WITHIN FAILURES, PROCEED. This is the only difference
				// from the original routeMinStop
				if (!failures.contains(adjStation)) {
					// Get the path, if we choose to go to this adjacent station.
					ArrayList<String> adjacentStationPath = new ArrayList<String>();
					adjacentStationPath.addAll(stationList.get(currentStation).getBestRoute());
					adjacentStationPath.add(adjStation);
					// Is adjStation the destination?
					if (adjStation.equals(destination)) {
						// Check if this is the current shortest route to the destination.
						if ((stationList.get(destination).getBestRoute().isEmpty())
								|| (stationList.get(destination).getBestRoute().size() > adjacentStationPath.size())) {
							// This path is better than the destinations current path
							stationList.get(destination).setBestRoute(adjacentStationPath);
						}
					}
					// adjStation is not the destination, keep looking.
					else {
						// Only keep looking if destination hasn't been reached yet OR
						if ((stationList.get(destination).getBestRoute().isEmpty()) ||
						// This adjacent path is less than the current bestRoute for the destination.
								(adjacentStationPath.size() < stationList.get(destination).getBestRoute().size())) {
							// Only search this adjacent station if we havn't searched it yet OR
							if (stationList.get(adjStation).getBestRoute().isEmpty() ||
							// If this path is smaller than the current path.
									(stationList.get(adjStation).getBestRoute().size() > adjacentStationPath.size())) {
								stationList.get(adjStation).setBestRoute(adjacentStationPath);
								stationQueue.add(adjStation);
							}
						}
					}
				}
			}
		}
		return stationList.get(destination).getBestRoute();
	}

	/**
	 * Given a route between two stations, compute the total distance of this route.
	 * 
	 * @param path the list of stations in the route (as String objects)
	 * @return the length of the route between the first station and last station in
	 *         the list
	 */
	public int findTotalDistance(ArrayList<String> path) {
		// No path to find distance
		if (path.size() < 2) {
			return 0;
		}
		int distance = 0;
		// Add up the distances between each neighboring node in the path
		for (int i = 0; i < path.size() - 1; i++) {
			distance = distance + computeDistance(path.get(i), path.get(i + 1));
		}
		return distance;
	}

	/**
	 * Given a route between two stations, compute the minimum total cost of
	 * performing an exhaustive scan on this route, as described in the assignment
	 * specification for Stage 2.
	 * 
	 * Return 0 if there are 2 or less stations in the route.
	 * 
	 * @param route the list of stations in the route (as String objects)
	 * @return the minimum cost of performing exhaustive scans
	 */
	public int optimalScanCost(ArrayList<String> route) {
		if (route == null || route.size() <= 2) {
			return 0;
		}
		int bestCombination = Integer.MAX_VALUE;
		// Loop through every station in the route, starting at the second station as you cannot cut at the first station.
		// Ending at the 2nd last station as you cannot cut at the last station.
		for (int i = 1; i < route.size() - 1; i++ ) {
			// Create two routes cutting at point i.
			ArrayList<String> headRoute = new ArrayList<String>();
			ArrayList<String> tailRoute = new ArrayList<String>();
			headRoute.addAll(route.subList(0, i + 1));
			tailRoute.addAll(route.subList(i, route.size()));
			// Get the total distances of these routes.
			int headDistance = findTotalDistance(headRoute);
			int tailDistance = findTotalDistance(tailRoute);
			// Get the best combination of cuts within these routes + the initial distance.
			bestCombination = Math.min(bestCombination, headDistance + tailDistance + optimalScanCost(headRoute) + optimalScanCost(tailRoute));
		}
		return bestCombination;
	}

	/***
	 * Given a route between two stations, return the list of stations (in the order
	 * that they were chosen) that gives the segmentation that leads to the minimum
	 * cost for performing an exhaustive scan on the the route (as described in the
	 * assignment specification for Stage 2.
	 * 
	 * Return an empty ArrayList if there are 2 or less stations in the route.
	 * 
	 * @param route
	 * @return
	 */
	public ArrayList<String> optimalScanSolution(ArrayList<String> route) {
		if (route == null || route.size() <= 2)
			return new ArrayList<String>();
		return routeMinDistance(route.get(0), route.get(route.size() - 1));

	}
}
