package systems.trackers;

import java.util.ArrayList;
import java.util.List;

import interfaces.IDataListener;
import interfaces.ISimulation;
import interfaces.ITracker;
import models.world.Food;
import models.world.World;
import systems.WorldSimulation;
import util.HelperFunctions;

public class AvailableFoodTracker implements ITracker {
	private static int trackerId = 1;
	private List<IDataListener> dataListeners;
	private String trackerName;
	private boolean onlyFullyGrown;

	public AvailableFoodTracker() {
		this("Available Food Tracker " + trackerId, false);
	}

	public AvailableFoodTracker(String trackerName, boolean onlyFullyGrown) {
		this.trackerName = trackerName;
		this.dataListeners = new ArrayList<IDataListener>();
		trackerId++;
		this.onlyFullyGrown = onlyFullyGrown;
	}

	@Override
	public void track(ISimulation sim) {
		if (sim instanceof WorldSimulation) {
			World world = ((WorldSimulation) sim).getWorld();
			int food;

			if (onlyFullyGrown) {
				food = (int) world.getFood().entrySet().stream().filter((posfood) -> {
					Food f = posfood.getValue();
					return HelperFunctions.almost(f.getNutrition(), f.getMaxPossibleNutrition(), f.getNutritionGainPerTick());
				}).count();
			} else {
				food = world.getFood().size();
			}

			for (IDataListener listener : dataListeners) {
				if (listener != null) {
					listener.dataReceived(food, getTrackerName());
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
