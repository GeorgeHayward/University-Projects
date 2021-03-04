import java.util.Iterator;
import java.util.TreeSet;

public class GreedyPlayer implements Player {

	@Override
	public int chooseNum(TreeSet<Integer> avail) {
		// This greedy strategy finds the element which has the greatest
		// increase in score, compared to the score that the taxman will get
		// from the divisors total.
		Iterator<Integer> iter = avail.iterator();
		// This will play the role of the best possible choice. It will be
		// updated as calculations commence until it is finally used to return
		// the best choice.
		int bestChoice = 0;
		// Start of with a low value so that the scoreGap will always be changed
		// on the first instance of the algorithm(no matter how small the
		// difference, it will always be larger than -100)
		int scoreGap = -100;
		int taxmanPoints = 0;
		int playerPoints = 0;
		// Go through the list
		while (iter.hasNext()) {
			int temp = (int) iter.next();
			// Does the number even have divisors (is it an option)
			if (hasDivisorsOf(temp, avail) == true) {
				// Value of the element.
				playerPoints = temp;
				// Add up taxmanPoints
				taxmanPoints = getDivisorTotal(temp, avail);
				// If this scoregap is larger than the previous score gap, then
				// it is a better option.
				if ((playerPoints - taxmanPoints) > scoreGap) {
					// The new best element for this selection.
					bestChoice = temp;
					// Update the best scoregap
					scoreGap = playerPoints - taxmanPoints;
				}
			}
		}
		// Return the element which has the best possible immediate outcome for
		// the human player.
		return bestChoice;
	}

	// Check if 'a' has divisors in x.
	public boolean hasDivisorsOf(int a, TreeSet<Integer> x) {
		Iterator<Integer> iter = x.iterator();
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

	// Returns an integer value for all the divisors of 'a' in TreeSet 'x' added
	// together.
	private int getDivisorTotal(int a, TreeSet<Integer> x) {
		TreeSet<Integer> divisors = new TreeSet<Integer>();
		Iterator<Integer> iter = x.iterator();
		int total = 0;
		// Go through every item in numbers.
		while (iter.hasNext()) {
			int temp = (int) iter.next();
			// If a number is a divisor of a then AND not a itself then:
			if (a % temp == 0 && temp != a) {
				// Add the divisor to the divisor numbers treeset.
				divisors.add(temp);
			}
		}
		// Go through all the divisors and add it to the running total of the
		// divisors.
		Iterator<Integer> divIter = divisors.iterator();
		while (divIter.hasNext()) {
			int divTemp = (int) divIter.next();
			total = total + divTemp;
		}
		// Return all the divisors added up.
		return total;
	}
}
