package models.genes;

import java.util.Random;

import util.HelperFunctions;

public class SpeedGene extends Gene {
	public final static String name = "Speed";

	public SpeedGene(double mutationChance, double value) {
		super(name, mutationChance, value, evaluateCostPerTick(value));
	}

	@Override
	public Gene clone() {
		return new SpeedGene(mutationChance, value);
	}
	
	@Override
	public Gene mutate(double mutationVariation) {
		Random rand = new Random();
		double newValue = value + HelperFunctions.randomRange(rand, -mutationVariation, mutationVariation);	
		return new SpeedGene(mutationVariation, newValue);
	}
	
	private static double evaluateCostPerTick(double value) {
		// We want the relationship to be somewhat linear
		// Function obtained from playing around with https://www.desmos.com/calculator
		// x = speed (value), y = cost (return value)
		// With this function, the max speed comes to 7.937 (the speed at which one tick 
		// will cost 1 energy, assuming the maxEnergy of the entity is 1)
		return Math.pow(value, 3) / 500.0;
	}
}
