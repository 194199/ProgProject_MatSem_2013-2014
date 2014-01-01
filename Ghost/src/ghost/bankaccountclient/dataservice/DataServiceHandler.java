package ghost.bankaccountclient.dataservice;

import ghost.bankaccountclient.observer.Observable;
import ghost.bankaccountclient.observer.Observer;
import ghost.bankaccountclient.util.CallbackNotifier;

import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;


/**
 * 
 * @author root
 * Klasa-singleton która obsługuje połaczenie się serwisu z aktywnością, jego rozłączenie, powiadamianie innych fragmentów i aktywności
 * o referencji do serwisu, jego współdzielenie oraz zapewnienie integralności w programie. Programista musi zadbać aby 
 * elementy programu korzystajce z serwisu poprzez DataServiceHandler nie szukały odwołania do niego zbyt wcześnie. Klasa zostanie 
 * powiadomiona o serwisie najwcześniej jak to możliwe, lecz mimoi wsyztsko zaleca się używanie zadań asynchronicznych jeśli serwis jest
 * wymagany
 */
public class DataServiceHandler implements Observable,CallbackNotifier {
	
	/**ClassNotFoundException
	 * Referencja na samego siebie - SINGLETON
	 */
	private static DataServiceHandler sRef;
	
	/**
	 * Metoda singeltonu
	 * @return this
	 */
	public static DataServiceHandler getInstance() {
		if (sRef == null)
			sRef = new DataServiceHandler();
		
		return sRef;
	}
	
	private Context context;
	private LinkedList<Observer> observers;
	
	private DataServiceConnector mServiceConnection;

	private DataServiceHandler() {
		super();
		mServiceConnection = new DataServiceConnector(this);
		observers = new LinkedList<Observer>();
	}
	
	/**
	 * Metoda wiążąca serwis z contekstem
	 * @param context
	 */
	public void bindService(Context context) {
		this.context = context;
		Intent intent = new Intent(context, DataService.class);
		context.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
	}
	
	/**
	 * rozwiązywanie
	 */
	public void unbindService() {
		context.unbindService(mServiceConnection);
	}
	
	public DataService getService() {
		if (mServiceConnection.isServiceConnected())
			return mServiceConnection.getService();
		
		return null;
	}
	
	public boolean isServiceConnected() {
		return mServiceConnection.isServiceConnected();
	}

	
	/**
	 * Dodawanie obiektów które mają być informowane o zmienie stanu dostępności servisu
	 */
	@Override
	public void addObserver(Observer observer) {
		if (!observers.contains(observer))
			observers.add(observer);
		if (mServiceConnection.isServiceConnected())
			observer.update(this, mServiceConnection.getService());
	}

	@Override
	public void addAndNotify(Observer observer, Object value) {
		addObserver(observer);
		observer.update(this, value);
	}

	@Override
	public boolean removeObserver(Observer observer) {
		return observers.remove(observer);
	}

	@Override
	public void notifyObservers(Object value) {
		for (Observer o : observers) {
			o.update(this, value);
		}
	}

	
	/**
	 * Obsługa wydarzenia poinformowania klasy o pojawieniu się serwosu przez klase zajmującą się połączeniem
	 */
	@Override
	public void callbackNotify() {
		notifyObservers(mServiceConnection.getService());
	}

}
