package models.entities;

public class EntityStats {
	private double maxEnergy;
	
	// This variable goes from max to 0
	private double energy;

	// Each of these variables grow from 0 to 1,
	// the higher the number, the larger the urge
	// to sate it & bring it down
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
		this.hunger = Math.max(0, hunger);
	}
	
	public void getHungry(double amount) {
		setHunger(hunger + amount);
	}

	public double getThirst() {
		return thirst;
	}

	public void setThirst(double thirst) {
		this.thirst = Math.max(0, thirst);
	}
	
	public void getThirsty(double amount) {
		setThirst(thirst + amount);
	}

}
