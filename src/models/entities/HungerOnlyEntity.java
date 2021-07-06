package models.entities;

/**
 * An entity that only dies when it becomes too hungry. Energy is not
 * considered.
 */
public class HungerOnlyEntity extends Entity {

	public HungerOnlyEntity() {
		this(new EntityStats(0, 100));
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
	public EntityStats.Need getCurrentNeed() {
		return isHungry() ? EntityStats.Need.Food : EntityStats.Need.None;
	}

	@Override
	protected Entity createNewInstance(EntityStats stats) {
		return new HungerOnlyEntity(stats);
	}

	@Override
	public double getEnergy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getNutrition() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public ConsumableType getPreferredFoodType() {
		return ConsumableType.Plant;
	}
}
