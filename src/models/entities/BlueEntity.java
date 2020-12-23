package models.entities;

public class BlueEntity extends Entity {
	public BlueEntity(double replicationChance, double deathChance) {
		super(replicationChance, deathChance);
	}

	@Override
	public Entity replicate() {
		return new BlueEntity(replicationChance, deathChance);
	}

	@Override
	public Entity replicatePerfectly() {
		return new BlueEntity(replicationChance, deathChance);
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub

	}
}