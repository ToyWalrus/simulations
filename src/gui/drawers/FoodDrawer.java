package gui.drawers;

import java.awt.Color;
import java.awt.Graphics;

import models.world.Food;
import models.world.PositionInt;

public class FoodDrawer {
	public static int FOOD_SIZE = 4;
	public static Color FOOD_COLOR = new Color(116, 195, 101);
	
	public static void draw(Graphics g, Food food, PositionInt position) {
		// TODO eventually override the color and size based on food properties
		g.setColor(FOOD_COLOR);		
		g.fillOval(position.x, position.y, FOOD_SIZE, FOOD_SIZE);
	}

}
