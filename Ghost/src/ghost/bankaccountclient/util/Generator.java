package ghost.bankaccountclient.util;

import ghost.bankaccountclient.db.AccountRow;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

public class Generator extends OrmLiteConfigUtil {
	public static void main(String[] args) throws Exception {
		writeConfigFile("ormlite_config.txt", new Class[] {AccountRow.class});
	}
}
