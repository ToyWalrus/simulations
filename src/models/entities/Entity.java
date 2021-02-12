package models.entities;

import java.util.HashMap;

import models.genes.Gene;
import models.world.Food;
import models.world.Position;
import models.world.World;

public abstract class Entity {
	protected EntityStats stats;
	protected HashMap<String, Gene> genes;

	public Entity(EntityStats stats) {
		this.stats = stats;
		this.genes = new HashMap<String, Gene>();
	}

	public abstract Position tick(World world, Position position);

	public abstract Entity replicate();

	public abstract Entity replicatePerfectly();

	public void die() {}
	
	public EntityStats getStats() { return stats; }

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
	
	public void eatFood(Food food) {
		stats.gainEnergy(food.getEnergy());
		double hunger = stats.getHunger();
		stats.setHunger(hunger + food.getNutrition());
	}
}
