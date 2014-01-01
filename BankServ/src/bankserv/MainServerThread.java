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
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author mth
 */
public class MainServerThread implements Runnable {
    
    private int port;
    private SSLServerSocket servSocket;
    private SSLContext secutiryContext;

    public MainServerThread(int port) throws IOException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, CertificateException, UnrecoverableKeyException, NoSuchProviderException {
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
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        
        SSLSocket socket = null;
        CommandInterpreter interpreter = new CommandInterpreter(jdbcTemplate);
        while (true) {
            try {
                socket = (SSLSocket)servSocket.accept();
                System.out.println("Przyjęto");
                new ServerConnectionHandler(socket, interpreter).start();
            } catch (IOException ex) {
                Logger.getLogger(MainServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
            
            
    
    }
    
    
    
}
