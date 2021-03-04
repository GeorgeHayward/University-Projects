
public class Main {
	public static void main(String[] args) {
		
		// Change the below line to use a NaivePlayer or a GreedyPlayer or an OptimalPlayer
		// This is how you can test your different Player classes
		Player player = new NaivePlayer();
		
		// This kicks off the game. You don't need to change this line.
		new TaxmanGameOperator(player);
	}
}
