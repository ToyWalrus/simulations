package models.world;

import com.google.common.collect.HashBiMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import models.entities.Entity;
import util.HelperFunctions;

public class World {
	private HashMap<Position, Food> food;
	private HashBiMap<Position, Entity> entities;
	private int worldWidth;

	private int worldHeight;
	private Random rand;

	public World(HashMap<Position, Food> foodLocations, Map<Position, Entity> entities, int width, int height) {
		this.food = foodLocations;
		this.entities = HashBiMap.create(entities);
		worldWidth = width;
		worldHeight = height;
		rand = new Random(System.currentTimeMillis());
	}

	public World(int width, int height) {
		this(new HashMap<Position, Food>(), new HashMap<Position, Entity>(), width, height);
	}
	
	public Map<Position, Food> getFood() {
		return food;
	}

	public Set<Entity> getEntities() {
		return entities.values();
	}
	
	public void worldTick(double foodSpawnChance) {
		worldTick(foodSpawnChance, Integer.MAX_VALUE);
	}
	
	public Position getEntityPosition(Entity e) {
		return entities.inverse().get(e);		
	}
	
	public void moveEntity(Entity e, double dx, double dy) {
		Position origin = getEntityPosition(e);
		if (origin == null) return;
		
		double x = HelperFunctions.clamp(origin.x + dx, 0, worldWidth);
		double y = HelperFunctions.clamp(origin.y + dy, 0, worldHeight);
		Position newPos = new Position(x, y);
		
		if (!entities.remove(origin, e)) {
			System.err.println("Tried removing an entity from a place where it wasn't!");
		}
		
		while (entities.containsKey(newPos)) {
			newPos = new Position(newPos.x + 0.1, newPos.y + 0.1);
		}	
		entities.put(newPos, e);
	}
	
	public void worldTick(double foodSpawnChance, int maxFoodAllowed) {
		if (foodSpawnChance <= 0) return;		
		
		for (int x = 0; x < worldWidth; ++x) {
			for (int y = 0; y < worldHeight; ++y) {
				if (rand.nextDouble() < foodSpawnChance) {
					food.put(new Position(x, y), new Food(100, 1));
				}
				
				if (food.size() >= maxFoodAllowed) {
					return;
				}
			}
		}
	}
	
	public void removeFood(Food foodToRemove) {
		for (Position p : food.keySet()) {
			if (food.get(p) == foodToRemove) {
				food.remove(p);
				return;
			}
		}
	}

	public List<Food> getFoodInRadius(Position position, double radius) {
		List<Position> positionsInRadius = getPositionsInRadius(position, radius, food.keySet());		
		List<Food> foods = new ArrayList<Food>();
		
		for (Position p : positionsInRadius) {
			foods.add(food.get(p));
		}
		
		return foods;
	}
	
	public List<Entity> getEntitiesInRadius(Position position, double radius) {
		List<Position> positionsInRadius = getPositionsInRadius(position, radius, entities.keySet());
		List<Entity> entitiesInRadius = new ArrayList<Entity>();
		
		for (Position p : positionsInRadius) {
			entitiesInRadius.add(entities.get(p));
		}
		
		return entitiesInRadius;
	}
	
	public int getWorldWidth() {
		return worldWidth;
	}

	public int getWorldHeight() {
		return worldHeight;
	}
	
	private List<Position> getPositionsInRadius(Position position, double radius, Set<Position> keySet) {
		List<Position> positionsInRadius = new ArrayList<Position>();
		for (Position p : keySet) {
			if (HelperFunctions.distance(p, position) <= radius) {
				positionsInRadius.add(p);
			}
		}
		return positionsInRadius;
	}
}
