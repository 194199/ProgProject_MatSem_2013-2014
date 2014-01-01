package ghost.bankaccountclient.observer;


public interface Observable {
	public void addObserver(Observer observer);
	public void addAndNotify(Observer obserer, Object value);
	public boolean removeObserver(Observer observer);
	public void notifyObservers(Object value);
}
