/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bankserv;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mth
 */
public class Test {
    
    
    public static void main (String[] args) {
        new Test();
    }       

    public Test() {
        Thread t = new Thread() {

            @Override
            public void run() {
                while (true){
                    System.out.println("N");
                    try {
                        synchronized(this) {
                        wait(300);
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            
        };
        
        t.start();

    }
        
    
    
}
