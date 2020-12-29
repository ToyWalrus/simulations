package models.entities;

public class EntityStats {
	private double maxEnergy;
	private double energy;

	private double reproductiveUrge;
	private double hunger;
	private double thirst;

	public EntityStats(double maxEnergy) {
		this.maxEnergy = this.energy = maxEnergy;
	}

	/**
	 * Subtract the given amount from the current energy of the entity.
	 * 
	 * @param amount
	 * @return False if the energy falls below zero, True if more energy still
	 *         remains.
	 */
	public boolean useEnergy(double amount) {
		energy -= amount;
		if (energy <= 0) {
			return false;
		}
		return true;
	}
	
	public void gainEnergy(double amount) {
		energy = Math.min(energy + amount, maxEnergy);
	}

	public double getEnergy() {
		return energy;
	}

	public double getMaxEnergy() {
		return maxEnergy;
	}

	public void setMaxEnergy(double maxEnergy) {
		this.maxEnergy = maxEnergy;
	}

	public double getReproductiveUrge() {
		return reproductiveUrge;
	}

	public void setReproductiveUrge(double reproductiveUrge) {
		this.reproductiveUrge = reproductiveUrge;
	}

	public double getHunger() {
		return hunger;
	}

	public void setHunger(double hunger) {
		this.hunger = hunger;
	}

	public double getThirst() {
		return thirst;
	}

	public void setThirst(double thirst) {
		this.thirst = thirst;
	}

}
