package models.genes;

import java.util.Random;

import util.HelperFunctions;

public class DesirabilityGene extends Gene {
	public final static String name = "Desirability";

	public DesirabilityGene(double mutationChance, double value) {
		super(name, mutationChance, value, 0);
	}

	@Override
	public Gene mutate(double mutationVariation) {
		Random rand = new Random();
		double newValue = value + HelperFunctions.randomRange(rand, -mutationVariation, mutationVariation);
		return new DesirabilityGene(mutationVariation, newValue);
	}

	@Override
	public Gene clone() {
		return new DesirabilityGene(mutationChance, value);
	}

}
