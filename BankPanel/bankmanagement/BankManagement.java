/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bankmanagement;

import java.io.InputStream;

/**
 *
 * @author mth
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
public class BankManagement {

    /**
     * @param args the command line arguments
     */
    public static void f(String[] args) {
        Runtime r=Runtime.getRuntime();
        String[] cmds = {"cat","/home/mth/.bashrc"};
        InputStream os;
        try{
            os = r.exec(cmds).getInputStream();
            
            BufferedReader br = new BufferedReader(new InputStreamReader(os));
            
            String s;
            
            while((s=br.readLine()) != null) {
                System.out.println(s);
            }
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
        
        
    }
    
    
}
