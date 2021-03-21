package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.jfree.chart.ChartPanel;

import gui.drawers.EntityDrawer;
import gui.drawers.LiveLineChart;
import gui.panels.DataPanel;
import gui.panels.WorldPanel;
import interfaces.ISimulation;
import interfaces.ITracker;
import models.entities.Entity;
import models.world.Food;
import models.world.Position;
import models.world.World;
import systems.PopulationTracker;
import systems.Simulator;
import systems.WorldSimulation;

public class Main {

	static int worldWidth = 300;
	static int worldHeight = 300;
	static int windowWidth = 1280;
	static int windowHeight = 800;

	public static void main(String[] args) {
		final JFrame frame = new JFrame("World view");
		frame.setSize(windowWidth, windowHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// TODO: also get live bar chart for death causes
		DataPanel mainPanel = new DataPanel(setupSimulation());
		mainPanel.setup(windowWidth, 500);

		frame.add(mainPanel);

		frame.setVisible(true);
	}

	private static WorldSimulation setupSimulation() {
		WorldSimulation sim = WorldSimulation.defaultSimulation(worldWidth, worldHeight);

		sim.addTracker(new PopulationTracker("Population"));
		
		return sim;
	}

}
