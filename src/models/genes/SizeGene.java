package models.genes;

public class SizeGene extends Gene {
	public final static String name = "Size";

	public SizeGene(double mutationChance, double value) {
		super(name, mutationChance, value, 0);
	}

	@Override
	public double getCostPerTick() {
		return value * value;
	}
}
