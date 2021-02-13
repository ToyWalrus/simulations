package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import interfaces.ISimulation;
import models.entities.Entity;
import models.world.Food;
import models.world.Position;
import models.world.World;
import systems.Simulator;
import systems.WorldSimulation;

public class Main {

	public static void main(String[] args) {
		final int worldWidth = 100;
		final int worldHeight = 100;
		int windowWidth = 1000;
		int windowHeight = 750;

		final JFrame frame = new JFrame("World view");
		frame.setSize(windowWidth, windowHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		WorldDrawer wd = new WorldDrawer(windowWidth - 50, windowHeight - 50);
		Simulator simulator = setupSimulator(wd, worldWidth, worldHeight);
		
		frame.add(wd);
		

//		JPanel panel = new JPanel(new BorderLayout());
//		Button button = new Button("Update");		
//		button.setSize(100, 30);
//		button.addActionListener(new ActionListener() {			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				w.setFood(generateRandomFood(100, worldWidth, worldHeight));
//				frame.repaint();
//			}
//		});
//		panel.add(button, BorderLayout.LINE_END);
//		panel.setVisible(true);
//		
//		frame.add(panel);

		frame.setVisible(true);
		
		simulator.startSimulation(1000);
	}

	private static Simulator setupSimulator(WorldDrawer worldDrawer, int worldWidth, int worldHeight) {
		WorldSimulation sim = WorldSimulation.defaultSimulation(worldWidth, worldHeight);
		worldDrawer.setWorld(sim.getWorld());

		List<ISimulation> sims = new ArrayList<ISimulation>();
		sims.add(sim);

		Simulator simulator = new Simulator(sims, 50);

		return simulator;
	}

	private static HashMap<Position, Food> generateRandomFood(int count, int maxX, int maxY) {
		HashMap<Position, Food> foods = new HashMap<Position, Food>();
		Random rand = new Random(System.currentTimeMillis());

		for (int i = 0; i < count; ++i) {
			double x = rand.nextDouble() * maxX;
			double y = rand.nextDouble() * maxY;
			foods.put(new Position(x, y), new Food(1, 1));
		}

		return foods;
	}
}
