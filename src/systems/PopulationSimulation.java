package systems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import interfaces.ISimulation;
import interfaces.ITracker;
import models.entities.Entity;

public class PopulationSimulation implements ISimulation {
	private List<ITracker> trackers;
	private List<Entity> entities;
	private List<Entity> initialPopulation;
	private Entity baseEntity;
	private double baseSpawnChance;
	private Random rand;

	public PopulationSimulation(List<Entity> basePopulation, double baseSpawnChance, Entity baseEntity) {
		this.initialPopulation = basePopulation;
		this.baseSpawnChance = baseSpawnChance;
		this.baseEntity = baseEntity;
		this.trackers = new ArrayList<ITracker>();
		this.entities = new ArrayList<Entity>(basePopulation);
		
		rand = new Random(System.currentTimeMillis() * (new Random().nextLong()));
	}
	
	public PopulationSimulation(List<Entity> basePopulation) {
		this(basePopulation, 0, null);
	}
	
	public PopulationSimulation(double baseSpawnChance, Entity baseEntity) {
		this(new ArrayList<Entity>(), baseSpawnChance, baseEntity);
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
		DoEntityTick();
		InformTrackers();
	}

	private void DoBaseSpawn() {
		if (baseSpawnChance > 0 && rand.nextDouble() <= baseSpawnChance) {
			entities.add(baseEntity.replicatePerfectly());
		}
	}
	
	private void DoEntityTick() {
		if (entities.size() == 0) return;
		
		List<Entity> died = new ArrayList<Entity>();	
		List<Entity> born = new ArrayList<Entity>();
		for (Entity entity : entities) {
			if (rand.nextDouble() < entity.getReplicationChance()) {
				born.add(entity.replicate());
			}
			if (rand.nextDouble() < entity.getDeathChance()) {
				entity.die();
				died.add(entity);
			}
		}		
		
		entities.removeAll(died);
		entities.addAll(born);
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
		entities.clear();
		entities = new ArrayList<Entity>(initialPopulation); 
	}
}
