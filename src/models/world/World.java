package models.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import interfaces.IObservable;
import interfaces.IObserver;
import models.entities.Entity;
import util.HelperFunctions;
import util.Pair;

public class World implements IObservable {
	private HashMap<Position, Food> food;
	private List<Entity> entities;
	private int worldWidth;
	private int worldHeight;
	private Random rand;
	private FoodFactory foodFactory;
	private List<Entity> deadEntitiesLastTick;

	private Set<IObserver<World>> observers;

	public World(HashMap<Position, Food> foodLocations, List<Entity> entities, int width, int height) {
		this.food = foodLocations;
		this.entities = entities;
		worldWidth = width;
		worldHeight = height;
		deadEntitiesLastTick = new ArrayList<Entity>();
		rand = new Random(System.currentTimeMillis());
		observers = new HashSet<IObserver<World>>();
		foodFactory = new FoodFactory(Food.createFullyGrown(100, 1));
	}

	public World(int width, int height) {
		this(new HashMap<Position, Food>(), new ArrayList<Entity>(), width, height);
	}

	public void setFoodFactory(FoodFactory factory) {
		this.foodFactory = factory;
	}

	public void setFood(HashMap<Position, Food> foods) {
		this.food = foods;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	public Map<Position, Food> getFood() {
		return food;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public List<Entity> getEntityDeathsSinceLastTick() {
		return deadEntitiesLastTick;
	}

	public void worldTick(double foodSpawnChance) {
		worldTick(foodSpawnChance, 200);
	}

	public void worldTick(double foodSpawnChance, int maxFoodAllowed) {
		deadEntitiesLastTick.clear();
		List<Entity> currentEntities = new ArrayList<Entity>(entities);

		for (Entity e : currentEntities) {
			e.tick(this);
			if (e.isDead()) {
				entities.remove(e);
				deadEntitiesLastTick.add(e);
			}
		}

		spawnFood(foodSpawnChance, maxFoodAllowed, 5);
		foodGrowthTick();

		updateObservers();
	}

	// This will spawn food up to the max amount allowed or
	// maxFoodSpawnedPerTick if the foodSpawnChance passes during this tick
	private void spawnFood(double foodSpawnChance, int maxFoodAllowed, int maxFoodSpawnedPerTick) {
		if (foodSpawnChance <= 0 || foodSpawnChance > rand.nextDouble())
			return;

		int foodSpawned = 0;

		while (food.size() < maxFoodAllowed && foodSpawned <= maxFoodSpawnedPerTick) {
			double x = Math.round(rand.nextDouble() * worldWidth);
			double y = Math.round(rand.nextDouble() * worldHeight);
			boolean foodWasSpawned = food.putIfAbsent(new Position(x, y), foodFactory.create()) == null;
			if (foodWasSpawned) {
				foodSpawned++;
			}
		}
	}

	private void foodGrowthTick() {
		for (Food f : food.values()) {
			f.growthTick();
		}
	}

	public void removeConsumable(IConsumable consumable) {
		for (Position p : food.keySet()) {
			if (food.get(p).equals(consumable)) {
				food.remove(p);
				return;
			}
		}
		for (Entity e : entities) {
			if (consumable.equals(consumable)) {
				entities.remove(e);
				return;
			}
		}
	}

	/**
	 * Gets the foods in the world inside the given radius, centered at position
	 * 
	 * @param position
	 * @param radius
	 * @return A list of IConsumables, ordered by distance (closest first)
	 */	
	public List<Pair<Position, IConsumable>> getFoodInRadius(Position position, double radius){
		return getFoodInRadius(position, radius, new ArrayList<IConsumable>());
	}
	
	/**
	 * Gets the foods in the world inside the given radius, centered at position
	 * 
	 * @param position
	 * @param radius
	 * @param exclude A list of IConsumables to ignore from search
	 * @return A list of IConsumables, ordered by distance (closest first)
	 */
	public List<Pair<Position, IConsumable>> getFoodInRadius(Position position, double radius,
			List<IConsumable> exclude) {
		Map<Position, IConsumable> consumableEntities = entities.stream().filter(e -> !exclude.contains(e))
				.collect(Collectors.toMap(Entity::getPosition, e -> e, (existing, replacement) -> existing));
		
		Set<Position> consumablePositions = consumableEntities.keySet();
		consumablePositions
				.addAll(food.keySet().stream().filter(e -> !exclude.contains(food.get(e))).collect(Collectors.toSet()));

		List<Position> positionsInRadius = getPositionsInRadius(position, radius, consumablePositions);
		List<Pair<Position, IConsumable>> consumables = new ArrayList<Pair<Position, IConsumable>>();

		for (Position p : positionsInRadius) {
			if (food.containsKey(p)) {
				consumables.add(new Pair<Position, IConsumable>(p, food.get(p)));
			} else {
				consumables.add(new Pair<Position, IConsumable>(p, consumableEntities.get(p)));
			}
		}

		return consumables;
	}

	public List<Entity> getEntitiesInRadius(Position position, double radius) {
		return entities.stream().filter(e -> isInCircle(position, radius, e.getPosition()))
				.collect(Collectors.toList());
	}

	public List<Entity> getEntitiesInRadius(Position position, double radius, List<Entity> exclude) {
		return getEntitiesInRadius(position, radius).stream().filter(e -> !exclude.contains(e))
				.collect(Collectors.toList());
	}

	public int getWorldWidth() {
		return worldWidth;
	}

	public int getWorldHeight() {
		return worldHeight;
	}

	/**
	 * Returns all of the positions within keySet that are in the radius centered at
	 * origin
	 * 
	 * @param origin
	 * @param radius
	 * @param keySet
	 * @return A sorted list of positions (closest first)
	 */
	private List<Position> getPositionsInRadius(Position origin, double radius, Iterable<Position> keySet) {
		List<Position> positionsInRadius = new ArrayList<Position>();
		for (Position p : keySet) {
			if (isInCircle(origin, radius, p)) {
				positionsInRadius.add(p);
			}
		}
		return positionsInRadius.stream()
				.sorted((a, b) -> HelperFunctions.distance(origin, a) < HelperFunctions.distance(origin, b) ? -1 : 1)
				.collect(Collectors.toList());
	}

	private boolean isInCircle(Position origin, double radius, Position position) {
		return HelperFunctions.distance(origin, position) <= radius;
	}

	@Override
	public void updateObservers() {
		for (IObserver<World> observer : observers) {
			observer.update(this);
		}
	}

	@Override
	public void registerObserver(IObserver<?> observer) {
		observers.add((IObserver<World>) observer);
	}

	@Override
	public void deRegisterObserver(IObserver<?> observer) {
		observers.remove((IObserver<World>) observer);
	}
}
