package ghost.bankaccountclient;

import ghost.bankaccountclient.dataservice.DataService;
import ghost.bankaccountclient.dataservice.DataServiceConnector;
import ghost.bankaccountclient.dataservice.DataServiceHandler;
import ghost.bankaccountclient.net.exceptions.ConnectionNotInitializedException;
import ghost.bankaccountclient.net.utils.InformationToken;
import ghost.bankaccountclient.observer.Observable;
import ghost.bankaccountclient.observer.Observer;
import ghost.bankaccountclient.util.PackageInfo;

import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.widget.Toast;

public class AccountActivity extends FragmentActivity implements Observer {
	
	//Wymagane argumenty do prezekazania
	
	// Informacja o wartości odczytanej z qr-kodu
	public static final String INPUT_ACCOUNT_NUMBER = AccountActivity.class.getSimpleName()+".ACCOUNT";
	
	// Informacja o zachowaniu aktywności
	public static final String INPUT_ACTIVITY_BEHAVIOUR = AccountActivity.class.getSimpleName()+".BEHAVIOUR";
	
	//###################################################################################################
	
	//behaviour	of Activity
	
	// odczytana informacja z kodu
	public static final String READ_FROM_CODE = AccountActivity.class.getSimpleName()+".READ_QR_CODE";
	
	// odczytana informacja z bazy
	public static final String SELECTED_FROM_LIST = AccountActivity.class.getSimpleName()+".SELECTED_FROM LIST";
	
	//###################################################################################################
	
	// argumenty zarządzania stanem aktywności
	
	// tag głownego fragmentu widoku
	private static final String ACCOUNT_VIEW_FRAGMENT_TAG = AccountActivity.class.getSimpleName()+".ACCOUNT_FRAGMENT_VIEW";
	
	// tag zapisanego stanu aktywności
	private static final String ACCOUT_VIEW_STATE = AccountActivity.class.getSimpleName()+".ACCOUNT_FRAGMENT_STATE";
	
	//###################################################################################################
	
	private DataService mService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);

		//Nie znaleziono argumentuów
		if (getIntent() == null) {
			Toast.makeText(this, R.string.code_not_found, Toast.LENGTH_LONG).show();
			setResult(RESULT_CANCELED);
			finish();
		}
		// Sprawdzanie poprawności przekazanego argumentu w intencji
		if (!isCorrectSource(getIntent().getStringExtra(INPUT_ACCOUNT_NUMBER))) {
			Toast.makeText(this, R.string.wrong_code_pattern, Toast.LENGTH_LONG).show();
			setResult(RESULT_CANCELED);
			finish();
		}	
		// Sprawdzanie obecności argumentu zachowania się aktywności
		if (getIntent().getStringExtra(INPUT_ACTIVITY_BEHAVIOUR) == null) {
			Toast.makeText(this, R.string.behaviour_not_found, Toast.LENGTH_LONG).show();
			setResult(RESULT_CANCELED);
			finish();
		}
		
		DataServiceHandler sHandler = DataServiceHandler.getInstance();
		sHandler.addObserver(this);
		
		String[] qrSource = getIntent().getStringExtra(INPUT_ACCOUNT_NUMBER).split("##");
		
		mService.createConnection(qrSource[0], Integer.parseInt(qrSource[1]));
		
		if (!isFinishing()) 
			init(savedInstanceState,qrSource[2]);
	}
	
	private void init(Bundle savedInstanceState, final String accountNumber) {
		if (savedInstanceState == null || savedInstanceState.getBundle(ACCOUT_VIEW_STATE) == null) {
			this.getSupportFragmentManager().beginTransaction().add(R.id.account_empty_view, new LoadFeatureFragment()).commit();
			new ConnectionThread(accountNumber).start();
		} else {
			Bundle intent = savedInstanceState.getBundle(ACCOUT_VIEW_STATE);
		    Fragment f = new AccountMainViewFragment();
		    f.setArguments(intent);
		    AccountActivity.this.getSupportFragmentManager().beginTransaction()
		    .replace(R.id.account_empty_view, f, ACCOUNT_VIEW_FRAGMENT_TAG).commit();
		}
	}
	
	private boolean isCorrectSource(String source) {
			if (source == null || !Pattern.matches("^[\\d]{1,3}.[\\d]{1,3}.[\\d]{1,3}.[\\d]{1,3}##[\\d]+##[\\d]{26}$", source)) {
				return false;
			}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account, menu);
		return true;
	}

	@Override
	public void update(Observable observable, Object value) {
		if (value instanceof DataService) {
			mService = (DataService)value;
		} else if (value instanceof DataServiceConnector) {
			mService = ((DataServiceConnector)value).getService();
		}
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		Fragment f = getSupportFragmentManager().findFragmentByTag(ACCOUNT_VIEW_FRAGMENT_TAG);
		
		if (f != null) {
			outState.putBundle(ACCOUT_VIEW_STATE, f.getArguments());
		}
	}
	
	private class ConnectionThread extends Thread {
		
		private String accountNumber;

		public ConnectionThread(String accountNumber) {
			super();
			this.accountNumber = accountNumber;
		}

		@Override
		public void run() {
			InformationToken t = null;
			try {
				t = mService.sendAccountNumber(accountNumber);
				
			/*	switch (t.getPackageType()) {
					case PackageInfo.ID_IMEI_REQUEST:
					String aosid = Secure.getString(AccountActivity.this.getContentResolver(), Secure.ANDROID_ID);
				}*/
				
				Bundle intent = new Bundle();
				String[] result;
				
				if (t.getPackageType() == PackageInfo.ACCOUNT_NOT_EXISTS) {
					Fragment f = new AccountMainViewFragment();
					intent.putBoolean(AccountMainViewFragment.ACCOUNT_NOT_EXIST_ERR, true);
					f.setArguments(intent);
					AccountActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.account_empty_view, f).commit();
				} else if (t.getPackageType() == PackageInfo.ACCOUNT_BLOCKED) {
					Fragment f = new AccountMainViewFragment();
					intent.putBoolean(AccountMainViewFragment.ACCOUNT_REMOTE_BLOCKED, true);
					f.setArguments(intent);
					AccountActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.account_empty_view, f).commit();
				} else if (t.getPackageType() == PackageInfo.ID_IMEI_REQUEST) {
					String aosid = Secure.getString(AccountActivity.this.getContentResolver(), Secure.ANDROID_ID);
					String imei = ((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
					
					t = mService.sendAosIDIMEI(aosid+"##"+imei);
					
				/*	result = t.getValue().split("##");
					intent.putStringArray(AccountMainViewFragment.ACCOUNT_DATA_INFO, 
							new String[] {result[0]+" "+result[1], accountNumber[0] ,result[2]});
					
					intent.putStringArray(AccountMainViewFragment.ACCOUNT_DATA_INFO, 
							new String[] {result[0]+" "+result[1], accountNumber[0] ,result[2]});
				    Fragment f = new AccountMainViewFragment();
				    f.setArguments(intent);
				    
				    AccountActivity.this.getSupportFragmentManager().beginTransaction()
				    .replace(R.id.account_empty_view, f, ACCOUNT_VIEW_FRAGMENT_TAG).commit();*/
				}						
				
				
			} catch (ConnectionNotInitializedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
