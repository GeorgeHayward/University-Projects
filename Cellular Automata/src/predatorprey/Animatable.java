package predatorprey;

import java.awt.Graphics;

public interface Animatable {

	/** Step the animation state forward by one frame. */
	public void step();

	/**
	 * Paint the current state of the animation onto the graphics canvas.
	 */
	public void paint(Graphics pGraphics);

	/** Get the width of this animation in pixels. */
	public int getWidth();

	/** Get the height of this animation in pixels. */
	public int getHeight();

}
