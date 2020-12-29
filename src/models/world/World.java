package models.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import models.entities.Entity;

public class World {
	private HashMap<Position, Food> food;
	private HashMap<Position, Entity> entities;
	private int worldWidth;
	private int worldHeight;
	private Random rand;

	public World(HashMap<Position, Food> foodLocations, HashMap<Position, Entity> entities, int width, int height) {
		this.food = foodLocations;
		this.entities = entities;
		worldWidth = width;
		worldHeight = height;
		rand = new Random(System.currentTimeMillis());
	}

	public World(int width, int height) {
		this(new HashMap<Position, Food>(), new HashMap<Position, Entity>(), width, height);
	}
	
	public HashMap<Position, Food> getFood() {
		return food;
	}

	public HashMap<Position, Entity> getEntities() {
		return entities;
	}
	
	public void worldTick(double foodSpawnChance) {
		worldTick(foodSpawnChance, Integer.MAX_VALUE);
	}
	
	public Position getEntityPosition(Entity e) {
		for (Position p : entities.keySet()) {
			if (entities.get(p) == e) return p;
		}
		return null;
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
	
	private List<Position> getPositionsInRadius(Position position, double radius, Set<Position> keySet) {
		List<Position> positionsInRadius = new ArrayList<Position>();
		double x = position.x;
		double y = position.y;
		for (Position p : keySet) {
			double dx = p.x - x;
			double dy = p.y - y;

			double dist = Math.sqrt(dx * dx + dy * dy);
			if (dist <= radius) {
				positionsInRadius.add(p);
			}
		}
		return positionsInRadius;
	}
}
