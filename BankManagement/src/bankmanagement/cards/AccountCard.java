/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bankmanagement.cards;

import java.util.Map;

/**
 *
 * @author mth
 */
public class AccountCard {
    
    private int accountID;
    private int customerID;
    private String accountNumber;
    private float balance;
    private boolean accessDisabled;
    private boolean confirmTransfer;

    public AccountCard() {
        accountID = -1;
    }

    public AccountCard(Map<String,Object> account) {
        this.accountID = Integer.parseInt(account.get("AccountID").toString());
        this.customerID = Integer.parseInt(account.get("CustomerID").toString());
        this.accountNumber = account.get("AccountNumber").toString();
        this.balance = Float.parseFloat(account.get("Balance").toString());
        this.accessDisabled = (Integer.parseInt(account.get("AccessDisabled").toString()) > 0 ? true : false);
        this.confirmTransfer = (Integer.parseInt(account.get("TransferConfirm").toString()) > 0 ? true : false);
    }
    
    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public boolean isAccessDisabled() {
        return accessDisabled;
    }

    public void setAccessDisabled(boolean accessDisabled) {
        this.accessDisabled = accessDisabled;
    }

    public boolean isConfirmTransfer() {
        return confirmTransfer;
    }

    public void setConfirmTransfer(boolean confirmTransfer) {
        this.confirmTransfer = confirmTransfer;
    }

    @Override
    public String toString() {
        return this.accountNumber;
    }
    
    
    
}
