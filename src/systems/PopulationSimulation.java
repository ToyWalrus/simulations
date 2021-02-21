package systems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import interfaces.ISimulation;
import interfaces.ITracker;
import models.entities.Entity;
import models.entities.PopulationTestEntity;

public class PopulationSimulation implements ISimulation {
	private List<ITracker> trackers;
	private List<PopulationTestEntity> entities;
	private List<PopulationTestEntity> initialPopulation;
	private Entity baseEntity;
	private double baseSpawnChance;
	private Random rand;

	public PopulationSimulation(List<PopulationTestEntity> basePopulation, double baseSpawnChance, Entity baseEntity) {
		this.initialPopulation = basePopulation;
		this.baseSpawnChance = baseSpawnChance;
		this.baseEntity = baseEntity;
		this.trackers = new ArrayList<ITracker>();
		this.entities = new ArrayList<PopulationTestEntity>(basePopulation);
		
		rand = new Random(System.currentTimeMillis() * (new Random().nextLong()));
	}
	
	public PopulationSimulation(List<PopulationTestEntity> basePopulation) {
		this(basePopulation, 0, null);
	}
	
	public PopulationSimulation(double baseSpawnChance, PopulationTestEntity baseEntity) {
		this(new ArrayList<PopulationTestEntity>(), baseSpawnChance, baseEntity);
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
		doBaseSpawn();
		doEntityTick();
		informTrackers();
	}

	private void doBaseSpawn() {
		if (baseSpawnChance > 0 && rand.nextDouble() <= baseSpawnChance) {
			entities.add((PopulationTestEntity) baseEntity.replicatePerfectly());
		}
	}
	
	private void doEntityTick() {
		List<PopulationTestEntity> died = new ArrayList<PopulationTestEntity>();	
		List<PopulationTestEntity> born = new ArrayList<PopulationTestEntity>();
		
		for (PopulationTestEntity entity : entities) {
			if (rand.nextDouble() < entity.getReplicationChance()) {
				born.add((PopulationTestEntity) entity.reproduce(null));
			}
			if (rand.nextDouble() < entity.getDeathChance()) {
				entity.die();
				died.add(entity);
			}
		}		
		
		entities.removeAll(died);
		entities.addAll(born);
	}

	private void informTrackers() {
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
		entities = new ArrayList<PopulationTestEntity>(initialPopulation); 
	}
}
