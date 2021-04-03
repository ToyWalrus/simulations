package main;

import javax.swing.JFrame;
import gui.panels.DataPanel;
import systems.WorldSimulation;
import systems.trackers.*;

public class Main {

	static int worldWidth = 300;
	static int worldHeight = 300;
	static int windowWidth = 1280;
	static int windowHeight = 800;

	public static void main(String[] args) {
		final JFrame frame = new JFrame("World view");
		frame.setSize(windowWidth, windowHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		DataPanel mainPanel = new DataPanel(setupSimulation());
		mainPanel.setup(windowWidth, 500);

		// TODO: create a GUI for creating a simulation
		
		frame.add(mainPanel);

		frame.setVisible(true);
	}

	private static WorldSimulation setupSimulation() {
		WorldSimulation sim = WorldSimulation.defaultSimulation(worldWidth, worldHeight);

		sim.addTracker(new PopulationTracker("Population"));
		sim.addTracker(new AverageSpeedTracker("Average Speed"));
		sim.addTracker(new AvailableFoodTracker("Available Food", false));
		sim.addTracker(new AvailableFoodTracker("Fully Grown Food", true));
		sim.addTracker(new CauseOfDeathTracker("Cause of Death"));
		
		return sim;
	}

}
