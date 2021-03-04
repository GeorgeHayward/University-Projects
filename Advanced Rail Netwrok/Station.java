
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class Station implements Comparable<Station>{

	/* the station name */
	private String name;
	
	/* longitude and latitude coordinates of the station */
	private Double latitude;
	private Double longitude;
	
	/* used to indicate if the station has been visited*/
	private boolean marked;
	
	/* the list of neighbours of the current station and the distance 
	 * of the neighbouring stations
	 * 
	 * for example, if the TreeMap contains
	 * 	{Blacktown=2768, Toongabbie=1984} 
	 * then the station is adjacented connected to Blacktown (2768 metres away) 
	 * and Toongabbie (1984 metres away)
	 */
	private TreeMap<Station,Integer> adjacentStations;
	//adding new properties for station used in stage 4
	private TreeMap<Station,String> adjacentLineStation;
	private ArrayList<String> trainLine;

	/**
	 * Construct a new Station object
	 * 
	 * @param s			station name
	 * @param latitude  station latitude coordinate
	 * @param longitude station longitude coordinate
	 */
	public Station(String s, Double latitude, Double longitude) {
		this.name = s;
		this.latitude = latitude;
		this.longitude = longitude;
		this.marked = false;
		
		adjacentStations = new TreeMap<Station,Integer>();	
		//initial value for new stations' properties used in stage 4
		adjacentLineStation = new TreeMap<Station,String>();
		trainLine = new ArrayList<String>();
	}
	
	/**
	 * Return the list of stations which are adjacent to the current station
	 * and their distances. The method returns a new TreeMap. 
	 * 
	 * @return the list of adjacent stations
	 */
	public TreeMap<Station,Integer> getAdjacentStations(){
		return new TreeMap<Station,Integer>(adjacentStations);
	}
	//return the list of stations which are adjacent with selected station based on line information
	public TreeMap<Station,String> getAdjacentLineStation(){
		return new TreeMap<Station,String>(adjacentLineStation);
	}
	//return the line that selected station locates on
	public ArrayList<String> getTrainLine(){
		return new ArrayList<String>(trainLine);
	}
	
	/**
	 * Add Station s as a neighbour of this station
	 * 
	 * @param s			the station to be added as a neighbour
	 * @param distance  the distance to station s (in metres)
	 */
	public void addNeighbour(Station s, Integer distance) {
		if (s != null)
			adjacentStations.put(s, distance);
	}
	//add adjacent station to current station based on line information
	public void addLineNeighbour(Station s, String line) {
		if (s != null)
			adjacentLineStation.put(s, line);
	}
	//add line code/name to the selected station
	public void addLine(String line) {
			trainLine.add(line);
	}
	
	/* Getters */
	public String getName() {
		return this.name;
	}
	public Double getLatitude() {
		return this.latitude;
	}
	public Double getLongitude() {
		return this.longitude;
	}
	
	
	/* Methods to mark and unmark a station */
	public void setUnmarked() {
		this.marked = false;
	}
	
	public void setMarked() {
		this.marked = true;
	}
	
	public boolean isMarked() {
		return this.marked;
	}

	/* compareTo method since we are implementing Comparable */
	@Override
	public int compareTo(Station o) {
		if (o != null)
			return this.name.compareTo(o.getName());
		else
			return 1;
	}
	
	/*
	 * Prints out the station name, followed by its neighbours and distances
	 * to them
	 */
	public String toString() {
		String s = this.name + ", {";
		for (Station st : adjacentStations.keySet()) {
			s = s + st.getName() + "=" + adjacentStations.get(st) + ", ";
		}
		s = s.substring(0,s.length()-2);
		s = s + "}";
		return s;
	}
}