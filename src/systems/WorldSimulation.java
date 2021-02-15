package systems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import interfaces.ISimulation;
import interfaces.ITracker;
import models.entities.Entity;
import models.entities.HungerOnlyEntity;
import models.genes.AwarenessGene;
import models.genes.SpeedGene;
import models.world.Position;
import models.world.World;

public class WorldSimulation implements ISimulation {
	private List<ITracker> trackers;
	private World world;
	private double foodSpawnChance;
	private int maxFood;
	
	public WorldSimulation(World world) {
		this.world = world;
		this.foodSpawnChance = .02;
		this.maxFood = 100;
		this.trackers = new ArrayList<ITracker>();
	}
	
	public WorldSimulation(World world, double foodSpawnChance, int maxFood) {
		this(world);
		this.foodSpawnChance = foodSpawnChance;
		this.maxFood = maxFood;
	}
	
	public World getWorld() {
		return world;
	}
	
	@Override
	public void tick() {
		world.worldTick(foodSpawnChance, maxFood);
		
		for (ITracker tracker : trackers) {
			tracker.track(this);
		}
	}

	@Override
	public void reset() {
		System.out.println("Reset world simulation not implemented");
	}

	@Override
	public void addTracker(ITracker tracker) {
		trackers.add(tracker);
	}
	
	public static WorldSimulation defaultSimulation(int width, int height) {
		World world = new World(width, height);
		Random rand = new Random(System.currentTimeMillis());
		
		ArrayList<Entity> entities = new ArrayList<Entity>();
		for (int i = 0; i < 10; ++i) {
			Entity entity = new HungerOnlyEntity();
			
			double x = rand.nextDouble() * (double)width;
			double y = rand.nextDouble() * (double)height;			
			entity.setPosition(new Position(x, y));
			
			entity.addGene(new SpeedGene(0, .6, 0.01));
			entity.addGene(new AwarenessGene(0, 10, 0.001));
			
			entities.add(entity);
		}
		
		world.setEntities(entities);
		
		return new WorldSimulation(world);		
	}
}
