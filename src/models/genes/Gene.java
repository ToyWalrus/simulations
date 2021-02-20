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
	
	public double getCostPerTick() { return Math.max(0, costPerTick); }
	
	public double getValue() { return value; }
	
	public String getName() { return name; }
	
	public double getMutationChance() { return mutationChance; }
	
	/**
	 * Creates a new Gene based on this, with the value mutated between
	 * -mutationVariation and +mutationVariation.
	 * @param mutationVariation The range of variation.
	 * @return A new Gene.
	 */
	public abstract Gene mutate(double mutationVariation);
	
	/**
	 * Returns a perfect replica of this Gene as a new Gene object.
	 */
	public abstract Gene clone();
}
