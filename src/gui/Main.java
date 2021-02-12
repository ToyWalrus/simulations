package gui;

import java.util.HashMap;
import java.util.Random;

import javax.swing.JFrame;

import models.entities.Entity;
import models.world.Food;
import models.world.Position;
import models.world.World;

public class Main {

	public static void main(String[] args) {
		int worldWidth = 100;
		int worldHeight = 100;
		int windowWidth = 1000;
		int windowHeight = 750;
		
		World w = new World(generateRandomFood(100, worldWidth, worldHeight), new HashMap<Position, Entity>(), worldWidth, worldHeight);
		WorldDrawer wd = new WorldDrawer(w, windowWidth - 50, windowHeight - 50);
		
		JFrame frame = new JFrame("World view");
		frame.setSize(windowWidth, windowHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(wd);
		frame.setVisible(true);
	}
	
	private static HashMap<Position, Food> generateRandomFood(int count, int maxX, int maxY) {
		HashMap<Position, Food> foods = new HashMap<Position, Food>();
		Random rand = new Random(System.currentTimeMillis());
		
		for (int i = 0; i < count; ++i) {
			double x = rand.nextDouble() * maxX;
			double y = rand.nextDouble() * maxY;
			foods.put(new Position(x, y), new Food(1,1));
		}
		
		return foods;
	}

}
