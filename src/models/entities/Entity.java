package models.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import models.entities.EntityStats.Need;
import models.genes.Gene;
import models.world.Food;
import models.world.IConsumable;
import models.world.Position;
import models.world.World;
import systems.EntityBehavior;
import util.HelperFunctions;

public abstract class Entity implements IConsumable {
	public enum Gender {
		Male, Female;

		static Gender random() {
			return new Random().nextBoolean() ? Male : Female;
		}
	}

	public static double mutationVariation = .005;
	protected EntityStats stats;
	protected HashMap<String, Gene> genes;
	protected Position worldPosition;
	protected EntityBehavior behavior;
	protected CauseOfDeath causeOfDeath;
	protected Gender gender;

	public Entity(EntityStats stats) {
		this(stats, Gender.random());
	}

	public Entity(EntityStats stats, Gender gender) {
		this.stats = stats;
		this.genes = new HashMap<String, Gene>();
		this.worldPosition = new Position(0, 0);
		this.gender = gender;
		causeOfDeath = null;
	}

	protected abstract Entity createNewInstance(EntityStats withStats);

	public void tick(World world) {
		if (behavior == null) {
			behavior = new EntityBehavior(world, this);
		}

		double energyUsed = behavior.doEntityAction();
		stats.tick(energyUsed);
		updateNeeds();

		if (shouldDie()) {
			die();
		}
	}

	public void die() {
		die(false);
	}

	public void die(boolean eaten) {
		String cause = "(?)";

		if (eaten) {
			causeOfDeath = CauseOfDeath.Eaten;
			cause = "being eaten";
		} else if (stats.getHunger() >= 1) {
			causeOfDeath = CauseOfDeath.Hunger;
			cause = "hunger";
		} else if (stats.getThirst() >= 1) {
			causeOfDeath = CauseOfDeath.Thirst;
			cause = "thirst";
		} else if (stats.getEnergy() <= 0) {
			causeOfDeath = CauseOfDeath.Energy;
			cause = "no energy";
		} else if (stats.getAge() > stats.getLifeExpectancy()) {
			causeOfDeath = CauseOfDeath.OldAge;
			cause = "old age";
		} else {
			causeOfDeath = CauseOfDeath.Unknown;
			cause = "unknown";
		}

		System.out.println("Entity died due to " + cause + ", at age " + stats.getAge());
	}

	public abstract ConsumableType getPreferredFoodType();

	public Position getPosition() {
		return worldPosition;
	}

	public void setPosition(Position p) {
		worldPosition = p;
	}

	public EntityStats getStats() {
		return stats;
	}

	public double energySpentBeingIdle() {
		return stats.getMaxEnergy() / 100.0;
	}

	public double hungerGainPerTick() {
		return 0.0025;
	}

	public double thirstGainPerTick() {
		return 0.002;
	}

	public double reproductiveUrgeGainPerTick() {
		return 0.01;
	}

	public Gender getGender() {
		return gender;
	}

	protected void updateNeeds() {
		stats.increaseHunger(hungerGainPerTick());
		stats.increaseThirst(thirstGainPerTick());
		stats.increaseReproductiveUrge(reproductiveUrgeGainPerTick());
	}

	public boolean isDead() {
		return causeOfDeath != null;
	}

	public boolean shouldDie() {
		return stats.getEnergy() <= 0 || stats.getHunger() >= 1 || stats.getThirst() >= 1
				|| stats.getAge() > stats.getLifeExpectancy();
	}

	public boolean isHungry() {
		return stats.getHunger() >= stats.getHungerThreshold();
	}

	public boolean isThirsty() {
		return stats.getThirst() >= stats.getThirstThreshold();
	}

	public Need getCurrentNeed() {
		return stats.getCurrentNeed();
	}

	public Gene getGene(String geneName) {
		return genes.get(geneName);
	}

	public Collection<Gene> getAllGenes() {
		return genes.values();
	}

	public boolean addGene(Gene gene) {
		if (genes.containsKey(gene.getName())) {
			System.out.println("Trying to add a gene already present in this entity: " + gene.getName());
			return false;
		}
		genes.put(gene.getName(), gene);
		return true;
	}

	public boolean updateGene(String geneName, Gene updatedGene) {
		if (!genes.containsKey(geneName)) {
			System.out.println("Cannot update a gene that was never set! " + geneName);
			return false;
		}
		genes.put(geneName, updatedGene);
		return true;
	}

	public void consume(IConsumable food) {
		stats.gainEnergy(food.getEnergy());
		double hunger = stats.getHunger();
		stats.setHunger(hunger - food.getNutrition());
	}

	public void drinkWater(double amount) {
		double thirst = stats.getThirst();
		stats.setThirst(thirst - amount);
	}

	public CauseOfDeath getCauseOfDeath() {
		return causeOfDeath;
	}

	/**
	 * The amount of energy this entity provides when consumed. It will be
	 * equivalent to a percentage of its max energy divided by its current energy
	 * and its progress toward maturity.
	 */
	@Override
	public double getEnergy() {
		final double maxEnergyRatio = .85;
		double currentEnergyRatio = stats.getEnergy() / stats.getMaxEnergy();
		double maxEnergyGained = stats.getMaxEnergy() * maxEnergyRatio * currentEnergyRatio;
		return maxEnergyGained * stats.progressTowardMaturity();
	}

	@Override
	public double getNutrition() {
		final double nutritionBaseValue = 1;
		return nutritionBaseValue * stats.progressTowardMaturity();
	}

	@Override
	public ConsumableType getConsumableType() {
		return ConsumableType.Entity;
	}

	/**
	 * The other Entity must be of the opposite gender and must have the need to
	 * reproduce.
	 * 
	 * @param other
	 * @return Whether the given Entity is available to mate with.
	 */
	public boolean canReproduceWith(Entity other) {
		if (other == null)
			return false;
		return other.getStats().getCurrentNeed() == Need.Reproduce && gender != other.gender;
	}

	public Entity reproduce(Entity other) {
		Random rand = new Random();

		stats.setReproductiveUrge(0);
		other.stats.setReproductiveUrge(0); // setting this for the other entity because reproduce will only be called
											// on one entity of the pair

		Entity e = createNewInstance(stats.createNewEntityStats(other.stats, mutationVariation));
		e.setPosition(HelperFunctions.lerp(getPosition(), other.getPosition(), .5));

		Set<String> geneNames = new HashSet<String>(
				getAllGenes().stream().map(g -> g.getName()).collect(Collectors.toSet()));
		geneNames.addAll(other.getAllGenes().stream().map(g -> g.getName()).collect(Collectors.toSet()));

		for (String geneName : geneNames) {
			Gene geneA = getGene(geneName);
			Gene geneB = other.getGene(geneName);

			Gene gene;
			if (geneA != null && geneB != null) {
				gene = rand.nextBoolean() ? geneA : geneB;
			} else if (geneA != null) {
				gene = geneA;
			} else {
				gene = geneB;
			}

			if (rand.nextDouble() < gene.getMutationChance()) {
				e.addGene(gene.mutate(mutationVariation));
				System.out.println(geneName + " mutated!");
			} else {
				e.addGene(gene.clone());
			}
		}

		return e;
	}

	public Entity replicatePerfectly() {
		stats.setReproductiveUrge(0);
		return createNewInstance(stats.freshStats());
	}
}
