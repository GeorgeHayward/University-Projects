package simplenim;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import simplenim.SimpleNim.*;
import simplenim.SimpleNim.GameState.Player;
import simplenim.SimpleNim.PlayerTyped.Command;

public class SimpleNimTest {
		
	@Before
	public void setup() {
		SimpleNim.main(null);
	}
	@Test //Test all cases of get getPlayersPileSelection.
	public void getPlayersPileSelectionTest() throws NimError { 
		//Check that if INT input is between 0-4 the command will be 'TAKE'.
		PlayerTyped testType;
		Command test = Command.TAKE;
		testType = SimpleNim.getPlayersPileSelection(1);
		assertEquals(test, testType.command);
		testType = SimpleNim.getPlayersPileSelection(2);
		assertEquals(test, testType.command);
		testType = SimpleNim.getPlayersPileSelection(3);
		assertEquals(test, testType.command);
		
		//Check that if INT input is 10 the command will be 'UNDO'.
		test = Command.UNDO;
		testType = SimpleNim.getPlayersPileSelection(10);
		assertEquals(test, testType.command);
		
		//Check that if INT input is 11 the command will be 'REDO'.
		test = Command.REDO;
		testType = SimpleNim.getPlayersPileSelection(11);
		assertEquals(test, testType.command);
		
		//Check that if INT input is 12 the command will be 'BEGIN'.
		test = Command.BEGIN;
		testType = SimpleNim.getPlayersPileSelection(12);
		assertEquals(test, testType.command);
		
		//Check that if INT input is 15 the command will be 'HELP'.
		test = Command.HELP;
		testType = SimpleNim.getPlayersPileSelection(15);
		assertEquals(test, testType.command);
	}
	
	@Test //Test the two possible ends of the boolean.
	public void GameStateGameIsOverTest() {
		GameState.matchesOnLeftPile = 0;
		GameState.matchesOnRightPile = 0;
		GameState.matchesOnMiddlePile = 0;
		assertEquals(true,GameState.gameIsOver()); //Check that the game is over.
		GameState.matchesOnLeftPile = 1;
		GameState.matchesOnRightPile = 4;
		GameState.matchesOnMiddlePile = 6;
		assertEquals(false,GameState.gameIsOver()); //Check that the game isn't over.
	}
	
	@Test //Test all of the variable changes in the reset method.
	public void GameStateResetTest() {
		GameState.matchesOnLeftPile = 3;
		GameState.matchesOnMiddlePile = 5;
		GameState.matchesOnRightPile = 2;
		GameState.totalMatches = 10;
		GameState.currentPlayer = Player.Black;
		GameState.reset();
		assertEquals(5, GameState.matchesOnLeftPile);
		assertEquals(Player.White, GameState.currentPlayer);
		assertEquals(15, GameState.totalMatches);
		
	}
	
	@Test //Tests important cases of undo / redo / take.
	public void GameStateButtonClickTest() {
		//Make move.
		GameState.buttonClick(3, 'l');
		assertEquals(2, GameState.matchesOnLeftPile); //Check that 3 has been taken from left pile.
		GameState.buttonClick(2, 'l');
		assertEquals(0, GameState.matchesOnLeftPile); //Check that 2 has been taken from left pile.
		assertEquals(2, SimpleNim.madeMovesPrefixLength); //Check that the number of moves is 2.
		assertEquals(2, SimpleNim.moves.size());//Check the moves made = 2.
		
		//Undo
		GameState.buttonClick(10, 'z');
		assertEquals(2, GameState.matchesOnLeftPile); //Check that the undo button takes away the last moves.
		GameState.buttonClick(10, 'z');
		assertEquals(5, GameState.matchesOnLeftPile);
		GameState.buttonClick(10, 'z');
		assertEquals(5, GameState.matchesOnLeftPile); //Check that undo with no moves made does nothing.
		assertEquals(0, SimpleNim.madeMovesPrefixLength); //Check that the moves made is 0 after the undos.
		assertEquals(2, SimpleNim.moves.size()); //Check that the made moves are still there.
		
		//Redo
		GameState.buttonClick(11, 'z');
		assertEquals(2, GameState.matchesOnLeftPile); //Check that redo button adds the last move again.
		GameState.buttonClick(11, 'z');
		assertEquals(0, GameState.matchesOnLeftPile);
		GameState.buttonClick(11, 'z');
		assertEquals(0, GameState.matchesOnLeftPile);//Check that redo button with no moves does nothing.
		assertEquals(2, SimpleNim.madeMovesPrefixLength); //Check that redo, re adds the moves made.
		
		//FinishGameStateTest
		GameState.buttonClick(3, 'r');
		GameState.buttonClick(2, 'r');
		GameState.buttonClick(3, 'm');
		GameState.buttonClick(2, 'm');
		assertEquals(true, GameState.gameIsOver()); //Test if removing all the buttons makes the game over.
		
		//UndoEverything
		while(GameState.totalMatches != 15){
			GameState.buttonClick(10, 'z');
		}
		assertEquals(0, SimpleNim.madeMovesPrefixLength); //Check that all the moves have been removed from the tracker variable.
		assertEquals(6, SimpleNim.moves.size());//Check that they are still stored in moves.
		
		//RedoEverything
		while(GameState.totalMatches != 0){
			GameState.buttonClick(11, 'z');
		}
		assertEquals(6, SimpleNim.madeMovesPrefixLength); //Check that all the moves have been added to the tracker variable.
		assertEquals(6, SimpleNim.moves.size());//Check that they are still stored in moves.
		assertEquals(true, GameState.gameIsOver());
	}
}
	