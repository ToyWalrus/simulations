package models.genes;

public class DesirabilityGene extends Gene {
	public final static String name = "Desirability";

	public DesirabilityGene(double mutationChance, double value) {
		super(name, mutationChance, value, 0);
	}

}
