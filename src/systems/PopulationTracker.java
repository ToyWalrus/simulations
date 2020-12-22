package systems;

import java.util.ArrayList;
import java.util.List;

import interfaces.IDataListener;
import interfaces.ISimulation;
import interfaces.ITracker;

public class PopulationTracker implements ITracker {
	private static int trackerId = 1;
	private List<IDataListener> dataListeners;
	private int trackCalls = 0;
	private double popAverage = 0;
	private int popSum = 0;
	private String trackerName;

	public PopulationTracker() {
		dataListeners = new ArrayList<IDataListener>();
		trackerName = "Population Tracker " + trackerId;
		trackerId++;
	}
	
	@Override
	public void track(ISimulation sim) {
		if (sim.getClass() != PopulationSimulation.class) return;
		
		trackCalls++;
		
		int currentPop = ((PopulationSimulation)sim).getCurrentPopulation();
		popSum += currentPop;
		popAverage = (double) popSum / (double) trackCalls;
		
		for (IDataListener listener : dataListeners) {
			if (listener != null) {			
				listener.dataReceived(currentPop, getTrackerName());
			}
		}
	}

	@Override
	public double getAverage() {
		return popAverage;
	}	
	
	@Override
	public String getTrackerName() {
		return trackerName;
	}

	@Override
	public void registerDataListener(IDataListener listener) {
		dataListeners.add(listener);		
	}

	@Override
	public void removeDataListener(IDataListener listener) {
		dataListeners.remove(listener);
	}
}
