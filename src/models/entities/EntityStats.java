package models.entities;

import java.util.Random;

import util.HelperFunctions;

public class EntityStats {
	public enum Need {
		Food, Water, Reproduce, None
	}

	public static double ageGainPerTick = .075;

	private double maxEnergy;

	// This variable goes from maxEnergy to 0
	private double energy;

	// Each of these variables grow from 0 to 1,
	// the higher the number, the larger the urge
	// to sate it & bring it down
	private double reproductiveUrge;
	private double hunger;
	private double thirst;

	private double hungerThreshold;
	private double thirstThreshold;

	// This is a value between 0 and 1 indicating
	// when the Entity should be considered mature
	// in relation to its lifeExpectancy.
	private double maturityThreshold;
	private double lifeExpectancy;
	private double age;

	public EntityStats(double maxEnergy, double lifeExpectancy) {
		this(maxEnergy, .5, .5, .2, lifeExpectancy);
	}

	public EntityStats(double maxEnergy, double hungerThreshold, double thirstThreshold, double maturityThreshold,
			double lifeExpectancy) {
		this.maxEnergy = maxEnergy;
		this.hungerThreshold = hungerThreshold;
		this.thirstThreshold = thirstThreshold;
		this.maturityThreshold = maturityThreshold;
		this.lifeExpectancy = lifeExpectancy;

		this.energy = maxEnergy;
		this.hunger = 0;
		this.thirst = 0;
		this.reproductiveUrge = 0;
		this.age = 0;
	}

	/**
	 * Get a fresh set of stats based on this object's maxs and thresholds.
	 * 
	 * @return A new EntityStats object with all stats set to their starting values.
	 */
	public EntityStats freshStats() {
		return new EntityStats(this.maxEnergy, this.hungerThreshold, this.thirstThreshold, this.maturityThreshold,
				this.lifeExpectancy);
	}

	/**
	 * Subtract the given amount from the current energy of the entity.
	 * 
	 * @param energyUsed
	 * @return False if the energy falls below zero or age is over the life
	 *         expectancy, True if more energy still remains.
	 */
	public boolean tick(double energyUsed) {
		energy -= energyUsed;
		age += ageGainPerTick;

		if (energy <= 0 || age >= lifeExpectancy) {
			return false;
		}
		return true;
	}

	public Need getCurrentNeed() {
		final double dangerZone = .8;
		boolean mustFindFood = hunger >= dangerZone;
//		boolean mustFindWater = thirst >= dangerZone;

		boolean hungry = hunger >= hungerThreshold || energy < 1 - dangerZone;
		boolean thirsty = false;// thirst >= thirstThreshold;

		if (mustFindFood) {
			return Need.Food;
		}
		// if (mustFindWater) {
		// return Need.Water;
		// }

		if (hungry || thirsty) {
			if (hungry || hunger > thirst) {
				return Need.Food;
			}
//			return Need.Water;
		}

		if (isMature() && reproductiveUrge >= .5) {
			return Need.Reproduce;
		}

		return Need.Food;
		// return Need.None;
	}

	/**
	 * Creates a new EntityStats object, randomly selecting properties from between
	 * this and the given mate stats.
	 * 
	 * @param mateStats The stats of the entity we're mating with.
	 * @return An EntityStats object with properties shared between this and
	 *         mateStats.
	 */
	public EntityStats createNewEntityStats(EntityStats mateStats) {
		Random rand = new Random(System.currentTimeMillis());
		double maxEnergy = rand.nextBoolean() ? this.maxEnergy : mateStats.maxEnergy;
		double hungerThreshold = rand.nextBoolean() ? this.hungerThreshold : mateStats.hungerThreshold;
		double thirstThreshold = rand.nextBoolean() ? this.thirstThreshold : mateStats.thirstThreshold;
		double lifeExpectancy = rand.nextBoolean() ? this.lifeExpectancy : mateStats.lifeExpectancy;
		return new EntityStats(maxEnergy, hungerThreshold, thirstThreshold, maturityThreshold, lifeExpectancy);
	}

	/**
	 * Creates a new EntityStats object, randomly selecting properties from between
	 * this and the given mate stats and altering the final stat value between +/-
	 * variationAmount.
	 * 
	 * @param mateStats       The stats of the entity we're mating with.
	 * @param variationAmount The possible variation amount for stats.
	 * @return An EntityStats object with properties shared between this and
	 *         mateStats.
	 */
	public EntityStats createNewEntityStats(EntityStats mateStats, double variationAmount) {
		Random rand = new Random();
		EntityStats stats = createNewEntityStats(mateStats);
		stats.maxEnergy += HelperFunctions.randomRange(rand, -variationAmount, variationAmount);
		stats.hungerThreshold += HelperFunctions.randomRange(rand, -variationAmount, variationAmount);
		stats.thirstThreshold += HelperFunctions.randomRange(rand, -variationAmount, variationAmount);
		stats.lifeExpectancy += HelperFunctions.randomRange(rand, -variationAmount * 2, variationAmount * 2);
		return stats;
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
		this.reproductiveUrge = HelperFunctions.clamp(reproductiveUrge, 0, 1);
	}

	public void increaseReproductiveUrge(double amount) {
		setReproductiveUrge(reproductiveUrge + amount);
	}

	public double getHunger() {
		return hunger;
	}

	public void setHunger(double hunger) {
		this.hunger = Math.max(0, hunger);
	}

	public void increaseHunger(double amount) {
		setHunger(hunger + amount);
	}

	public double getThirst() {
		return thirst;
	}

	public void setThirst(double thirst) {
		this.thirst = Math.max(0, thirst);
	}

	public void increaseThirst(double amount) {
		setThirst(thirst + amount);
	}

	public double getHungerThreshold() {
		return hungerThreshold;
	}

	public void setHungerThreshold(double hungerThreshold) {
		this.hungerThreshold = hungerThreshold;
	}

	public double getThirstThreshold() {
		return thirstThreshold;
	}

	public void setThirstThreshold(double thirstThreshold) {
		this.thirstThreshold = thirstThreshold;
	}

	public double getAge() {
		return age;
	}

	public double getLifeExpectancy() {
		return lifeExpectancy;
	}

	public double progressTowardMaturity() {
		return Math.min(age / (maturityThreshold * lifeExpectancy), 1);
	}

	public boolean isMature() {
		return progressTowardMaturity() == 1;
	}
}
