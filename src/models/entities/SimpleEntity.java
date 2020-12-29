package models.entities;

import models.world.Position;
import models.world.World;

public class SimpleEntity extends Entity {
	private double replicationChance;
	private double deathChance;
	
	public SimpleEntity(double replicationChance, double deathChance) {
		super(new EntityStats(100));
		this.replicationChance = replicationChance;
		this.deathChance = deathChance;
	}

	@Override
	public Entity replicate() {
		return replicatePerfectly();
	}

	@Override
	public Entity replicatePerfectly() {
		return new SimpleEntity(replicationChance, deathChance);
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub

	}

	@Override
	public Position tick(World world, Position position) {
		return position;
	}
	
	public double getReplicationChance() {
		return replicationChance;
	}

	public double getDeathChance() {
		return deathChance;
	}
}