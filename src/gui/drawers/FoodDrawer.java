package gui.drawers;

import java.awt.Color;
import java.awt.Graphics;

import models.world.Food;
import models.world.PositionInt;
import util.HelperFunctions;

public class FoodDrawer {
	public static int MAX_FOOD_SIZE = 4;
	public static Color RIPE_FOOD_COLOR = new Color(116, 195, 101);
	public static Color UNRIPE_FOOD_COLOR = new Color(245, 206, 32);

	public static void draw(Graphics g, Food food, PositionInt position, double xScale, double yScale) {
		double nutritionProgress = food.getNutrition() / food.getMaxPossibleNutrition();
		double size = HelperFunctions.lerp(1, MAX_FOOD_SIZE, nutritionProgress);

		g.setColor(HelperFunctions.lerp(UNRIPE_FOOD_COLOR, RIPE_FOOD_COLOR, nutritionProgress));
		g.fillOval(position.x, position.y, (int) Math.round(size * xScale), (int) Math.round(size * yScale));
	}

}
