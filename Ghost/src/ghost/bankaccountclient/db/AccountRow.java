package ghost.bankaccountclient.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "accounts")
public class AccountRow {
	
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(canBeNull = false)
	private String accountNumber;
	
	@DatabaseField(canBeNull = false)
	private String ipAddress;
	
	@DatabaseField(canBeNull = false)
	private int port;
	
	@DatabaseField
	private String pattern;

	public AccountRow() {
		super();
	}

	public AccountRow(String accountNumber) {
		super();
		this.accountNumber = accountNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	
}
