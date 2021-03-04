import java.util.Iterator;
import java.util.TreeSet;

//The point of this class is to create objects which hold specific game scores related to a choice list (outside this class object).
public class ChoiceTree {
	public int taxManTotal;
	public int playerTotal;

	// Constructor
	public ChoiceTree() {
		this.taxManTotal = 0;
		this.playerTotal = 0;
	}

	// Update the totals based on the input.
	public void addChoice(int selected, TreeSet<Integer> divisors) {
		// Update the totals.
		updatePlayerTotal(selected);
		updateTaxmanTotal(divisors);
	}

	// Updates the players total score
	public void updatePlayerTotal(int x) {
		this.playerTotal = this.playerTotal + x;
	}

	// Updates the TaxMans total score
	public void updateTaxmanTotal(TreeSet<Integer> x) {
		Iterator<Integer> numbersIterator = x.iterator();
		while (numbersIterator.hasNext()) {
			this.taxManTotal = this.taxManTotal + numbersIterator.next();
		}
	}

	// Getter for TaxManTotal
	public int getTMTotal() {
		return this.taxManTotal;
	}

	// Setter for TaxManTotal
	public void setTaxManTotal(int x) {
		this.taxManTotal = x;
	}

	// Getter for playerTotal
	public int getPTotal() {
		return this.playerTotal;
	}

	// Setter for playerTotal
	public void setPlayerTotal(int x) {
		this.playerTotal = x;
	}

	// Calculates the difference between playerTotal and TaxManTotal.
	public int getScoreDifference() {
		return this.playerTotal - this.taxManTotal;
	}

}
