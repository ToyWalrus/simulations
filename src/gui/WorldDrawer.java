package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Map;

import javax.swing.JPanel;

import models.world.Food;
import models.world.Position;
import models.world.World;

public class WorldDrawer extends JPanel {
	public static int FOOD_SIZE = 8;
	private World world;
	private int panelWidth;
	private int panelHeight;

	public WorldDrawer(World world, int panelWidth, int panelHeight) {
		this.world = world;

		this.panelWidth = panelWidth;
		this.panelHeight = panelHeight;
		this.setSize(panelWidth, panelHeight);
	}

	public void paint(Graphics g) {
		super.paint(g);

		Color foodColor = new Color(116, 195, 101);
		g.setColor(foodColor);
		for (Map.Entry<Position, Food> e : world.getFood().entrySet()) {
			Position panelPoint = worldPointToPanelPoint(e.getKey());
			g.fillOval((int) panelPoint.x, (int) panelPoint.y, FOOD_SIZE, FOOD_SIZE);
		}
	}

	private Position worldPointToPanelPoint(Position p) {
		int worldMaxX = world.getWorldWidth();
		int worldMaxY = world.getWorldHeight();

		double percentX = p.x / (double) worldMaxX;
		double percentY = p.y / (double) worldMaxY;

		// Y may be inversed
		return new Position(Math.floor(percentX * panelWidth), Math.floor(percentY * panelHeight));
	}
}
