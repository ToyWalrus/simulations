package models.genes;

public abstract class Gene {
	protected String name;
	protected double mutationChance;
	protected double value;
	protected double costPerTick;
	
	public Gene(String name, double mutationChance, double value, double costPerTick) {
		this.name = name;
		this.mutationChance = mutationChance;
		this.value = value;
		this.costPerTick = costPerTick;
	}
	
	public double getCostPerTick() { return costPerTick; }
	
	public double getValue() { return value; }
	
	public String getName() { return name; }
}
