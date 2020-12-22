package systems;

import java.util.ArrayList;
import java.util.List;

public class Simulator {
	private List<ISimulation> simulations;
	private long tickDelay = 0;

	public Simulator(int numSimulations) {
		simulations = new ArrayList<ISimulation>();

		for (int i = 0; i < numSimulations; ++i) {
			Simulation sim = new Simulation(50, 1);
			simulations.add(sim);
		}
	}
	
	public Simulator(List<ISimulation> simulations) {
		this.simulations = simulations;
	}

	public List<ITracker> addTrackerTypeToSimulations(Class<? extends ITracker> trackerType) {
		List<ITracker> trackers = new ArrayList<ITracker>();
		for (ISimulation sim : simulations) {
			ITracker tracker = null;
			try {
				tracker = trackerType.getConstructor().newInstance();
				trackers.add(tracker);
				sim.addTracker(tracker);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return trackers;
	}
	
	/**
	 * Set the delay between ticks, in milliseconds.
	 * @param delay
	 */
	public void setTickDelay(long delay) { tickDelay = delay; }

	public void startSimulation(int numIterations) {
		startSimulation(numIterations, numIterations + 1);
	}

	public void startSimulation(int numIterations, int notifyInterval) {
		int notifyCounter = 0;
		System.out.println("Starting simulations with " + numIterations + " iterations");
		try {
			for (int i = 0; i < numIterations; ++i) {
				long timeStart = System.currentTimeMillis();
				for (ISimulation sim : simulations) {
					sim.tick();
				}
				long timeToExecute = System.currentTimeMillis() - timeStart;

				notifyCounter++;
				if (notifyCounter == notifyInterval) {
					System.out.println("Iteration #" + i + "...");
					notifyCounter = 0;
				}
				Thread.sleep(Math.max(tickDelay - timeToExecute, 0));
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.err.println("Simulation exited early!");
			e.printStackTrace();
			return;
		}
		System.out.println("Done!");
	}
}
