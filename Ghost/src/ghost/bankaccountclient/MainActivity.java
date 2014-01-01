package ghost.bankaccountclient;

import ghost.bankaccountclient.dataservice.DataService;
import ghost.bankaccountclient.dataservice.DataServiceConnector;
import ghost.bankaccountclient.dataservice.DataServiceHandler;
import ghost.bankaccountclient.observer.Observable;
import ghost.bankaccountclient.observer.Observer;
import group.pals.android.lib.ui.lockpattern.LockPatternActivity;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.zxing.client.android.CaptureActivity;

public class MainActivity extends FragmentActivity implements Observer {
	
	private static final int QR_READER_REQUEST = 0xff;
	
	private static final int REQ_CREATE_PATTERN = 1;
	
	private DataService mService;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_view);
		
		DataServiceHandler sHandler = DataServiceHandler.getInstance();
		sHandler.addObserver(this);
		sHandler.bindService(this);
	}
	
	

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		new buildInterface().execute();
		
	} 

	

	@Override
	protected void onDestroy() {
		DataServiceHandler.getInstance().unbindService();
		super.onDestroy();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.readerrun) {
			Intent i = new Intent(this,CaptureActivity.class);
			startActivityForResult(i, QR_READER_REQUEST);
		} else {
			Intent intent = new Intent(LockPatternActivity.ACTION_CREATE_PATTERN, null,
			        this, LockPatternActivity.class);
			intent.putExtra(LockPatternActivity.EXTRA_THEME, group.pals.android.lib.ui.lockpattern.R.style.Alp_Theme_Dialog_Dark);
			startActivityForResult(intent, REQ_CREATE_PATTERN);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == Activity.RESULT_OK && requestCode == QR_READER_REQUEST) {
			String result = intent.getStringExtra(CaptureActivity.TAG);
			
			Intent data = new Intent(this,AccountActivity.class);
			data.putExtra(AccountActivity.INPUT_ACCOUNT_NUMBER, result);
			data.putExtra(AccountActivity.INPUT_ACTIVITY_BEHAVIOUR, AccountActivity.READ_FROM_CODE);
			startActivity(data);
		} else if (requestCode == REQ_CREATE_PATTERN) {
	        if (resultCode == RESULT_OK) {
	            char[] pattern = intent.getCharArrayExtra(
	                    LockPatternActivity.EXTRA_PATTERN);
	        }
		}
	}

	@Override
	public void update(Observable observable, Object value) {
		if (value instanceof DataService) {
			mService = (DataService)value;
		} else if (value instanceof DataServiceConnector) {
			mService = ((DataServiceConnector)value).getService();
		}
		
	}



	private class buildInterface extends AsyncTask<Void,Void,Void> {
		
		public void execute() {
			this.execute(new Void[0]);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			while (mService == null) {
				try{ 
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (Throwable e) {}
			}
			
			
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			AccountList f = new AccountList();
			
			DataServiceHandler.getInstance().addObserver(f);
			
			getSupportFragmentManager().beginTransaction().add(R.id.main_view_id, f).commit();
			super.onPostExecute(result);
		}
		
		
		
		
		
	}
	
	
}
