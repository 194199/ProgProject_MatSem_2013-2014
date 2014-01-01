package ghost.bankaccountclient.net;

import ghost.bankaccountclient.R;
import ghost.bankaccountclient.net.exceptions.ConnectionNotInitializedException;
import ghost.bankaccountclient.net.utils.InformationToken;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Klasa obsługująca i zarządzająca połączeniem z serwerem. Zajmuje się inizjalizacją socketu SSL łącznie z załączeniem keystorów o truststorów.
 * @author root
 *
 */
public class SSLConnectionHandler {
	
	/**
	 * ID keystora
	 */
	public static final int KEY_STORE_NAME = R.raw.clientkeystore;
	
	/**
	 * ID truststora
	 */
	public static final int TRUST_STORE_NAME = R.raw.clienttruststore;
	
	/**
	 * Hasło zabezpieczające keystore
	 */
	public static final String KEY_STORE_PASSWORD = "MyN3wP455w00rD";
	
	/**
	 * Hasło zapezpieczające klucz prywatny w keystorze
	 */
	public static final String PRIVATE_KEY_PASSWORD = "MyN3wP455w00rD";
	
	/**
	 * Hasło zapezpieczające trusystore
	 */
	public static final String TRUST_STORE_PASSWORD = "MyN3wP455w00rD";
	
	/**
	 * Czas oczekiwania na odpowiedź serwera w sekundach
	 */
	private static final int WAITING_TIME_FOR_DATA = 3;
	
	/**
	 * Rodzaj keystora przechowującego klucze i certyfikaty
	 */
	private static final String STORE_TYPE = "BKS";
	
	private SocketThread socket;
	private Context context;
	private boolean isConnected;
	
	public SSLConnectionHandler(Context context) {
		this.context = context;
		this.isConnected = false;
	}
	
	public void initConnection(String address, int port) throws IOException {
		try {
			socket = new SocketThread(getSSLSocket(address, port));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		socket.start();
		isConnected = true;
	}
	
	/**
	 * Metoda odpowiedzialna za wysłanie zapytania do serwera o zwrócenie jego odpowiedzi
	 * @param order ID zapytania
	 * @param request treść zapytania w odpowiednim formacie dla podanego ID
	 * @return odpowiedź serwera
	 * @throws TimeoutException przekroczono czas oczekiwania na odpowiedź
	 */
	public InformationToken sendRequest(int order, String request) throws TimeoutException,ConnectionNotInitializedException {
		if (!isConnected) {
			throw new ConnectionNotInitializedException(context.getString(R.string.not_initialized_connection));
		}
		ConnectionThread requestThread = new ConnectionThread();
		requestThread.execute(order, request);
		
		InformationToken token = null;
		
		try {
			token = requestThread.get(WAITING_TIME_FOR_DATA, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} 
		
		return token;
	}
	
	public void closeConnection() throws IOException {
		socket.closeStreams();
		socket = null;
		isConnected = false;
	}
	
	public boolean isConnected() {
		return isConnected;
	}
	
	/**
	 * Metoda zwraca soket SSL z zainicjalizowanymi certyfikatami i połączony z serwerem
	 * @param address adres serwera
	 * @param port posr na którym pracuje serwer
	 * @return połaczony soket
	 * @throws IOException Nieudana próba łączenia
	 * @throws NoSuchAlgorithmException 
	 */
	private SSLSocket getSSLSocket(String address, int port) throws IOException, NoSuchAlgorithmException {
		SSLSocket socket = null;
		try {
			socket = new InitializedSocket().execute(address, ""+port).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return socket;
	} 
	
	/**
	 * Tworzenie keystora
	 * @return keystore dostępny z zasobach
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 * @throws UnrecoverableKeyException 
	 */
	private KeyManagerFactory getKeyManagerFactory() throws UnrecoverableKeyException, KeyStoreException {
		KeyStore ks = getStore(KEY_STORE_NAME, KEY_STORE_PASSWORD);
		KeyManagerFactory kmf = null;
		try {
			kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, KEY_STORE_PASSWORD.toCharArray());
		} catch (NoSuchAlgorithmException ex) {
			//ignore
		}
		
		return kmf;
	}
	
	/**
	 * Tworzenie truststora
	 * @return truststore dostępny z zasobach
	 * @throws KeyStoreException 
	 * @throws NoSuchAlgorithmException 
	 */
	private TrustManagerFactory getTrustManagerFactory() throws KeyStoreException {
		KeyStore ts = getStore(TRUST_STORE_NAME, TRUST_STORE_PASSWORD);
		TrustManagerFactory tmf = null;
		try {
			tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		} catch (NoSuchAlgorithmException ex) {
			//ignore
		}
		tmf.init(ts);
		return tmf;
	}
	
	private KeyStore getStore(int resourceID, String password) {
		try {
			InputStream storeStream = context.getResources().openRawResource(resourceID);
			KeyStore store = KeyStore.getInstance(STORE_TYPE);
			store.load(storeStream, password.toCharArray());
			return store;
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		return null;
	}
	
	private class InitializedSocket extends AsyncTask<String,Void,SSLSocket> {

		@Override
		protected SSLSocket doInBackground(String... params) {
			SSLContext sslContext = null;
			SSLSocket socket = null;
			try {
				sslContext = SSLContext.getInstance("TLS");
			} catch (NoSuchAlgorithmException e1) {
				// ignore
			}
			
			try {
				sslContext.init(getKeyManagerFactory().getKeyManagers(), getTrustManagerFactory().getTrustManagers(), null);
				socket = (SSLSocket)sslContext.getSocketFactory().createSocket(params[0], Integer.parseInt(params[1]));
			} catch (KeyManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnrecoverableKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException ex) {
				ex.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			return socket;
		}
		
	}
	
	private class ConnectionThread extends AsyncTask<Object, Integer, InformationToken> {

		@Override
		protected InformationToken doInBackground(Object... args) {
			//if (args.length != 2 || !(args[0] instanceof Integer) || !(args[1] instanceof String))
				//this.cancel(true);
			
			InformationToken token = null;
			try {
				token = (InformationToken)socket.sendRequest((String)args[1], (Integer)args[0]);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return token;
		}
		
	}
	
}
