import java.util.Iterator;
import java.util.TreeSet;

public class Taxman {

	// member attributes
	private int upperLimit;
	private TreeSet<Integer> numbers;
	private int playerScore = 0;
	private int taxmanScore = 0;

	// given N, update `upperLimit` and
	// fill `numbers` with the necessary numbers.
	public Taxman(int n) {
		numbers = new TreeSet<Integer>();
		this.upperLimit = n; // Set the upper limit to n.
		for (int i = 1; i <= n; i++) {
			this.numbers.add(i); // Add all numbers previous to n.
		}
	}

	// upperLimit getter
	public int getUpperLimit() {
		return this.upperLimit;
	}

	// playerScore getter
	public int getPlayerScore() {
		return this.playerScore;
	}

	// taxmanScore getter
	public int getTaxmanScore() {
		return this.taxmanScore;
	}

	// return a deep copy of `numbers`
	public TreeSet<Integer> getAvailableNumbers() {
		TreeSet<Integer> deepCopy = new TreeSet<Integer>();
		// Deep copy the numbers
		deepCopy.addAll(this.numbers);
		return deepCopy;
	}

	// return true if there are divisors of `a` in `numbers` (not including `a`)
	// return false otherwise
	public boolean hasDivisorsOf(int a) {
		Iterator<Integer> iter = this.numbers.iterator();
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

	// return true if there are no longer any legal choices for player to make
	public boolean gameOver() {
		Iterator<Integer> iter = this.numbers.iterator();
		while (iter.hasNext()) {
			int temp = (int) iter.next();
			// There are divisors and therefore there is a legal choice.
			if (hasDivisorsOf(temp) == true) {
				return false;
			}
		}
		// Since the method didn't return false, add all remaining numbers in
		// numbers to the taxMan's score.
		if (numbers.size() > 0) {
			Iterator<Integer> iter2 = numbers.iterator();
			while (iter2.hasNext()) {
				int temp = (int) iter2.next();
				this.taxmanScore = this.taxmanScore + temp;
			}
		}
		// There are no divisors left, the game is over.
		return true;
	}

	// remove all the divisors of `a` in `numbers` (not including `a`)
	// return a TreeSet of all the numbers you removed
	private TreeSet<Integer> removeDivisorsOf(int a) {
		TreeSet<Integer> removedNumbers = new TreeSet<Integer>();
		Iterator<Integer> iter = this.numbers.iterator();
		// Count the iteration numbers incase we need to go back to a previous
		// item
		// after an element has been removed.
		int itercounter = 0;
		// Go through every item in numbers.
		while (iter.hasNext()) {
			int temp = (int) iter.next();
			// If a number is a divisor of a then AND not a itself then:
			if (a % temp == 0 && temp != a) {
				// Add the divisor to the removed numbers treeset.
				removedNumbers.add(temp);
				// remove the element from numbers.
				numbers.remove(temp);
				// When removing an item the iterator must go back an element.
				// Create a new iter for numbers starting from the start.
				Iterator<Integer> newIter = this.numbers.iterator();
				// Reset the iter.
				iter = newIter;
				int i = 0;
				// Move the iter to the correct positioning.
				while (i < itercounter) {
					iter.next();
					i++;
				}
			}
			// If nothing is removed simply go to the next element in numbers.
			itercounter++;
		}
		return removedNumbers;
	}

	// the player chooses the number `a` from the set.
	// the taxman then takes some numbers, as per the rules of the game.
	// return `true` if `a` was a valid choice.
	// return `false` if `a` was an invalid choice.
	// this method should also update scores where necessary.
	// if the game is over, the taxman should take all remaining numbers. 
	public boolean choose(int a) {
		// If it is a valid choice then:
		if (hasDivisorsOf(a) == true && numbers.contains(a) == true) {
			// Add the players choice to his score.
			playerScore = playerScore + a;
			TreeSet<Integer> taxMansNumbers = removeDivisorsOf(a);
			Iterator<Integer> tmnIterator = taxMansNumbers.iterator();
			// Add the taxman numbers to his score.
			while (tmnIterator.hasNext()) {
				taxmanScore = taxmanScore + tmnIterator.next();
			}
			// Remove 'a' from numbers.
			numbers.remove(a);
			// Return true as it was a valid choice.
			return true;
		} else
			return false;
	}

	// a helper function for displaying the current game state.
	public void displayGameState() {
		System.out.print("Remaining nums: ");
		for (int i = 0; i <= upperLimit; i++)
			if (this.numbers.contains(i))
				System.out.print(i + " ");
		System.out.println("\n" + "\nThe scores are now:");
		System.out.println("Player: " + playerScore + "\n" + "Taxman: " + taxmanScore + "\n");
		if (gameOver()) {
			// Player wins
			if (playerScore > taxmanScore) {
				System.out.println("Game Over! The winner is: Player!");
			}
			// Taxman wins
			if (taxmanScore > playerScore) {
				System.out.println("Game Over! The winner is: Taxman");
			}
			// Tie
			if (taxmanScore == playerScore) {
				System.out.println("Game Over! It's a tie!");
			}
			// Final Score Report
			System.out.print("Player: " + playerScore + "\n" + "Taxman: " + taxmanScore);
		}
	}
}
