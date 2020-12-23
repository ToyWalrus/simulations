package models.genes;

public abstract class Gene {
	protected double mutationChance;
	public Gene(double mutationChance) {
		this.mutationChance = mutationChance;
	}
}
