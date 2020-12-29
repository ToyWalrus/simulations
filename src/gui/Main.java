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
import models.entities.SimpleEntity;

public class Main {

	public static void main(String[] args) {
		int tickLength = Math.round(1000 / (float) 60);
//		int initialEntityCountLowerBound = 20;
//		int initialEntityCountUpperBound = 200;

		List<ISimulation> simulations = new ArrayList<ISimulation>();
		List<ITracker> trackers = new ArrayList<ITracker>();

		PopulationSimulation popSim1 = new PopulationSimulation(1, new SimpleEntity(0, .05));
		PopulationTracker tracker1 = new PopulationTracker("Spawn = 1");
		popSim1.addTracker(tracker1);
		
		PopulationSimulation popSim2 = new PopulationSimulation(.5, new SimpleEntity(0, .05));
		PopulationTracker tracker2 = new PopulationTracker("Spawn = 0.5");
		popSim2.addTracker(tracker2);

		simulations.add(popSim1);
		simulations.add(popSim2);
		
		trackers.add(tracker1);
		trackers.add(tracker2);

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

		JPanel panel = initActionButtons(simulator, lineChart, iterations);
		frame.add(panel, BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private static JPanel initActionButtons(final Simulator simulator, final LiveLineChart lineChart, final int iterations) {
		final JButton start = new JButton("Start");		
		final JButton pause = new JButton("Pause");
		final JButton resume = new JButton("Resume");
		final JButton reset = new JButton("Reset");
		
		resume.setEnabled(false);
		pause.setEnabled(false);
		
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulator.startSimulation(iterations);
				pause.setEnabled(true);
				resume.setEnabled(false);
				start.setEnabled(false);
			}
		});
		
		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulator.pauseSimulation();
				pause.setEnabled(false);
				resume.setEnabled(true);
			}
		});
		
		resume.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				simulator.resumeSimulation();
				pause.setEnabled(true);
				resume.setEnabled(false);
			}
		});

		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulator.resetSimulation();
				lineChart.resetChart();
				start.setEnabled(true);
				pause.setEnabled(false);
				resume.setEnabled(false);
			}
		});

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(start);
		panel.add(pause);
		panel.add(resume);
		panel.add(reset);
		
		return panel;
	}

	private static List<ISimulation> getSimulations(int numSims, int initialEntityCountLowerBound,
			int initialEntityCountUpperBound, double baseSpawnChance) {
		List<ISimulation> sims = new ArrayList<ISimulation>();

		for (int i = 0; i < numSims; ++i) {
			PopulationSimulation sim = new PopulationSimulation(1, new SimpleEntity(.1, .05));
			sims.add(sim);
		}

		return sims;
	}
}
