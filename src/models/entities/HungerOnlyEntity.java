package models.entities;

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

		stats.getHungry(hungerGainPerTick());

		if (isDead()) {
			die();
		}
	}

	@Override
	public double hungerGainPerTick() {
		return .005;
	}

	@Override
	public Entity replicate() {
		return new HungerOnlyEntity(stats);
	}

	@Override
	public Entity replicatePerfectly() {
		return new HungerOnlyEntity(stats);
	}
}
