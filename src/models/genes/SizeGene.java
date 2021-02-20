package models.genes;

import java.util.Random;

import util.HelperFunctions;

public class SizeGene extends Gene {
	public final static String name = "Size";

	public SizeGene(double mutationChance, double value) {
		super(name, mutationChance, value, evaluateCostPerTick(value));
	}

	@Override
	public Gene mutate(double mutationVariation) {
		Random rand = new Random();
		double newValue = value + HelperFunctions.randomRange(rand, -mutationVariation, mutationVariation);	
		return new SizeGene(mutationVariation, newValue);
	}

	@Override
	public Gene clone() {
		return new SizeGene(mutationChance, value);
	}
	
	private static double evaluateCostPerTick(double value) {
		// A 2D creature will consume energy by the power of ~2 
		// Assuming max energy is 1, the max size a creature can be for 
		// a single tick is 4.47
		return Math.pow(value, 2) / 20;
	}
}
