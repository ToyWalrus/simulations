package models.entities;

import models.world.World;
import systems.EntityBehavior;

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
	public void tick(World world) {
		if (behavior == null) {
			behavior = new EntityBehavior(world, this);
		}
		
		double energyUsed = behavior.doEntityAction();		
		boolean stillAlive = stats.useEnergy(energyUsed);
		
		if (!stillAlive) {
			die();
		}
	}
	
	public double getReplicationChance() {
		return replicationChance;
	}

	public double getDeathChance() {
		return deathChance;
	}
}