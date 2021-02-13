package interfaces;

import java.awt.Graphics;

public interface IThingDrawer {
	/**
	 * Draws whatever was assigned to this IThingDrawer.
	 * @param g The graphics used to draw.
	 * @return True if the thing was drawn, false if not.
	 */
	public boolean draw(Graphics g, double xScale, double yScale);
}
