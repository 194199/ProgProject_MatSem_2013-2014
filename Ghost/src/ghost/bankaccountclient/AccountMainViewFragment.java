package ghost.bankaccountclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AccountMainViewFragment extends Fragment {
	
	public static final String ACCOUNT_DATA_INFO = AccountMainViewFragment.class.getSimpleName()+".ACCOUNT_INFO";
	
	public static final String ACCOUNT_NOT_EXIST_ERR = AccountMainViewFragment.class.getSimpleName()+".ACCOUNT_NOT_FOUND";
	public static final String ACCOUNT_REMOTE_BLOCKED = AccountMainViewFragment.class.getSimpleName()+".ACCOUNT_BLOCKED"; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		int content = 0;
		if (getArguments().getBoolean(ACCOUNT_REMOTE_BLOCKED, false))
			content = R.string.dialog_content_account_blocked;
		
		if (getArguments().getBoolean(ACCOUNT_NOT_EXIST_ERR, false))
			content = R.string.dialog_content_account_not_exists;
		
		if (content != 0) {
			final AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
			alert.setTitle(R.string.dialog_title_error);
			alert.setMessage(getActivity().getText(content));
			alert.setButton(DialogInterface.BUTTON_NEUTRAL, getActivity().getText(R.string.button_ok), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					alert.dismiss();
					getActivity().finish();
				}
			});
			alert.show();
			return super.onCreateView(inflater, container, savedInstanceState);
		}
		
		return inflater.inflate(R.layout.account_main_view, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		if (getArguments().getBoolean(ACCOUNT_NOT_EXIST_ERR, false) ||
				getArguments().getBoolean(ACCOUNT_REMOTE_BLOCKED, false)) 
			return;
		
		String[] content = this.getArguments().getStringArray(ACCOUNT_DATA_INFO);
		
		if (content != null) {
			TextView
				nameV = (TextView)this.getActivity().findViewById(R.id.account_owner_field),
				numberV = (TextView)this.getActivity().findViewById(R.id.account_number_field),
				balanceV = (TextView)this.getActivity().findViewById(R.id.account_balance_field);
		
			char[] temp = content[1].toCharArray();
			String tempS = "";
			int k = temp.length-1;
			for (int i=k+1; i>0; i--) {
				if ((i & 3) == 0) {
					tempS += " ";
				}
				tempS += temp[k-i+1];
			}
			nameV.setText(content[0]);
			numberV.setText(tempS);
			balanceV.setText(content[2]);
		}
	}

	
	
	
}
