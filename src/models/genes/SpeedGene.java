package models.genes;

public class SpeedGene extends Gene {
	public final static String name = "Speed";

	public SpeedGene(double mutationChance, double value, double costPerTick) {
		super(name, mutationChance, value, costPerTick);
	}
}
