package systems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import interfaces.ISimulation;
import interfaces.ITracker;
import models.Entity;

public class PopulationSimulation implements ISimulation {
	private List<Entity> entities;
	private List<ITracker> trackers;
	private double baseSpawnChance;
	private Random rand;
	private int initialEntityCountLowerBound = 20;
	private int initialEntityCountUpperBound = 200;

	public PopulationSimulation(int initialLowerBound, int initialUpperBound, double baseSpawnChance) {
		this.trackers = new ArrayList<ITracker>();
		this.baseSpawnChance = baseSpawnChance;
		this.initialEntityCountLowerBound = initialLowerBound;
		this.initialEntityCountUpperBound = initialUpperBound;
		initSim();
	}

	private void initSim() {
		rand = new Random(System.currentTimeMillis() * new Random().nextLong());
		this.entities = new ArrayList<Entity>();
		
		int initialNumEntities = (rand.nextInt(initialEntityCountUpperBound - initialEntityCountLowerBound))
				+ initialEntityCountLowerBound;
		
		for (int i = 0; i < initialNumEntities; ++i) {
			entities.add(new Entity(.1, .05));
		}
	}

	public double getBaseSpawnChance() {
		return baseSpawnChance;
	}

	public int getCurrentPopulation() {
		return entities.size();
	}

	@Override
	public void addTracker(ITracker tracker) {
		trackers.add(tracker);
	}

	@Override
	public void tick() {
		DoBaseSpawn();
		CheckForEntityDeath();
		InformTrackers();
	}

	private void DoBaseSpawn() {
		if (rand.nextDouble() <= baseSpawnChance) {
			entities.add(new Entity(.1, .05));
		}
	}

	private void CheckForEntityDeath() {
		List<Entity> died = new ArrayList<Entity>();
		for (Entity entity : entities) {
			if (rand.nextDouble() <= entity.deathChance) {
				entity.die();
				died.add(entity);
			}
		}

		entities.removeAll(died);
	}

	private void InformTrackers() {
		for (ITracker tracker : trackers) {
			tracker.track(this);
		}
	}

	@Override
	public void reset() {
		for (ITracker tracker : trackers) {
			tracker.reset();
		}
		initSim();
	}
}
