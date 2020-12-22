package models;

public class Entity {
	public double reproductionChance;
	public double deathChance;
	
	public Entity(double reproductionChance, double deathChance) {
		this.reproductionChance = reproductionChance;
		this.deathChance = deathChance;
	}
	
	public void die() {
		
	}
}
