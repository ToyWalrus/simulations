package interfaces;

public interface ITracker {
	public String getTrackerName();
	public void registerDataListener(IDataListener listener);
	public void removeDataListener(IDataListener listener);
	public void track(ISimulation simulation);
	public void reset();
}
