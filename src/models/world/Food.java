package models.world;

public class Food {
	private final double energy;
	private final double nutrition;
	
	public Food(double energyAmount, double nutritionAmount) {
		energy = energyAmount;
		nutrition = nutritionAmount;
	}
	
	public Food fromTemplate(Food template) {
		return new Food(template.energy, template.nutrition);
	}
	
	public double getEnergy() { return energy; }
	public double getNutrition() { return nutrition; }
}
