package ghost.bankaccountclient.dataservice;

import ghost.bankaccountclient.net.SSLConnectionHandler;
import ghost.bankaccountclient.net.exceptions.ConnectionNotInitializedException;
import ghost.bankaccountclient.net.utils.InformationToken;
import ghost.bankaccountclient.util.PackageInfo;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


/**
 * 
 * @author root
 * Serwis mający za zadanie obsługę transakcji pomiędzy programem a bazą danych oraz siecią Intenret. 
 * Udostępnia interfejs, który umożliwia ingerencję w oba zasoby bez zbędnego powielania kodu
 */
public class DataService extends Service {

	private DataBinder dataBinder;
	
	private SSLConnectionHandler sslHandler;
	
	@Override
	public IBinder onBind(Intent intent) {
		dataBinder = new DataBinder();
		sslHandler = new SSLConnectionHandler(this.getApplicationContext());
		return dataBinder;
	}
	
	public void createConnection(String address, int port) {
		try {
			sslHandler.initConnection(address, port);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public InformationToken sendAccountNumber(String value) throws ConnectionNotInitializedException, TimeoutException {
		return sslHandler.sendRequest(PackageInfo.INFO_NUMBER_ACCOUNT, value);
	}
	
	public InformationToken sendAosIDIMEI(String value) throws TimeoutException, ConnectionNotInitializedException {
		return sslHandler.sendRequest(PackageInfo.INFO_ID_IMEI, value);
	}
	
	/**
	 * 
	 * @author root
	 * Lokalna implementacja klasy Binder udostępniajaca interfejs zwracający referencję do serwisu
	 */
	public class DataBinder extends Binder {  
		public Service getService() {
			return DataService.this;
		}
	}

}
