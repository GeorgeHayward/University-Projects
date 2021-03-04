import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class OptimalPlayer implements Player {

	// This choice tree will hold the series of choices which gives the best
	// possible player score, in comparison to the TaxMans score.
	public static ChoiceTree bestChoiceTree = new ChoiceTree();
	// This array list will hold the values of the best choice list. It will
	// then be used to feed integers into the chooseNum method.
	public static ArrayList<Integer> bestChoiceList = new ArrayList<Integer>();
	// This boolean switch is used to notify chooseNum if the getBestTree
	// algorithm has been run yet or not (False = hasn't been run. True = has
	// been run)
	public boolean TreeFound = false;

	@Override
	public int chooseNum(TreeSet<Integer> avail) {

		// If the best choice tree finding algorithm hasn't been run. Run it.
		if (TreeFound == false) {

			// Send through blank tree and blank choice list, as no choices have
			// been made yet.
			ChoiceTree blankTree = new ChoiceTree();
			ArrayList<Integer> blankList = new ArrayList<Integer>();
			getBestTree(avail, blankTree, blankList);
		}
		return getNextChoice(bestChoiceList);
	}

	// This recursive method inputs 3 variables:
	// - A TreeSet of Integers. This holds the selection of numbers available
	// for this turns choice.
	// - A ChoiceTree object. This holds the current scores so far in this
	// series of turns.
	// - An array of previous choices. This is what the program uses to
	// calculate the best possible set of choices for the player.
	// This method goes through every single possible choice path from the
	// original input of numbers and finds the one that ends with the largest
	// difference in players score and taxman score.
	public void getBestTree(TreeSet<Integer> inputNumbers, ChoiceTree inputTree, ArrayList<Integer> choiceList) {

		// Base Case (If there are no more legal choices left with the inputed
		// numbers)
		if (gameOver(inputNumbers, inputTree) == true) {

			// If this series of choices ended up having a larger score
			// difference than the previous largest. Then make this series the
			// new best.
			if (inputTree.getScoreDifference() >= bestChoiceTree.getScoreDifference()) {
				bestChoiceTree = inputTree;
				bestChoiceList = choiceList;
			}
		}

		// If the game is not over (meaning there are still more paths to go
		// down)
		else {

			// Create a TreeSet of all the possible legal choices in
			// inputNumbers.
			TreeSet<Integer> legalChoices = new TreeSet<Integer>();

			// Go through all the input numbers.
			Iterator<Integer> inputNumbersIterator = inputNumbers.iterator();
			while (inputNumbersIterator.hasNext()) {
				int selectedChoice = (int) inputNumbersIterator.next();

				// If it's a legal choice:
				if (hasDivisorsOf(inputNumbers, selectedChoice) == true) {

					// Add it to the legal choice TreeSet.
					legalChoices.add(selectedChoice);
				}
			}

			// Go through every legal choice and simulate what happens if you
			// select each on as the choice.
			Iterator<Integer> legalChoicesIterator = legalChoices.iterator();
			while (legalChoicesIterator.hasNext()) {

				// The current selected choice from legal choices.
				int currentChoice = (int) legalChoicesIterator.next();

				// Make a deep copy of the input numbers so that they can be
				// altered without effecting the rest of the legal choice
				// iterations.
				TreeSet<Integer> currentChoiceNumbers = new TreeSet<Integer>();
				currentChoiceNumbers = copyNumbers(inputNumbers);

				// Make a deep copy of the inputed tree. This is so the tree
				// can break off into different states without altering the
				// original inputed tree.
				ChoiceTree treeCopy = new ChoiceTree();
				treeCopy = copyTree(inputTree);

				// Make a deep copy of the inputed choice list. This is so it
				// can remain its own encapsulated list as it continues to go
				// down a path.
				ArrayList<Integer> copyList = new ArrayList<Integer>();
				copyList.addAll(choiceList);

				// Update the information in this new tree based on the
				// selection of this iteration.
				TreeSet<Integer> taxMansNumber = removeDivisorsOf(currentChoiceNumbers, currentChoice);
				treeCopy.addChoice(currentChoice, taxMansNumber);
				copyList.add(currentChoice);
				currentChoiceNumbers.remove(currentChoice);

				// Make a recursive call now with the new set of numbers and the
				// tree.
				getBestTree(currentChoiceNumbers, treeCopy, copyList);
			}
		}

		// After every single path option has been tested. Change the boolean
		// switch (TreeFound) to true as this algorithm has been run.
		TreeFound = true;
	}

	// Return true if there are no longer any legal choices for player to make
	// with the inputed series of numbers. Additionally input the choice tree
	// so that if the game is over, the remaining numbers in the list can be
	// added to the TaxMans score.
	public boolean gameOver(TreeSet<Integer> numbers, ChoiceTree tree) {
		Iterator<Integer> iter = numbers.iterator();
		// Go through every number.
		while (iter.hasNext()) {
			int selectedNumber = (int) iter.next();
			// There are divisors and therefore there is a legal choice left.
			if (hasDivisorsOf(numbers, selectedNumber) == true) {
				return false;
			}
		}
		// Since the game has in fact ended. The remaining numbers are added to
		// the taxmans score (If there are any numbers left).
		if (numbers.size() > 0) {
			tree.updateTaxmanTotal(numbers);
		}
		// There are no divisors(therefore choices) left, the game is over.
		return true;
	}

	// return true if there are divisors of `a` in `numbers` (not including `a`)
	// return false otherwise
	public boolean hasDivisorsOf(TreeSet<Integer> numbers, int a) {
		Iterator<Integer> iter = numbers.iterator();
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

	// return a deep copy of `numbers`
	public TreeSet<Integer> copyNumbers(TreeSet<Integer> numbers) {
		TreeSet<Integer> deepCopy = new TreeSet<Integer>();
		// Deep copy the numbers
		deepCopy.addAll(numbers);
		return deepCopy;
	}

	// return a deep copy of 'choiceTree'
	public ChoiceTree copyTree(ChoiceTree x) {
		ChoiceTree copy = new ChoiceTree();
		// Deep copy values
		copy.setPlayerTotal(x.getPTotal());
		copy.setTaxManTotal(x.getTMTotal());
		return copy;
	}

	// remove all the divisors of `a` in `numbers` (not including `a`)
	// return a TreeSet of all the numbers you removed
	private TreeSet<Integer> removeDivisorsOf(TreeSet<Integer> numbers, int a) {
		TreeSet<Integer> removedNumbers = new TreeSet<Integer>();
		Iterator<Integer> iter = numbers.iterator();
		// Count the iteration numbers incase we need to go back to a previous
		// item after an element has been removed.
		int itercounter = 0;
		// Go through every item in numbers.
		while (iter.hasNext()) {
			int temp = (int) iter.next();
			// If a number is a divisor of a then AND not 'a' itself then:
			if (a % temp == 0 && temp != a) {
				// Add the divisor to the removed numbers treeset.
				removedNumbers.add(temp);
				// remove the element from numbers.
				numbers.remove(temp);
				// When removing an item the iterator must go back an element.
				// Create a new iter for numbers starting from the start.
				Iterator<Integer> newIter = numbers.iterator();
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

	// Return the next choice in this choice tree. Then remove it from the list
	// so that the next element can be accessed.
	public int getNextChoice(ArrayList<Integer> x) {
		int temp = x.get(0);
		x.remove(0);
		return temp;
	}
}
