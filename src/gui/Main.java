package gui;

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

		setupLayout(frame, 500);

		frame.setVisible(true);
	}

	private static Simulator setupSimulator(WorldPanel worldDrawer, int worldWidth, int worldHeight,
			List<ITracker> trackers) {
		WorldSimulation sim = WorldSimulation.defaultSimulation(worldWidth, worldHeight);
		for (ITracker tracker : trackers) {
			sim.addTracker(tracker);
		}
		worldDrawer.setWorld(sim.getWorld());
		List<ISimulation> sims = List.of(sim);

		Simulator simulator = new Simulator(sims, 30);

		return simulator;
	}

	private static void setupLayout(JFrame frame, int rowHeight) {
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));		
		
		int padding = 5;
		JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

		WorldPanel wp = new WorldPanel(rowHeight, rowHeight);
		wp.setBorder(BorderFactory.createLineBorder(Color.gray));
		PopulationTracker popTracker = new PopulationTracker("population");
		final Simulator simulator = setupSimulator(wp, worldWidth, worldHeight, List.of(popTracker));
		row1.add(wp);

		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
		dataPanel.setPreferredSize(new Dimension(200, rowHeight));
		dataPanel.setBorder(BorderFactory.createLineBorder(Color.gray));

		final JButton startSimButton = new JButton("Start");
		final JButton pauseSimButton = new JButton("Pause");
		final JButton resetSimButton = new JButton("Reset");
		pauseSimButton.setEnabled(false);		

		startSimButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (simulator.hasStarted()) {
					simulator.resumeSimulation();
				} else {					
					simulator.startSimulation();
				}
				pauseSimButton.setEnabled(true);				
				startSimButton.setEnabled(false);
			}
		});

		pauseSimButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulator.pauseSimulation();
				pauseSimButton.setEnabled(false);
				startSimButton.setEnabled(true);				
			}
		});
		
		resetSimButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				simulator.resetSimulation();
				startSimButton.setEnabled(true);
				pauseSimButton.setEnabled(false);
			}
		});

		JButton visualizeButton = new JButton("Visualize");
		visualizeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Clicked vis button");
			}
		});

		addButtonToDataPanel(dataPanel, startSimButton, padding);
		addButtonToDataPanel(dataPanel, pauseSimButton, padding);
		addButtonToDataPanel(dataPanel, resetSimButton, padding);
		addButtonToDataPanel(dataPanel, visualizeButton, padding);
		row1.add(dataPanel);

		LiveLineChart lineChart = new LiveLineChart("Data Visualization", "Time", "Population", List.of(popTracker));
		JPanel chartPanel = lineChart.getChart();
		chartPanel.setPreferredSize(new Dimension(rowHeight, rowHeight));
		row1.add(chartPanel);

		content.add(row1);
		
		Label slowColorText = new Label("Slowest:");		
		Label fastColorText = new Label("Fastest:");
		JPanel slowColor = new JPanel();
		slowColor.setPreferredSize(new Dimension(10, 10));
		slowColor.setBackground(EntityDrawer.LOW_SPEED);
		JPanel fastColor = new JPanel();
		fastColor.setPreferredSize(new Dimension(10, 10));
		fastColor.setBackground(EntityDrawer.HIGH_SPEED);
		row2.add(slowColorText);
		row2.add(slowColor);
		row2.add(Box.createRigidArea(new Dimension(20, 0)));
		row2.add(fastColorText);
		row2.add(fastColor);
		
		content.add(row2);
		
		frame.add(content);
	}

	private static void addButtonToDataPanel(JPanel panel, JButton button, int vgap) {
		Dimension buttonSize = new Dimension(120, 30);
		button.setMinimumSize(buttonSize);
		button.setPreferredSize(buttonSize);
		button.setMaximumSize(buttonSize);
		panel.add(button);
		panel.add(Box.createRigidArea(new Dimension(0, vgap)));
	}

}
