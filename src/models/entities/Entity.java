package models.entities;

import java.util.HashMap;

import models.genes.Gene;

public abstract class Entity {
	protected double replicationChance;
	protected double deathChance;
	protected HashMap<String, Gene> genes;

	public Entity(double replicationChance, double deathChance) {
		this.replicationChance = replicationChance;
		this.deathChance = deathChance;
		this.genes = new HashMap<String, Gene>();
	}

	public abstract Entity replicate();
	public abstract Entity replicatePerfectly();
	public void die() {
	}

	public double getReplicationChance() {
		return replicationChance;
	}

	public double getDeathChance() {
		return deathChance;
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
}
