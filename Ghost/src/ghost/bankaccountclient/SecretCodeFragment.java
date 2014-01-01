package ghost.bankaccountclient;

import ghost.bankaccountclient.dataservice.DataService;
import ghost.bankaccountclient.dataservice.DataServiceConnector;
import ghost.bankaccountclient.observer.Observable;
import ghost.bankaccountclient.observer.Observer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SecretCodeFragment extends Fragment implements Observer {
	
	private DataService mService;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void update(Observable observable, Object value) {
		if (value instanceof DataService) {
			mService = (DataService)value;
		} else if (value instanceof DataServiceConnector) {
			mService = ((DataServiceConnector)value).getService();
		}
	}

}
