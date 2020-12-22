package gui;

import systems.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

	public static void main(String[] args) {
		Simulator simulator = new Simulator(generateRandomSims(50));
		simulator.setTickDelay(0);

		List<ITracker> trackers = simulator.addTrackerTypeToSimulations(PopulationTracker.class);

		System.out.println();
		simulator.startSimulation(100000);
		System.out.println();

		for (int i = 0; i < trackers.size(); ++i) {
			System.out.println("Avg for sim " + (i + 1) + ": " + trackers.get(i).getAverage());
		}
	}

	private static List<ISimulation> generateRandomSims(int numSims) {
		List<ISimulation> sims = new ArrayList<ISimulation>();
		Random rand = new Random(System.currentTimeMillis());

		for (int i = 0; i < numSims; ++i) {
			Simulation sim = new Simulation(100 /*rand.nextInt(99) + 1*/, (i + 1) / (double) numSims);
			System.out.println(String.format("Creating sim # %1$3s | Pop: %2$3s | Base spawn rate: %3$3f", i + 1,
					sim.getCurrentPopulation(), sim.getBaseSpawnChance()));
			sims.add(sim);
		}

		return sims;
	}

}
