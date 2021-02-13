package interfaces;

public interface IObservable {
	public void updateObservers();
	public void registerObserver(IObserver<? extends IObservable> observer);
	public void deRegisterObserver(IObserver<? extends IObservable> observer);
}
