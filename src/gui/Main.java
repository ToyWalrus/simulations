package gui;

import systems.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import interfaces.ISimulation;
import interfaces.ITracker;

public class Main {

	public static void main(String[] args) {
		final int iterations = 1000;
		int tickLength = Math.round(1000 / (float) 60);
		int initialEntityCountLowerBound = 20;
		int initialEntityCountUpperBound = 200;

		final Simulator simulator = new Simulator(
				getSimulations(4, initialEntityCountLowerBound, initialEntityCountUpperBound, 1), tickLength);
		List<ITracker> trackers = simulator.addTrackerTypeToSimulations(PopulationTracker.class);
		final LiveLineChart lineChart = new LiveLineChart("Population Over Time", "Generations", "Population",
				trackers);

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
			PopulationSimulation sim = new PopulationSimulation(initialEntityCountLowerBound,
					initialEntityCountUpperBound, baseSpawnChance);
			sims.add(sim);
		}

		return sims;
	}
}
