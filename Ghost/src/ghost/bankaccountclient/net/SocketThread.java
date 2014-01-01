package ghost.bankaccountclient.net;

import ghost.bankaccountclient.net.utils.InformationToken;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocket;

public class SocketThread extends Thread {

	private SSLSocket socket;
	private ObjectInputStream oIn;
	private ObjectOutputStream oOut;

	public SocketThread(SSLSocket socket) throws IOException {
		super();
		this.socket = socket;
		
	}
	
	@Override
	public void run() {
		try {
			initStream(socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initStream(SSLSocket socket) throws IOException {
		oOut = new ObjectOutputStream(socket.getOutputStream());
		oIn = new ObjectInputStream(socket.getInputStream());
	}
	
	public Object sendRequest(String value, int order) throws IOException, ClassNotFoundException {
		while (oOut == null || oIn == null) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {}
		}
		oOut.writeObject(new InformationToken(order,value));
		Object o = oIn.readObject();
		return o;
	}
	
	public void closeStreams() throws IOException {
		oOut.close();
		oIn.close();
	}
	
}
