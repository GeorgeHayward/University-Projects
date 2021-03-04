/*
 * This file is part of COMP332 Assignment 1.
 *
 * Copyright (C) 2019 Dominic Verity, Macquarie University.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.mq.frogsandtoads

import doodle.core._
import doodle.syntax._
import doodle.image._

/**
  * A puzzle state is given as a 1-dimensional array of cell values.
  */
class PuzzleState private (
    board: Vector[PuzzleState.Cell],
    loc: Int
) {

  import PuzzleState._

  val size = board.size
  val emptyLoc = loc

  def isTerminalState(): Boolean = {
    board.slice(0, emptyLoc).forall(_ == Toad) &&
    board(emptyLoc) == Empty &&
    board.slice(emptyLoc + 1, size).forall(_ == Frog)
  }

  def isInitialState(): Boolean = {
    board.slice(0, emptyLoc).forall(_ == Frog) &&
    board(emptyLoc) == Empty &&
    board.slice(emptyLoc + 1, size).forall(_ == Toad)
  }

  //Frog jumps over head of toad to the right
  def jumpFromLeft(): Option[PuzzleState] = {
    //If the emptyLoc is atleast 2 spaces from the far left.
    if ((board(emptyLoc) != board(0)) && (board(emptyLoc) != board(
          1
        ))) {
      //If there is a Toad inbetween an empty space and a Frog.
      if (board(emptyLoc - 1) == Toad && (board(emptyLoc - 2) == Frog)) {
        // Cut out the cells that will be altered. Replace with the new states cells.
        val newPuzzleState = new PuzzleState(
          board
            .slice(0, emptyLoc - 2) ++ Vector(Empty, Toad, Frog) ++ board
            .slice(emptyLoc + 1, size),
          emptyLoc - 2
        )
        Some(newPuzzleState)
      } else None
    } else None
  }

  //Toad jumps over head of frog to the left
  def jumpFromRight(): Option[PuzzleState] = {
    //If the emptyLoc is atleast 2 spaces from the far right.
    if ((board(emptyLoc) != board(size - 2)) && (board(emptyLoc) != board(
          size - 1
        ))) {
      //If there is a frog inbetween an empty space and a toad.
      if (board(emptyLoc + 1) == Frog && (board(emptyLoc + 2) == Toad)) {
        val newPuzzleState = new PuzzleState(
          board
            .slice(0, emptyLoc) ++ Vector(Toad, Frog, Empty) ++ board
            .slice(emptyLoc + 3, size),
          emptyLoc + 2
        )
        Some(newPuzzleState)
      } else None
    } else None
  }

  //Frog slides from left to right
  def slideFromLeft(): Option[PuzzleState] = {
    //If the emptyLoc is not in position 0
    if (board(emptyLoc) != board(0)) {
      //If the frog can legally slide to the right
      if (board(emptyLoc - 1) == Frog) {
        val newPuzzleState = new PuzzleState(
          board.slice(0, emptyLoc - 1) ++ Vector(Empty, Frog) ++ board
            .slice(emptyLoc + 1, size),
          emptyLoc - 1
        )
        Some(newPuzzleState)
      } else None
    } else None
  }

  //Toad slides from right to left
  def slideFromRight(): Option[PuzzleState] = {
    //If the emptyLoc is not in position size
    if (board(emptyLoc) != board(size - 1)) {
      //If the toad can legally slide to the left
      if (board(emptyLoc + 1) == Toad) {
        val newPuzzleState = new PuzzleState(
          board.slice(0, emptyLoc) ++ Vector(Toad, Empty) ++ board
            .slice(emptyLoc + 2, size),
          emptyLoc + 1
        )
        Some(newPuzzleState)
      } else None
    } else None
  }

  // Returns an image representing this puzzleStates board
  private def generateBoard(iter: Int): Image = {
    iter match {
      // All rectangles added, return an empty image to stop the recursion.
      case -1 => Image.empty
      // Not all rectangles added yet
      case x =>
        // If the selected locations a Frog
        if (board(x) == Frog) {
          // Create lightGreen rectangle representing a frog and place besides the recursive creation of all other positions on board.
          Image
            .rectangle(20, 20)
            .fillColor(Color.lightGreen) beside generateBoard(x - 1)
        }
        // Create darkGreen rectangle representing a toad and place besides the recursive creation of all other positions on board.
        else if (board(x) == Toad) {
          Image
            .rectangle(20, 20)
            .fillColor(Color.darkGreen) beside generateBoard(x - 1)
        }
        // Create white rectangle representing a emptySpace and place besides the recursive creation of all other positions on board.
        else {
          Image.rectangle(20, 20).fillColor(Color.white) beside generateBoard(
            x - 1
          )
        }
    }

  }

  // Get a string version of the board for testing reasons.
  override def toString() = {
    val cellStrings: Vector[String] = board.map(_.toString)
    val newCellStrings: Vector[String] = cellStrings.collect {
      case cell: String => simpleChar(cell)
    }
    val puzzleStateString: String = newCellStrings.reduceLeft(_ ++ "|" ++ _)
    val result: String = "[" ++ puzzleStateString ++ "]"
    result
  }

  // Converts strings to simplified versions. E.g. Frog => F
  def simpleChar(string: String): String = {
    if (string == "Frog") {
      "F"
    } else if (string == "Toad") {
      "T"
    } else {
      " "
    }
  }

}

/**
  * Companion object for the [[PuzzleState]] class, provides a public constructor.
  */
object PuzzleState {

  /**
    * Case class for case objects to represent the possible contents of a
    * cell in the puzzle.
    */
  sealed abstract class Cell
  case object Frog extends Cell
  case object Toad extends Cell
  case object Empty extends Cell

  /**
    * Construct a [[PuzzleState]] object in the initial state for a
    * puzzle with specified numbers of frogs and toads.
    *
    * @param frogs number of frogs to place on the left of the [[PuzzleState]]
    * to be constructed
    * @param toads number of toads top place on the right of the [[PuzzleState]]
    * to be constructed
    */
  def apply(frogs: Int, toads: Int): PuzzleState = {
    if (frogs <= 0 || frogs > 10)
      throw new Exception("The number of frogs must be between 1 and 10.")

    if (toads <= 0 || toads > 10)
      throw new Exception("The number of frogs must be between 1 and 10.")

    new PuzzleState(
      Vector.fill(frogs)(Frog) ++ Vector(Empty) ++
        Vector.fill(toads)(Toad),
      frogs
    )
  }

  // Create puzzle object from a string. (testing purposes)
  def apply(string: String): PuzzleState = {
    var newString = string.filter(_ != '[')
    newString = newString.filter(_ != ']')
    newString = newString.filter(_ != '|')
    val vectorList = List()
    for (c <- newString) {
      if (c == 'F') {
        vectorList ++ Vector(Frog)
      }
      if (c == 'T') {
        vectorList ++ Vector(Toad)
      }
      if (c == ' ') {
        vectorList ++ Vector(Empty)
      }
    }
    new PuzzleState(recursiveAddVector(newString), newString.indexOf(' '))
  }

  //Recursively combine vectors from input string to produce a puzzleState for above apply method
  def recursiveAddVector(string: String): Vector[Cell] = {
    string.size match {
      case 1 =>
        if (string.charAt(0) == 'F') {
          Vector(Frog)
        } else if (string.charAt(0) == 'T') {
          Vector(Toad)
        } else {
          Vector(Empty)
        }
      case _ =>
        if (string.charAt(0) == 'F') {
          Vector(Frog) ++ recursiveAddVector(string.substring(1))
        } else if (string.charAt(0) == 'T') {
          Vector(Toad) ++ recursiveAddVector(string.substring(1))
        } else {
          Vector(Empty) ++ recursiveAddVector(string.substring(1))
        }
    }
  }

  /**
    * Find a sequence of legal moves of the frogs and toads puzzle from a specified starting
    * [[PuzzleState]] to the terminal [[PuzzleState]].
    *
    * @param start the starting [[PuzzleState]]
    * @return the sequence of [[PuzzleState]] objects passed through in the transit from
    * state `start` to the terminal state (inclusive). Returns the empty sequence if no solution
    * is found.
    */
  def solve(start: PuzzleState): Seq[PuzzleState] = {
    // Create a new sequence containing only the input 'start' state.
    val newStateSolution = Seq() :+ start
    // Check / Return if the input is the terminal state
    start.isTerminalState() match {
      case true =>
        newStateSolution
      // Not terminal, try moves.
      case false =>
        // Try move
        start.slideFromLeft() match {
          // Moves valid
          case Some(newState) =>
            // Create new Seq which holds the current state and the next move's state solved recursively.
            val futureStates = newStateSolution ++ solve(newState)
            // If futureStates last item is terminal return futureStates
            if (futureStates.last.isTerminalState()) {
              return futureStates
            }
          case None =>
        }
        // Try next move, do same as above.
        start.slideFromRight() match {
          case Some(newState) =>
            val futureStates = newStateSolution ++ solve(newState)
            if (futureStates.last.isTerminalState()) {
              return futureStates
            }
          case None =>
        }
        // Try next move, do same as above.
        start.jumpFromLeft() match {
          case Some(newState) =>
            val futureStates = newStateSolution ++ solve(newState)
            if (futureStates.last.isTerminalState()) {
              return futureStates
            }
          case None =>
        }
        // Try next move, do same as above.
        start.jumpFromRight() match {
          case Some(newState) =>
            val futureStates = newStateSolution ++ solve(newState)
            if (futureStates.last.isTerminalState()) {
              return futureStates
            }
          case None =>
        }
        // No moves, return an empty sequence to backtrack.
        Seq()
    }
  }

  /**
    * Call [[solve]] to generate a sequence of legal moves from a specified
    * starting [[PuzzleState]] to the terminal [[PuzzleState]]. Render each state in that solution as
    * an image and return the resulting sequence of images.
    *
    * @param start the starting [[PuzzleState]]
    * @return the sequence of [[Image]] objects depicting the sequence of puzzle states
    * passed through in the transit from the `start` state to the terminal state.
    */
  def animate(start: PuzzleState): Seq[Image] = {
    // Get the the sequence of states from initial to terminal.
    val solution = solve(start)
    // Create a new image sequence containing a visual representation of the start state
    val imageSeq = Seq() :+ (start.generateBoard(start.size - 1) rotate 180.degrees)
    // Check if this start state is actually the terminal state
    start.isTerminalState() match {
      // If true, stop recursion by returning just this imageSeq
      case true =>
        imageSeq
      //If false, combine imageSeq with a recursive call of animate but with the next item in the solution sequence
      case false =>
        val newImageSeq = imageSeq ++ animate(solution(1))
        newImageSeq
    }
  }

  /**
    * Create an animation of a solution to the frogs and toads puzzle, starting from the initial
    * [[PuzzleState]] and ending at the terminal [[PuzzleState]].
    *
    * @param frogs the number of frogs in the puzzle (between 1 and 10 inclusive)
    * @param toads the number of toads in the puzzle (between 1 and 10 inclusive)
    */
  def animate(frogs: Int, toads: Int): Seq[Image] =
    animate(PuzzleState(frogs, toads))
}
