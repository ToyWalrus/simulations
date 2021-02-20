package models.entities;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import models.genes.Gene;
import models.world.World;
import systems.EntityBehavior;

/**
 * An entity that only dies when it becomes too hungry. Energy is not
 * considered.
 */
public class HungerOnlyEntity extends Entity {

	public HungerOnlyEntity() {
		this(new EntityStats(0));
		stats.setHunger(0);
	}

	public HungerOnlyEntity(EntityStats stats) {
		super(stats);
	}

	@Override
	public boolean isDead() {
		return stats.getHunger() >= 1;
	}

	@Override
	public void tick(World world) {
		if (behavior == null) {
			behavior = new EntityBehavior(world, this);
		}

		behavior.doEntityAction();
		updateNeeds();

		if (isDead()) {
			die();
		}
	}
	
	@Override
	public EntityStats.Need getCurrentNeed() {
		EntityStats.Need need = stats.getCurrentNeed();
		if (need == EntityStats.Need.Water) return EntityStats.Need.Food;
		return need;
	}

	@Override
	public double hungerGainPerTick() {
		return .005;
	}

	@Override
	public Entity reproduce(Entity other) {
		Random rand = new Random();
		double mutationVariation = .005;
		
		stats.setReproductiveUrge(0);
		other.stats.setReproductiveUrge(0); // setting this for the other entity because reproduce will only be called on one
		
		Entity e = new HungerOnlyEntity(stats.createNewEntityStats(other.stats, mutationVariation));
		
		Set<String> geneNames = new HashSet<String>(getAllGenes().stream().map(g -> g.getName()).collect(Collectors.toSet()));
		geneNames.addAll(other.getAllGenes().stream().map(g -> g.getName()).collect(Collectors.toSet()));
		
		for (String geneName : geneNames) {
			Gene geneA = getGene(geneName);
			Gene geneB = other.getGene(geneName);
			
			Gene gene;
			if (geneA != null && geneB != null) {
				gene = rand.nextBoolean() ? geneA : geneB;
			} else if (geneA != null) {
				gene = geneA;
			} else {
				gene = geneB;
			}
			
			if (rand.nextDouble() < gene.getMutationChance()) {
				e.addGene(gene.mutate(mutationVariation));
				System.out.println(geneName + " mutated!");
			} else {
				e.addGene(gene.clone());
			}
		}
		
		return e;
	}

	@Override
	public Entity replicatePerfectly() {
		stats.setReproductiveUrge(0);
		return new HungerOnlyEntity(stats.freshStats());
	}
}
