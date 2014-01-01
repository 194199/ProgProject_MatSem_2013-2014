/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bankserv;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * @author mth
 */
public class MainThread implements Runnable {
    
    private int port;
    private SSLServerSocket servSocket;
    private SSLContext secutiryContext;

    public MainThread(int port) throws IOException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, CertificateException, UnrecoverableKeyException, NoSuchProviderException {
        this.port = port;
        secutiryContext = SSLContext.getInstance("TLS");
        
        
        
        String keystore = "/home/mth/Certificates/keystores/bankkeystore.jks";
        String truststore = "/home/mth/Certificates/truststores/banktruststore.jks";
        String keystorePass = "MyN3wP455w00rD";
        
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        FileInputStream kis = new FileInputStream(keystore);
        
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(kis, keystorePass.toCharArray());   
        kmf.init(ks, keystorePass.toCharArray());
        
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        FileInputStream tin = new FileInputStream(truststore);
        KeyStore ts = KeyStore.getInstance("JKS");
        ts.load(tin, keystorePass.toCharArray());
        tmf.init(ts);
        
        secutiryContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        
        servSocket = (SSLServerSocket)secutiryContext.getServerSocketFactory().createServerSocket(port);
    }

    @Override
    public void run() {
        ObjectOutputStream out = null;
        try {
            SSLSocket newSocket;
            int count = 0;
            newSocket = (SSLSocket)servSocket.accept();
            System.out.print("Fount");
            out = new ObjectOutputStream(newSocket.getOutputStream());
            System.out.print("Conected");
            out.writeObject("Message number "+(++count));
        } catch (IOException ex) {
            Logger.getLogger(MainThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(MainThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
  
            
            
            
    
    }
    
    
    
}
