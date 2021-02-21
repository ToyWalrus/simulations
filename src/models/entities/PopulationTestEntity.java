package models.entities;

import models.world.World;
import systems.EntityBehavior;

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

	@Override
	public Entity replicatePerfectly() {
		stats.setReproductiveUrge(0);
		return new PopulationTestEntity(replicationChance, deathChance);
	}

	@Override
	public void tick(World world) {
		if (behavior == null) {
			behavior = new EntityBehavior(world, this);
		}
		
		double energyUsed = behavior.doEntityAction();		
		stats.useEnergy(energyUsed);
		updateNeeds();
				
		if (isDead()) {
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