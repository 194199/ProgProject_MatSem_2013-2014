/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bankserv;

import bankserv.observer.Observable;
import bankserv.observer.Observer;
import ghost.bankaccountclient.net.utils.InformationToken;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author mth
 */
public class ServerConnectionHandler extends Thread implements Observer {
    private SSLSocket socket;
    
    private ObjectInputStream oIn;
    private ObjectOutputStream oOut;
    
    private CommandInterpreter interpreter;

    public ServerConnectionHandler(SSLSocket socket, CommandInterpreter interpreter) throws IOException {
        this.socket = socket;
        this.interpreter = interpreter;
        
        oIn = new ObjectInputStream(socket.getInputStream());
        oOut = new ObjectOutputStream(socket.getOutputStream());
    }
    
    @Override
    public void run() {
        InformationToken token;
        try {
            while (!isInterrupted()) {
                try {
                    token = (InformationToken)oIn.readObject();
                } catch (ClassNotFoundException ex) {
                    continue;
                }
                
                oOut.writeObject(interpreter.executeObject(token));
            }   
        } catch (IOException ex) {
            //to implements
        }  
    }

    @Override
    public void update(Observable observable, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
