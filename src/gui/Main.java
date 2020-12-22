package gui;

import systems.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

import interfaces.ISimulation;
import interfaces.ITracker;

public class Main {

	public static void main(String[] args) {
		final int iterations = 20;
		final Simulator simulator = new Simulator(generateSameSims(3, 100, .9));
		simulator.setTickDelay(500);
		List<ITracker> trackers = simulator.addTrackerTypeToSimulations(PopulationTracker.class);
		final LiveLineChart lineChart = new LiveLineChart(trackers, iterations);
		
		JFrame frame = new JFrame("Simulation");
		frame.add(lineChart.getChart(), BorderLayout.CENTER);
		
		JButton start = new JButton("Start Simulation");
		start.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				lineChart.startRecording();
				simulator.startSimulation(iterations);
			}
		});
		frame.add(start, BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private static List<ISimulation> generateRandomSims(int numSims) {
		List<ISimulation> sims = new ArrayList<ISimulation>();

		for (int i = 0; i < numSims; ++i) {
			PopulationSimulation sim = new PopulationSimulation(100, (i + 1) / (double) numSims);
			System.out.println(String.format("Creating sim # %1$3s | Pop: %2$3s | Base spawn rate: %3$3f", i + 1,
					sim.getCurrentPopulation(), sim.getBaseSpawnChance()));
			sims.add(sim);
		}

		return sims;
	}
	
	private static List<ISimulation> generateSameSims(int numSims, int initialEntityCount, double baseSpawnChance) {
		List<ISimulation> sims = new ArrayList<ISimulation>();

		for (int i = 0; i < numSims; ++i) {
			PopulationSimulation sim = new PopulationSimulation(initialEntityCount, baseSpawnChance);
			sims.add(sim);
		}

		return sims;		
	}
}
