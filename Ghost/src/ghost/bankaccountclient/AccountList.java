package ghost.bankaccountclient;

import ghost.bankaccountclient.dataservice.DataService;
import ghost.bankaccountclient.observer.Observable;
import ghost.bankaccountclient.observer.Observer;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AccountList extends ListFragment implements Observer {
	
	private DataService mDataService;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ArrayList<String> list = new ArrayList<String>();
		list.add("Value 1");
		list.add("Value 2");
		list.add("Value 3");
		list.add("Value 4");
		
		this.setListAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list));
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
	}

	@Override
	public void update(Observable observable, Object value) {
		if (value instanceof DataService) {
			mDataService = (DataService)value;
		}
	}
	
	
}
