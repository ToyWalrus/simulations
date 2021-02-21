package models.entities;

public class PopulationTestEntity extends Entity {
	private double replicationChance;
	private double deathChance;
	
	public PopulationTestEntity(double replicationChance, double deathChance) {
		super(new EntityStats(100));
		this.replicationChance = replicationChance;
		this.deathChance = deathChance;
	}

	@Override
	public Entity reproduce(Entity other) {
		return replicatePerfectly();
	}
	
	public double getReplicationChance() {
		return replicationChance;
	}

	public double getDeathChance() {
		return deathChance;
	}

	@Override
	protected Entity createNewInstance(EntityStats withStats) {
		return new PopulationTestEntity(replicationChance, deathChance);
	}
}