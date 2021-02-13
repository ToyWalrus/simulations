package interfaces;

public interface IObserver<T extends IObservable> {
	public void update(T observable);
}
