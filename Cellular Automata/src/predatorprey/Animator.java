/**
 * This file is part of a project entitled Week8Samples which is provided as
 * sample code for the following Macquarie University unit of study:
 * 
 * COMP229 "Object Oriented Programming Practices"
 * 
 * Copyright (c) 2011-2012 Dominic Verity and Macquarie University.
 * 
 * Week8Samples is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Week8Samples is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Week8Samples. (See files COPYING and COPYING.LESSER.) If not,
 * see <http://www.gnu.org/licenses/>.
 */

package predatorprey;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * <p>
 * The Animator class maintains and updates the current state of our animation.
 * In this case it contains a list of Sprite objects, each one of which
 * represents an image on the screen (storing its current position and
 * velocity).
 * </p>
 * 
 * <p>
 * This class extends JPanel, to provide a component upon which the animation is
 * drawn. It also implements Runnable in order to provide a thread which
 * periodically updates the animation and calls <code>repaint()</code> to
 * persuade the event dispatch thread to paint the next frame.
 * </p>
 * 
 * @author Dominic Verity
 *
 */
public class Animator extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	// Size of the animation window.
	public static final int CANVAS_WIDTH = 500;
	public static final int CANVAS_HEIGHT = 500;
	public static final int FRAME_PAUSE = 100;

	// Create a Cellularautomata object.
	Cellularautomata existingCA = null;

	/**
	 * Constructor, sets preffered size of window and defines the
	 * Cellularautomata object.
	 */
	public Animator() {
		// Set windows preferred size
		setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		// Instantiate object
		existingCA = new Cellularautomata();
	}

	/**
	 * Reset's the board of cells, making a new cell population.
	 */
	public synchronized void resetBoard() {
		existingCA = new Cellularautomata();
	}

	/**
	 * Steps to the nextTable
	 */
	public synchronized void step() {
		existingCA.step();
	}

	/**
	 * The paintComponent() method which is called from the event dispatch
	 * thread whenever the GUI wants to repaint an Animator component.
	 */
	public synchronized void paintComponent(Graphics pGraphics) {
		pGraphics.clearRect(0, 0, getWidth(), getHeight());
		existingCA.paint(pGraphics);
	}

	/**
	 * The run() method of an Animator thread simply loops calling step() to
	 * update the positions of the cell board and then calling repaint() to
	 * inform the event dispatch thread that it needs to paint the next frame.
	 * 
	 * We pause between each frame for FRAME_PAUSE milliseconds, so decreasing
	 * FRAME_PAUSE we can increase the framerate (and thus the speed) of the
	 * animation.
	 */
	public void run() {
		while (true) {
			step();
			repaint();
			try {
				Thread.sleep(FRAME_PAUSE);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
