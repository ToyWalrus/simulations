package gui.panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Map;

import javax.swing.JPanel;

import gui.drawers.EntityDrawer;
import gui.drawers.FoodDrawer;
import models.entities.Entity;
import models.world.Food;
import models.world.Position;
import models.world.PositionInt;
import models.world.World;
import interfaces.IObserver;

public class WorldPanel extends JPanel implements IObserver<World> {
	private World world;
	private int panelWidth;
	private int panelHeight;

	public WorldPanel(int panelWidth, int panelHeight) {
		this.panelWidth = panelWidth;
		this.panelHeight = panelHeight;
	}

	public WorldPanel(int panelWidth, int panelHeight, World world) {
		this(panelWidth, panelHeight);
		setWorld(world);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(panelWidth, panelHeight);
	}

	public void setWorld(World world) {
		if (this.world != null) {
			this.world.deRegisterObserver(this);
		}

		this.world = world;

		if (this.world != null) {
			this.world.registerObserver(this);
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		if (world == null)
			return;

		double xScale = getWidth() / world.getWorldWidth();
		double yScale = getHeight() / world.getWorldHeight();

		for (Map.Entry<Position, Food> entry : world.getFood().entrySet()) {
			PositionInt panelPoint = worldPointToPanelPoint(entry.getKey());
			FoodDrawer.draw(g, entry.getValue(), panelPoint, xScale, yScale);
		}

		for (Entity entity : world.getEntities()) {
			PositionInt panelPoint = worldPointToPanelPoint(entity.getPosition());
			EntityDrawer.draw(g, entity, panelPoint, xScale, yScale);
		}
	}

	private PositionInt worldPointToPanelPoint(Position p) {
		int worldMaxX = world.getWorldWidth();
		int worldMaxY = world.getWorldHeight();

		double percentX = p.x / (double) worldMaxX;
		double percentY = p.y / (double) worldMaxY;

		// Y may be inverted
		return new PositionInt(new Position(Math.floor(percentX * panelWidth), Math.floor(percentY * panelHeight)));
	}

	@Override
	public void update(World updatedWorld) {
		this.repaint();
	}

	public World getWorld() {
		return world;
	}
}
