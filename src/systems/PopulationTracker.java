package systems;

import java.util.ArrayList;
import java.util.List;

import interfaces.IDataListener;
import interfaces.ISimulation;
import interfaces.ITracker;

public class PopulationTracker implements ITracker {
	private static int trackerId = 1;
	private List<IDataListener> dataListeners;
	private String trackerName;

	public PopulationTracker() {
		this("Population Tracker " + trackerId);
	}
	
	public PopulationTracker(String trackerName) {
		this.trackerName = trackerName;
		this.dataListeners = new ArrayList<IDataListener>();
		trackerId++;		
	}

	@Override
	public void track(ISimulation sim) {
		if (sim.getClass() != PopulationSimulation.class)
			return;

		int currentPop = ((PopulationSimulation) sim).getCurrentPopulation();

		for (IDataListener listener : dataListeners) {
			if (listener != null) {
				listener.dataReceived(currentPop, getTrackerName());
			}
		}
	}

	@Override
	public void reset() {
//		System.out.println("Reset " + getTrackerName());
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
