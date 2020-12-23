package gui;

import systems.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import interfaces.ISimulation;
import interfaces.ITracker;
import models.entities.BlueEntity;

public class Main {

	public static void main(String[] args) {
		int tickLength = Math.round(1000 / (float) 60);
//		int initialEntityCountLowerBound = 20;
//		int initialEntityCountUpperBound = 200;

		List<ISimulation> simulations = new ArrayList<ISimulation>();
		List<ITracker> trackers = new ArrayList<ITracker>();

		PopulationSimulation popSim1 = new PopulationSimulation(1, new BlueEntity(.05, .05));
		PopulationTracker tracker1 = new PopulationTracker("Blue Entity");
		popSim1.addTracker(tracker1);

		simulations.add(popSim1);
		trackers.add(tracker1);

		Simulator simulator = new Simulator(simulations, tickLength);
//		List<ITracker> trackers = simulator.addTrackerTypeToAllSimulations(PopulationTracker.class);

		LiveLineChart lineChart = new LiveLineChart("Population Over Time", "Generations", "Population", trackers);
		lineChart.setLineColor(tracker1.getTrackerName(), Color.BLUE);

		initGUI(simulator, lineChart);
	}

	private static void initGUI(final Simulator simulator, final LiveLineChart lineChart) {
		final int iterations = 1000;

		JFrame frame = new JFrame("Simulation");
		frame.add(lineChart.getChart(), BorderLayout.CENTER);

		JButton start = new JButton("Start Simulation");
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulator.startSimulation(iterations);
			}
		});

		JButton reset = new JButton("Reset Simulation");
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulator.resetSimulation();
				lineChart.resetChart();
			}
		});

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(start);
		panel.add(reset);

		frame.add(panel, BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private static List<ISimulation> getSimulations(int numSims, int initialEntityCountLowerBound,
			int initialEntityCountUpperBound, double baseSpawnChance) {
		List<ISimulation> sims = new ArrayList<ISimulation>();

		for (int i = 0; i < numSims; ++i) {
			PopulationSimulation sim = new PopulationSimulation(1, new BlueEntity(.1, .05));
			sims.add(sim);
		}

		return sims;
	}
}
