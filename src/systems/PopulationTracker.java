package systems;

public class PopulationTracker implements ITracker {
	private int trackCalls = 0;
	private double popAverage = 0;
	private int popSum = 0;
	
	@Override
	public void track(Simulation sim) {
		trackCalls++;
		
		int currentPop = sim.getCurrentPopulation();
		popSum += currentPop;
		popAverage = (double) popSum / (double) trackCalls;
	}

	@Override
	public double getAverage() {
		return popAverage;
	}	
}
