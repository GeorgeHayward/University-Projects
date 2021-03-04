import java.util.Scanner;

public class TaxmanGameOperator {
	// --- DO NOT MODIFY THIS FILE --- //

	/**
	 * TaxmanGameOperator receives a Player object, which will be used to make
	 * the decisions in the game. It runs the whole game from start to end,
	 * displaying progress as it goes.
	 **/
	public TaxmanGameOperator(Player player) {

		// get upper limit from system input
		Scanner in = new Scanner(System.in);
		System.out.print("Please choose an upper limit between 2 and 25. ");
		int upper = in.nextInt();
		if (upper >= 2 && upper <= 25) {
			// create the taxman object
			Taxman txm = new Taxman(upper);
			// keep getting and applying choices until the game is over
			while (!txm.gameOver()) {
				int chosen = player.chooseNum(txm.getAvailableNumbers());
				System.out.println("\nPlayer chose " + chosen);
				if (!txm.choose(chosen))
					System.out.println("Invalid choice!\n");
				txm.displayGameState();
			}
		} else
			//Wrong input, try again.
			System.out.print("The upper limit must be between 2 and 25. Please start again.");
	}
}
