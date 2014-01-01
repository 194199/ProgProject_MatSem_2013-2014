/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bankserv;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mth
 */
public class BankServ {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            MainServerThread m = new MainServerThread(1357);
            new Thread(m).start();
        } catch (IOException ex) {
            Logger.getLogger(BankServ.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(BankServ.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
