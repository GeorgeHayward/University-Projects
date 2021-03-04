import java.util.Iterator;
import java.util.TreeSet;

public class NaivePlayer implements Player {

	@Override
	// This method will find the highest legal choice that the player can take
	// each turn.
	public int chooseNum(TreeSet<Integer> avail) {
		// Implement a Naive strategy for choosing which number the
		// player will take. return which number you choose for this turn.
		int bestChoice = 0;
		Iterator<Integer> iter = avail.iterator();
		while (iter.hasNext()) {
			int temp = (int) iter.next();
			if (hasDivisorsOf(temp, avail) == true && temp > bestChoice) {
				bestChoice = temp;
			}
		}
		return bestChoice;
	}

	// Check if 'a' has divisors in x.
	public boolean hasDivisorsOf(int a, TreeSet<Integer> inputSet) {
		// Check every number in input set to see if it is a divisor of a.
		Iterator<Integer> iter = inputSet.iterator();
		while (iter.hasNext()) {
			int temp = (int) iter.next();
			// Return true if there is a divisor of a.
			if (a % temp == 0 && temp != a) {
				return true;
			}
		}
		// There are no divisors.
		return false;
	}
}
