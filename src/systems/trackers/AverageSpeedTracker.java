package systems.trackers;

import java.util.ArrayList;
import java.util.List;

import interfaces.IDataListener;
import interfaces.ISimulation;
import interfaces.ITracker;
import models.entities.Entity;
import models.genes.SpeedGene;
import models.world.World;
import systems.WorldSimulation;

public class AverageSpeedTracker implements ITracker {
	private static int trackerId = 1;
	private List<IDataListener> dataListeners;
	private String trackerName;

	public AverageSpeedTracker() {
		this("Average Speed Tracker " + trackerId);
	}
	
	public AverageSpeedTracker(String trackerName) {
		this.trackerName = trackerName;
		this.dataListeners = new ArrayList<IDataListener>();
		trackerId++;		
	}

	@Override
	public void track(ISimulation sim) {
		double avg = 0;
		if (sim instanceof WorldSimulation) {
			World world = ((WorldSimulation)sim).getWorld();

			for (Entity entity : world.getEntities()) {
				avg += entity.getGene(SpeedGene.name).getValue();
			}
			
			int count = world.getEntities().size();
			if (count > 0) {				
				avg /= count;
			} else {
				avg = 0;
			}
			
			for (IDataListener listener : dataListeners) {
				if (listener != null) {
					listener.dataReceived(avg, getTrackerName());
				}
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
