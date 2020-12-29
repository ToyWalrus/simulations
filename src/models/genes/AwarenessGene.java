package models.genes;

public class AwarenessGene extends Gene {
	public final static String name = "Awareness";

	public AwarenessGene(double mutationChance, double value, double costPerTick) {
		super(name, mutationChance, value, costPerTick);
	}
}
