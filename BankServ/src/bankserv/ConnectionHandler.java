/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bankserv;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mth
 */
public class ConnectionHandler implements Runnable {
    
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private volatile long endTime;
    private volatile boolean interrupted;
    private volatile List queue;
    
    private QueryHandler queryHandler;

    public ConnectionHandler(Socket socket) throws IOException {
        this.socket = socket;
        endTime = System.currentTimeMillis()+1800000;
        interrupted = false;
        
        queue = Collections.synchronizedList(new LinkedList());
        
        queryHandler = new QueryHandler();
        
        new Thread() {
            public void run() {
                try {
                    while (System.currentTimeMillis() < endTime && !interrupted) {
                        synchronized(this) {
                            wait(60000);
                        }
                    }
                    
                    ConnectionHandler.this.in.close();
                    ConnectionHandler.this.out.close();
                } catch (InterruptedException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }
    
    

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            
            while (true) {
                try {
                    queue.add(in.readObject());
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        
    }
    
    private synchronized void insertToQueue(Object val) {
        queue.add(val);
        try {
            synchronized(this) {
                queryHandler.notify();
            }
        } catch (Throwable ex) {}
    }
    
    
    
    
    
    
    
    private class QueryHandler extends Thread {
        
        public void run() {
            while (!interrupted) {
                while (queue.isEmpty()) {
                    try {
                        synchronized(this) {
                            this.wait(5000);
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                
                
            }
        }
    }
    
}
