package models.genes;

public abstract class Gene {
	protected String name;
	protected double mutationChance;
	public Gene(String name, double mutationChance) {
		this.name = name;
		this.mutationChance = mutationChance;
	}
	
	public String getName() { return name; }
}
