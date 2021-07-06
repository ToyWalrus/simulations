package models.entities;

public class CannibalEntity extends Entity {

	public CannibalEntity(EntityStats stats) {
		super(stats);
	}

	@Override
	protected Entity createNewInstance(EntityStats withStats) {
		return new CannibalEntity(withStats);
	}

	@Override
	public ConsumableType getPreferredFoodType() {
		return ConsumableType.Entity;
	}
}
