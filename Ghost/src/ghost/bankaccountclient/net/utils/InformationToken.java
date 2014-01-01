package ghost.bankaccountclient.net.utils;

import java.io.Serializable;

public class InformationToken implements Serializable {

	private int packageType;
	private String value;
	
	public InformationToken(int packageType, String value) {
		super();
		this.packageType = packageType;
		this.value = value;
	}
	
	public InformationToken() {
		super();
	}

	public int getPackageType() {
		return packageType;
	}

	public void setPackageType(int packageType) {
		this.packageType = packageType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "InformationToken [packageType=" + packageType + ", value="
				+ value + "]";
	}
	
	
}
