package gui;

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
		final int worldWidth = 250;
		final int worldHeight = 250;
		int windowWidth = 800;
		int windowHeight = 800;

		final JFrame frame = new JFrame("World view");
		frame.setSize(windowWidth, windowHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		WorldDrawer wd = new WorldDrawer(windowWidth - windowWidth / 10, windowHeight - windowHeight / 10);
		Simulator simulator = setupSimulator(wd, worldWidth, worldHeight);

		frame.add(wd);
		frame.setVisible(true);

		simulator.startSimulation();
	}

	private static Simulator setupSimulator(WorldDrawer worldDrawer, int worldWidth, int worldHeight) {
		WorldSimulation sim = WorldSimulation.defaultSimulation(worldWidth, worldHeight);
		worldDrawer.setWorld(sim.getWorld());
		List<ISimulation> sims = List.of(sim);

		Simulator simulator = new Simulator(sims, 30);

		return simulator;
	}
}
