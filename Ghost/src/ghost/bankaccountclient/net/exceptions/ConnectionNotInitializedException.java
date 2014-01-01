package ghost.bankaccountclient.net.exceptions;

public class ConnectionNotInitializedException extends Exception {
	public ConnectionNotInitializedException() {super();}
	public ConnectionNotInitializedException(String msg) {
		super(msg);
	}
}
