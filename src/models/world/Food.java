package models.world;

public class Food {
	private final double energy;
	private final double nutrition;
	
	public Food(double energyAmount, double nutritionAmount) {
		energy = energyAmount;
		nutrition = nutritionAmount;
	}
	
	public Food(Food template) {
		this.energy = template.energy;
		this.nutrition = template.nutrition;
	}
	
	public double getEnergy() { return energy; }
	public double getNutrition() { return nutrition; }
}
