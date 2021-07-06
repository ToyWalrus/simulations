package gui.drawers;

import java.awt.Color;
import java.awt.Graphics;

import models.entities.Entity;
import models.entities.EntityStats;
import models.entities.Entity.Gender;
import models.genes.AwarenessGene;
import models.genes.SizeGene;
import models.genes.SpeedGene;
import models.world.PositionInt;
import util.HelperFunctions;

public class EntityDrawer {
	public static Color LOW_SPEED = new Color(0, 0, 255);
	public static Color HIGH_SPEED = new Color(0, 255, 0);

	public static Color MALE = new Color(66, 135, 245);
	public static Color FEMALE = new Color(245, 66, 206);

	public static double startingSpeed = -1;

	public static void draw(Graphics g, Entity entity, PositionInt position, double xScale, double yScale) {
		draw(g, entity, position, xScale, yScale, false);
	}

	public static void draw(Graphics g, Entity entity, PositionInt position, double xScale, double yScale,
			boolean debug) {
		// get color
		double speed = entity.getGene(SpeedGene.name).getValue();
		if (startingSpeed == -1) {
			startingSpeed = speed;
		}

		// get size
		int size = (int) Math.round(entity.getGene(SizeGene.name).getValue() * 8.0);
		int sizeX = (int) Math.round(size * xScale);
		int sizeY = (int) Math.round(size * yScale);

		PositionInt drawCenter = new PositionInt(position.x - sizeX / 4, position.y - sizeY / 4);

		// shape?
		g.setColor(HelperFunctions.lerp(LOW_SPEED, HIGH_SPEED, speed / (startingSpeed * 2)));
		g.fillOval(drawCenter.x, drawCenter.y, sizeX, sizeY);

		int innerSizeX = (int) Math.round(sizeX * .75);
		int innerSizeY = (int) Math.round(sizeY * .75);
		g.setColor(entity.getGender() == Gender.Male ? MALE : FEMALE);
		g.fillOval(drawCenter.x + (sizeX - innerSizeX) / 2, drawCenter.y + (sizeY - innerSizeY) / 2, innerSizeX,
				innerSizeY);

		if (debug) {
			int barLength = sizeX * 2;

			EntityStats stats = entity.getStats();
			double energy = stats.getEnergy() / stats.getMaxEnergy();
			double hunger = stats.getHunger() / 1.0;
			double thirst = stats.getThirst() / 1.0;
			double reproUrge = stats.getReproductiveUrge() / 1.0;

			int barSpacing = sizeY / 3 + 2;
			int xPos = drawCenter.x - (barLength / 2);
			int yPos = drawCenter.y - (sizeY / 2);

			drawDebugBar(g, new PositionInt(xPos, yPos - barSpacing * 1), barLength, energy, xScale,
					new Color(194, 178, 4));
			drawDebugBar(g, new PositionInt(xPos, yPos - barSpacing * 2), barLength, reproUrge, xScale, Color.red);
			drawDebugBar(g, new PositionInt(xPos, yPos - barSpacing * 3), barLength, hunger, xScale,
					new Color(29, 194, 4));
			drawDebugBar(g, new PositionInt(xPos, yPos - barSpacing * 4), barLength, thirst, xScale, Color.cyan);

			int awarenessRadius = (int) Math.round(entity.getGene(AwarenessGene.name).getValue());
			drawAwarenessRadius(g, drawCenter, (int) Math.round(awarenessRadius * xScale),
					(int) Math.round(awarenessRadius * yScale));
		}
	}

	private static void drawDebugBar(Graphics g, PositionInt position, int length, double percentFull, double scale,
			Color color) {
		int height = (int) Math.round(3 * scale);

		g.setColor(color);
		g.fillRect(position.x, position.y, (int) Math.round((double) length * percentFull), height);
		g.setColor(Color.black);
		g.fillRect(position.x + length, position.y, 2, height);
	}

	private static void drawAwarenessRadius(Graphics g, PositionInt position, int xRadius, int yRadius) {
		g.setColor(new Color(179, 179, 179));
		g.drawOval(position.x - xRadius, position.y - yRadius, xRadius * 2, yRadius * 2);
	}
}
