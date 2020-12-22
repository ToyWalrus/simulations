package interfaces;

public interface ISimulation {
	public void tick();
	public void reset();
	public void addTracker(ITracker tracker);
}
