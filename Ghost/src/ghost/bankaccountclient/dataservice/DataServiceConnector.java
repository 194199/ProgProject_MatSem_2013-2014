package ghost.bankaccountclient.dataservice;

import ghost.bankaccountclient.util.CallbackNotifier;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * 
 * @author root
 * Obsługa połączenia serwisu z aktywnością
 */
public class DataServiceConnector implements ServiceConnection {
	
	private DataService mService;
	private boolean serviceConnected;
	private CallbackNotifier handler;

	/**
	 * Metoda przyjmuje referencję do klasy, która ma być powiadomiona o serwisie zaraz po jego pojawieniu się.
	 * @param handler 
	 */
	public DataServiceConnector(CallbackNotifier handler) {
		this.handler = handler;
	}
	
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		serviceConnected = true;
		mService = (DataService)((DataService.DataBinder)service).getService();
		handler.callbackNotify();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		serviceConnected = false;
	}
	
	public boolean isServiceConnected() {
		return serviceConnected;
	}
	
	public DataService getService() {
		if (serviceConnected) {
			return mService;
		}
		return null;
	}

}
