package systems.trackers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import interfaces.IDataListener;
import interfaces.ISimulation;
import interfaces.ITracker;
import models.entities.CauseOfDeath;
import models.entities.Entity;
import models.world.World;
import systems.WorldSimulation;

public class CauseOfDeathTracker implements ITracker {
	private static int trackerId = 1;
	private List<IDataListener> dataListeners;
	private String trackerName;

	public CauseOfDeathTracker() {
		this("Cause of death " + trackerId);
	}

	public CauseOfDeathTracker(String name) {
		dataListeners = new ArrayList<>();
		trackerName = name;
		trackerId++;
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

	@Override
	public void track(ISimulation sim) {
		if (sim instanceof WorldSimulation) {
			World world = ((WorldSimulation) sim).getWorld();
			TreeMap<CauseOfDeath, Integer> deathCauses = new TreeMap<>();
			for (Entity e : world.getEntityDeathsSinceLastTick()) {
				CauseOfDeath cause = e.getCauseOfDeath();
				if (!deathCauses.containsKey(cause)) {
					deathCauses.put(cause, 0);
				}
				deathCauses.put(cause, deathCauses.get(cause) + 1);
			}

			for (Map.Entry<CauseOfDeath, Integer> entry : deathCauses.entrySet()) {
				for (IDataListener listener : dataListeners) {
					listener.dataReceived(entry.getValue(), entry.getKey().toString());
				}
			}

		}
	}

	@Override
	public void reset() {
	}

}
