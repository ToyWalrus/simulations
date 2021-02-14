package models.entities;

import java.util.HashMap;

import models.genes.Gene;
import models.world.Food;
import models.world.Position;
import models.world.World;
import systems.EntityBehavior;

public abstract class Entity {
	protected EntityStats stats;
	protected HashMap<String, Gene> genes;
	protected Position worldPosition;
	protected EntityBehavior behavior;

	public Entity(EntityStats stats) {
		this.stats = stats;
		this.genes = new HashMap<String, Gene>();
	}

	public abstract void tick(World world);

	public abstract Entity replicate();

	public abstract Entity replicatePerfectly();

	public void die() {
		String cause = "";
		
		if (stats.getHunger() >= 1) cause = "hunger";
		else if (stats.getThirst() >= 1) cause = "thirst";
		else if (stats.getEnergy() <= 0) cause = "no energy";
		
		System.out.println("Entity died due to " + cause);
	}
	
	public Position getPosition() { return worldPosition; }
	
	public void setPosition(Position p) { worldPosition = p; }
	
	public EntityStats getStats() { return stats; }
	
	public double energySpentBeingIdle() { return 0.01; }
	
	public double hungerGainPerTick() { return 0.02; }
	
	public double thirstGainPerTick() { return 0.02; }
	
	public boolean isDead() { return stats.getEnergy() <= 0 || stats.getHunger() >= 1 || stats.getThirst() >= 1; }
	
	public boolean isHungry() {
		return stats.getHunger() >= stats.getHungerThreshold();
	}
	
	public boolean isThirsty() {
		return stats.getThirst() >= stats.getThirstThreshold();
	}

	public Gene getGene(String geneName) {
		return genes.get(geneName);
	}

	public boolean addGene(Gene gene) {
		if (genes.containsKey(gene.getName())) {
			System.out.println("Trying to add a gene already present in this entity: " + gene.getName());
			return false;
		}
		genes.put(gene.getName(), gene);
		return true;
	}
	
	public boolean updateGene(String geneName, Gene updatedGene) {
		if (!genes.containsKey(geneName)) {
			System.out.println("Cannot update a gene that was never set! "  + geneName);
			return false;
		}
		genes.put(geneName, updatedGene);
		return true;
	}
	
	public void eatFood(Food food) {
		stats.gainEnergy(food.getEnergy());
		double hunger = stats.getHunger();
		stats.setHunger(hunger - food.getNutrition());
	}
}
