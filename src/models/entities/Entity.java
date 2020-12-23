package models.entities;

public abstract class Entity {
	protected double replicationChance;
	protected double deathChance;

	public Entity(double reproductionChance, double deathChance) {
		this.replicationChance = reproductionChance;
		this.deathChance = deathChance;
	}	
	
	public abstract Entity replicate();
	public abstract Entity replicatePerfectly();
	public abstract void die();

	public double getReplicationChance() {
		return replicationChance;
	}

	public double getDeathChance() {
		return deathChance;
	}
}
