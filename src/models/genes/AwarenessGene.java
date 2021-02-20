package models.genes;

import java.util.Random;

import util.HelperFunctions;

public class AwarenessGene extends Gene {
	public final static String name = "Awareness";

	public AwarenessGene(double mutationChance, double value) {
		super(name, mutationChance, value, evaluateCostPerTick(value));
	}

	@Override
	public Gene mutate(double mutationVariation) {
		Random rand = new Random();
		double newValue = value + HelperFunctions.randomRange(rand, -mutationVariation, mutationVariation);	
		return new AwarenessGene(mutationVariation, newValue);
	}

	@Override
	public Gene clone() {
		return new AwarenessGene(mutationChance, value);
	}
	
	private static double evaluateCostPerTick(double value) {
		// This gene can be a nearly linear relationship
		// This curve is so flat it essentially is
		return Math.pow(value, 3) / 20000.0;
	}
}
