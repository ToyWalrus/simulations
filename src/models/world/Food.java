package models.world;

public class Food {
	private boolean grows;
	private double energy;
	private double nutrition;
	
	private double energyGainPerTick;
	private double nutritionGainPerTick;
	private int ticksUntilFullyGrown;
	private int ticksPassed;
	
	public Food(double energyGainPerTick, double nutritionGainPerTick, int ticksUntilFullyGrown) {
		this.energyGainPerTick = energyGainPerTick;
		this.nutritionGainPerTick = nutritionGainPerTick;
		this.ticksUntilFullyGrown = ticksUntilFullyGrown;
		
		this.energy = 0;
		this.nutrition = 0;
		this.ticksPassed = 0;
		this.grows = ticksUntilFullyGrown > 0;
	}
	
	public static Food createFullyGrown(double energyAmount, double nutritionAmount) {
		Food f = new Food(0, 0, 0);
		f.energy = energyAmount;
		f.nutrition = nutritionAmount;
		f.grows = false;
		return f;
	}
	
	
	public static Food fromTemplate(Food template) {
		Food f = new Food(template.energyGainPerTick, template.nutritionGainPerTick, template.ticksUntilFullyGrown);
		f.energy = template.energy;
		f.nutrition = template.nutrition;
		f.grows =  template.grows;
		f.ticksPassed = template.ticksPassed;
		return f;
	}
	
	public void growthTick() {
		if (!grows || ticksPassed > ticksUntilFullyGrown) return;
		energy += energyGainPerTick;
		nutrition += nutritionGainPerTick;
		ticksPassed++;
	}
	
	/**
	 * Gets the energy amount stored in this food.
	 */
	public double getEnergy() { return energy; }
	/**
	 * Gets the nutrition amount stored in this food.
	 * This will be used to sate the hunger stat.
	 */
	public double getNutrition() { return nutrition; }
	
	public double getEnergyGainPerTick() { return energyGainPerTick; }
	public double getNutritionGainPerTick() { return nutritionGainPerTick; }
	
	public double getMaxPossibleEnergy() { return grows ? energyGainPerTick * ticksUntilFullyGrown : energy; }
	public double getMaxPossibleNutrition() { return grows ? nutritionGainPerTick * ticksUntilFullyGrown : nutrition; }
}
