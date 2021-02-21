package models.entities;

import models.world.World;

public class BasicEntity extends Entity {

	public BasicEntity(EntityStats stats) {
		super(stats);
	}
	
	

	@Override
	public void tick(World world) {
		super.tick(world);
		stats.setThirst(0);
	}


	@Override
	protected Entity createNewInstance(EntityStats withStats) {
		return new BasicEntity(withStats);
	}

	@Override
	public boolean isDead() {
		return stats.getEnergy() <= 0 || stats.getHunger() >= 1;
	}
}
