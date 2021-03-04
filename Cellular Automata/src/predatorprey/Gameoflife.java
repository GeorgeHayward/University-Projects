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

//PLEASE READ! So as you can see I didn't manage to get the spirals, however I believe that I must have been
//very close and just missed something in the rules (even though I checked them about 100 times :<)
package predatorprey;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class Gameoflife extends JFrame implements ActionListener {

	private static Gameoflife mApplication;
	private JPanel mContentPane = null;
	private Animator mCellularAutomatonAnimator = null;
	private JButton mButton = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create version of this class.
		mApplication = new Gameoflife();
		// Make a runnable
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Run init App below.
				mApplication.initApp();

			}
		});
	}

	/**
	 * Setup the GUI by calling {@link Gameoflife#setup()} and set the
	 * application running.
	 */
	private void initApp() {
		// Set up the window.
		setup();
		// Set window to preffered size.
		pack();
		// Make the window visible.
		setVisible(true);
		// Start the Animator thread.
		new Thread(mCellularAutomatonAnimator).start();
	}

	/**
	 * Create the GUI components for this application and ley them out on the
	 * content pane of a top-level window.
	 * 
	 */
	private void setup() {
		// Name of the window / program
		setTitle("Game of Life");
		// Make the program close on exit button.
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Create reset button
		mButton = new JButton();
		mButton.setText("Reset");
		mButton.addActionListener(this);

		// Create the board
		mCellularAutomatonAnimator = new Animator();

		// Insert the board into a content pane.
		mContentPane = (JPanel) getContentPane();
		mContentPane.setLayout(new BorderLayout());
		mContentPane.add(mButton, BorderLayout.SOUTH);
		mContentPane.add(mCellularAutomatonAnimator, BorderLayout.CENTER);
	}

	/* If the reset button is hit, reset the board */
	public void actionPerformed(ActionEvent e) {
		mCellularAutomatonAnimator.resetBoard();
	}

}