package systems;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

import interfaces.ISimulation;
import interfaces.ITracker;

public class Simulator implements ActionListener {
	private List<ISimulation> simulations;
	private Timer timer;
	private int totalIterations;
	private int notificationFrequency;
	private int currentIteration;

	public Simulator(List<ISimulation> simulations) {
		this.simulations = simulations;
		timer = new Timer(10, this);
	}

	public Simulator(List<ISimulation> simulations, int tickDelay) {
		this(simulations);
		timer = new Timer(Math.max(tickDelay, 1), this);
	}

	public List<ITracker> addTrackerTypeToAllSimulations(Class<? extends ITracker> trackerType) {
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
	 * 
	 * @param delay
	 */
	public void setTickDelay(int delay) {
		delay = Math.max(delay, 1);
		boolean running = false;
		if (timer.isRunning()) {
			timer.stop();
			running = true;
		}

		timer = new Timer(delay, this);

		if (running) {
			timer.start();
		}
	}

	/**
	 * Start the simulation.
	 */
	public void startSimulation() {
		startSimulation(-1);
	}

	/**
	 * Start the simulation.
	 * 
	 * @param numIterations Run for this many ticks.
	 */
	public void startSimulation(int numIterations) {
		startSimulation(numIterations, numIterations + 1);
	}

	/**
	 * Start the simulation
	 * 
	 * @param numIterations  Run for this many ticks.
	 * @param notifyInterval Print to console every X ticks.
	 */
	public void startSimulation(int numIterations, int notifyInterval) {
		totalIterations = numIterations;
		currentIteration = totalIterations;
		notificationFrequency = notifyInterval;
		timer.start();
	}

	public void resetSimulation() {
		if (timer.isRunning()) {
			timer.stop();
		}

		setTickDelay(timer.getDelay());

		for (ISimulation sim : simulations) {
			sim.reset();
		}
	}

	public void pauseSimulation() {
		timer.stop();
	}

	public void resumeSimulation() {
		if (timer != null) {
			timer.start();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (currentIteration <= 0 && totalIterations != -1) {
			timer.stop();
			return;
		}

		runSimTick();
		currentIteration--;
	}

	private void runSimTick() {
		for (ISimulation sim : simulations) {
			sim.tick();
		}

		if (currentIteration % notificationFrequency == 0) {
			System.out.println("Iteration #" + (totalIterations - currentIteration) + "...");
		}
	}
}
