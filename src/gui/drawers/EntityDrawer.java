package gui.drawers;

import java.awt.Color;
import java.awt.Graphics;

import models.entities.Entity;
import models.entities.EntityStats;
import models.world.PositionInt;

public class EntityDrawer {
	public static void draw(Graphics g, Entity entity, PositionInt position) {
		draw(g, entity, position, false);
	}

	public static void draw(Graphics g, Entity entity, PositionInt position, boolean debug) {
		// get color
		g.setColor(new Color(0, 0, 255));

		// get size
		int size = 15;

		// shape?
		g.fillOval(position.x, position.y, size, size);		

		if (debug) {
			int barLength = size * 2;

			EntityStats stats = entity.getStats();
			double energy = stats.getEnergy() / (double) 1;
			double hunger = stats.getHunger() / (double) 1;
			double thirst = stats.getThirst() / (double) 1;

			int barSpacing = size / 3 + 2;
			int xPos = position.x - (barLength / 2);
			int yPos = position.y - (size / 2);

//			drawDebugBar(g, new PositionInt(xPos, yPos - barSpacing), barLength, energy, new Color(194, 178, 4));
			drawDebugBar(g, new PositionInt(xPos, yPos - barSpacing * 2), barLength, hunger, new Color(29, 194, 4));
//			drawDebugBar(g, new PositionInt(xPos, yPos - barSpacing * 3), barLength, thirst, Color.cyan);
		}
	}

	private static void drawDebugBar(Graphics g, PositionInt position, int length, double percentFull, Color color) {
		int height = 4;

		g.setColor(color);
		g.fillRect(position.x, position.y, (int) Math.round((double)length * percentFull), height);
		g.setColor(Color.black);
		g.fillRect(position.x + length, position.y, 2, height);
	}
}
