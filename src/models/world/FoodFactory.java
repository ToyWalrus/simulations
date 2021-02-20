package models.world;

public final class FoodFactory {
	private Food foodTemplate;
	
	public FoodFactory(Food template) {
		foodTemplate = template;
	}
	
	public Food create() {
		return Food.fromTemplate(foodTemplate);
	}
}
