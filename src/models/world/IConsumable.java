package models.world;

public interface IConsumable {
	/**
	 * Gets the energy amount stored in this food.
	 */
	public double getEnergy();
	
	/**
	 * Gets the nutrition amount stored in this food.
	 * This will be used to sate the hunger stat.
	 */
	public double getNutrition();
	
	public ConsumableType getConsumableType();
	
	public enum ConsumableType {
		Plant, Entity, Any
	}
}
