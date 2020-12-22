package systems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.Entity;

public class Simulation implements ISimulation {
	private List<Entity> entities;
	private List<ITracker> trackers;
	private double baseSpawnChance;
	private Random rand;

	public Simulation(int initialNumEntities, double baseSpawnChance) {
		this.baseSpawnChance = baseSpawnChance;
		this.entities = new ArrayList<Entity>();
		this.trackers = new ArrayList<ITracker>();
		
		for (int i = 0; i < initialNumEntities; ++i) {
			entities.add(new Entity(.1, .05));
		}
		
		rand = new Random(System.currentTimeMillis());
	}
	
	public double getBaseSpawnChance() { return baseSpawnChance; }
	public int getCurrentPopulation() { return entities.size(); }
	
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
}
