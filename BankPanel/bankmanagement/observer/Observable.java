/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bankmanagement.observer;

/**
 *
 * @author mth
 */
public interface Observable {
    public void addObserver(Observer observer);
    public void deleteObserver(Observer observer);
    public void notify(Object args);
}
