import java.util.TreeSet;

public interface Player {
	// --- DO NOT MODIFY THIS FILE --- //
	
	/** This is an interface that says that all Player classes must
	    have a function as described below. The player class is given
	    a TreeSet of all available numbers, and must return the value
	    of the number it will choose.
	**/
	public int chooseNum(TreeSet<Integer> avail);
}
