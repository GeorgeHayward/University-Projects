package simplenim;

import java.io.BufferedReader;
import java.util.*;

import simplenim.SimpleNim.GameState.Move;

public class SimpleNim {
	static boolean DEBUG = true; //Debug boolean variable.
	static EasyPuzzleGUI display; //Creates the GUI class.
	static Vector<SimpleNim.GameState.Move> moves = new Vector<Move>(); //Creates a Vector of all the moves made.
	static int madeMovesPrefixLength = 0; //Number of moves made in the game state.
	static BufferedReader in; //Character input stream

	public static void main(String[] paramArrayOfString)
	{
		display = new EasyPuzzleGUI(); //Instantiate the display.
		GameState.reset(); //Reset the game state.
	}

	//Returns what the player has typed.
	static PlayerTyped getPlayersPileSelection(int paramInt)
			throws NimError
	{
		if ((0 <= paramInt) && (paramInt <= 3)) return new PlayerTyped(SimpleNim.PlayerTyped.Command.TAKE); //If the input is between 0 and 3, take.
		if (paramInt == 10) return new PlayerTyped(SimpleNim.PlayerTyped.Command.UNDO); //Undo a move + 10 is the int for undo.
		if (paramInt == 11) return new PlayerTyped(SimpleNim.PlayerTyped.Command.REDO); //Redo a move + 11 is the int for redo.
		if (paramInt == 12) return new PlayerTyped(SimpleNim.PlayerTyped.Command.BEGIN); //State again + 12 is the int for Begin game.
		if (paramInt == 15) return new PlayerTyped(SimpleNim.PlayerTyped.Command.HELP); //Return help information + 15 is the int for help.
		throw new NimError(2002, "");
	}

	//Error description class - will print out the string corresponding with the error had.
	static String errorDescription(int paramInt, String paramString)
	{
		String str;
		if (paramInt >= 0)
			switch (paramInt) { case 1103:
				str = paramString; break;
			case 1132:
			case 2000:
				str = "End of file"; break;
			case 1136:
			case 2002:
				str = "Illegal input character '" + paramString + "'"; break;
			case 1509:
				str = "Not yet implemented"; break;
			case 1538:
				str = "Nothing to undo"; break;
			case 1539:
				str = "Nothing to redo"; break;
			case 1133:
			case 2001:
				str = "Empty input line"; break;
			case 1134:
			case 1135:
				str = "Extra characters on input line"; break;
			case 954:
				str = "Too few matches taken"; break;
			case 955:
				str = "Too many matches taken"; break;
			case 956:
				str = "Not enough matches left for that"; break;
			case 1020:
			case 1121:
				str = "No such pile"; break;
			default:
				str = "Undocumented error"; break; }
		else
			str = "Internal consistency error";
		if (DEBUG) str = str + " (" + paramInt + ")";
		return str + ".";
	}

	static class PlayerTyped
	{
		Command command; //Command variable holder.

		PlayerTyped(Command paramCommand)
		{
			this.command = paramCommand; //Makes the command what the player typed.
		}
		static enum Command { UNDO, REDO, TAKE, BEGIN, DEBUG, HELP; } //List of commands

	}

	//Describes everything that is in the current game state.
	static class GameState
	{
		static Player STARTING_PLAYER = Player.White; //Makes the white player the starting player.
		static int STARTING_CONFIGURATION = 5; //The starting size for the pile of pebbles or matches. (buttons)
		static int matchesOnLeftPile; //An int variable that hold the amount of matches currently on the left pile.
		static int matchesOnMiddlePile; //An int variable that hold the amount of matches currently on the left pile.
		static int matchesOnRightPile; //An int variable that hold the amount of matches currently on the left pile.
		static int totalMatches = 15; //Keep track of the total number of matches.
		static Player currentPlayer; //A player object which defines the current player.

		static void swapPlayers() //A function which changes the current player to the opposite player.
		{
			currentPlayer = currentPlayer == Player.White ? Player.Black : Player.White;
		}

		static String playerChar(Player paramPlayer) //Returns a string either 'W' or 'B' depending on black or white.
		{
			return paramPlayer == Player.White ? "W" : "B";
		}
		
		static boolean gameIsOver() {
			if (matchesOnLeftPile > 0 || matchesOnMiddlePile > 0 || matchesOnRightPile > 0) return false; //If there is more than 0 matches on the pile, the game is not over.
			return true; //If the number of matches on the pile is <=0 then the game is over.
		}
		
		static void reset() {
			matchesOnLeftPile = STARTING_CONFIGURATION; //Changed the amount of matches on the pile to the starting configuration of 5.
			matchesOnMiddlePile = STARTING_CONFIGURATION;
			matchesOnRightPile = STARTING_CONFIGURATION;
			totalMatches = 15;
			currentPlayer = STARTING_PLAYER; //Changes the current player to the starting player of white.
			display.printView(matchesOnLeftPile, matchesOnMiddlePile, matchesOnRightPile, currentPlayer); //Reprint the view with the new inputs.
		}

		static void buttonClick(int paramInt, char whichStack) //amount of pebbles the user wants to take
		{
			try {
				SimpleNim.PlayerTyped localPlayerTyped = SimpleNim.getPlayersPileSelection(paramInt);
				switch (localPlayerTyped.command) { //If a player typed a command that is one of the below cases. Do this :
				case HELP: //In the case of HELP, print help options.
					System.out.println();
					System.out.println("t:  \t   Take.");
					System.out.println("u:         Undo last move. (Can be repeated.)");
					System.out.println("r:         Redo last undo. (Can be repeated.)");
					System.out.println("b:         Go back to beginning. (That is, start again.)");
					System.out.println("?:         Print this help.");
					System.out.println("and D,d:   Turn debugging on,off");
					System.out.println();
					break;
				case UNDO: //In the case of UNDO
					if (SimpleNim.madeMovesPrefixLength <= 0) throw new NimError(1538);
					SimpleNim.madeMovesPrefixLength -= 1; //Take away one move from the moves made.
					((Move)SimpleNim.moves.elementAt(SimpleNim.madeMovesPrefixLength)).unMake(); //Unmake the move at this location.
					break;
				case REDO: //In the case of REDO
					if (SimpleNim.madeMovesPrefixLength >= SimpleNim.moves.size()) throw new NimError(1539);
					((Move)SimpleNim.moves.elementAt(SimpleNim.madeMovesPrefixLength)).make(); //Redo the move at this location.
					SimpleNim.madeMovesPrefixLength += 1; //Add one to the moves made.
					break;
				case BEGIN: //In the case of BEGIN
					reset(); //reset the game
					SimpleNim.madeMovesPrefixLength = 0; //0 moves made because of reset.
					System.out.println("Game restarted.\n"); //Print that the game has been restarted.
					break;
				case DEBUG: //Turn on debug mode.
					System.out.println(new StringBuilder().append("Debug ").append(SimpleNim.DEBUG ? "enabled" : "disabled").append(".\n").toString());
					break;
				case TAKE:
					int i = paramInt; //Amount of pebbles the user wants to take
					char j = whichStack;
					System.out.println(new StringBuilder().append("how many wants to move").append(i).toString()); //prints the amount of pebbles the user wants to take.

					Move localMove = new Move(i,j); //Create the move the user wishes to take
					localMove.make();//Make that move.

					SimpleNim.moves.setSize(SimpleNim.madeMovesPrefixLength); //Increases the vector size to accommodate the new move just taken.
					SimpleNim.moves.addElement(localMove);//Add the move to the list of moves.
					SimpleNim.madeMovesPrefixLength += 1; //Add 1 to the size of the moves made.
					System.out.println(new StringBuilder().append(localMove.string()).append("\n").toString());
					if (gameIsOver())
						System.out.println(new StringBuilder().append("Player ").append(currentPlayer).append(" is the winner!").toString()); //If the game is over display the winner.(whoevers turn it is as the last person who moved took the last pebble and therefore loses)
					break;
				default:
					throw new NimError(-1512);
				}
			}
			catch (NimError localNimError)
			{
				System.out.println(new StringBuilder().append("Whoops! ").append(SimpleNim.errorDescription(localNimError.errorCode, localNimError.description)).append("\n").toString());
				if (localNimError.errorCode == 2000);
			}
		}

		//A class which contains all information about a 'move' in the game.
		static class Move
		{
			SimpleNim.GameState.Player player;//Create a player variable
			int howManyTaken; 
			char stack;

			Move(int paramInt, char whichStack)
			{
				this.player = SimpleNim.GameState.currentPlayer; //Make the moves player the current game states player.
				this.howManyTaken = paramInt; //How many matches you want to take.
				this.stack = whichStack;
			}

			void make()
					throws NimError
			{
				if (SimpleNim.DEBUG) System.out.println("548: Making move " + string());
				if (this.howManyTaken > SimpleNim.GameState.totalMatches) throw new NimError(956);
				
				if(stack == 'l'){ //If from left stack.
					SimpleNim.GameState.matchesOnLeftPile -= this.howManyTaken;//Make the matches on the pile the matches on pile minus how many where taken in that move.
					SimpleNim.GameState.totalMatches -= this.howManyTaken; //Remove taken from total.
				}
				if(stack == 'm'){ //If from middle stack.
					SimpleNim.GameState.matchesOnMiddlePile -= this.howManyTaken;//Make the matches on the pile the matches on pile minus how many where taken in that move.
					SimpleNim.GameState.totalMatches -= this.howManyTaken;//Remove taken from total.
				}
				if(stack == 'r'){ //If from right stack.
					SimpleNim.GameState.matchesOnRightPile -= this.howManyTaken;//Make the matches on the pile the matches on pile minus how many where taken in that move.
					SimpleNim.GameState.totalMatches -= this.howManyTaken;//Remove taken from total.
				}
				SimpleNim.GameState.swapPlayers(); //Swap players for the next turn.
				display.printView(matchesOnLeftPile, matchesOnMiddlePile, matchesOnRightPile, currentPlayer); //Print new view after the move is made.
			}
			String string() { return "Player " + this.player + " took " + this.howManyTaken + " cats" + "."; } 
			void unMake() throws NimError { //Does the exact opposite of make function, unmakes a move.
				if (SimpleNim.DEBUG) System.out.println("839: Unmaking move " + string());
				if (this.howManyTaken > 15 - SimpleNim.GameState.totalMatches) throw new NimError(-825);
				if(stack == 'l'){
					SimpleNim.GameState.matchesOnLeftPile += this.howManyTaken;
					SimpleNim.GameState.totalMatches += this.howManyTaken;
				}
				if(stack == 'm'){
					SimpleNim.GameState.matchesOnMiddlePile += this.howManyTaken;
					SimpleNim.GameState.totalMatches += this.howManyTaken;
				}
				if(stack == 'r'){
					SimpleNim.GameState.matchesOnRightPile += this.howManyTaken;
					SimpleNim.GameState.totalMatches += this.howManyTaken;
				}
				SimpleNim.GameState.swapPlayers();
				display.printView(matchesOnLeftPile, matchesOnMiddlePile, matchesOnRightPile, currentPlayer);
			}
		}
		
		//Describes the type of characters possible in the game
		public static enum Player
		{
			Black, White;
		}
	}
}

