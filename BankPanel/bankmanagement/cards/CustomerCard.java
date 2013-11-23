/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bankmanagement.cards;

import java.util.Map;
import java.util.Objects;

/**
 *
 * @author mth
 */
public class CustomerCard {
    
    public static CustomerCard createCustomerCard(Map<String,Object> customer) {
        return new CustomerCard(customer);
    }

    public CustomerCard(Map<String,Object> customer) {
        this.customerID = Integer.parseInt(customer.get("customerID").toString());
        this.customerName = customer.get("Name").toString();
        this.customerSurname = customer.get("Surname").toString();
        this.customerAddress = (customer.get("Address") != null ? customer.get("Address").toString() : "");
        this.customerBirthdate = (customer.get("Birthdate") != null ? customer.get("Birthdate").toString() : "");
    }

    public CustomerCard() {
        customerID = -1;
        customerName="Name";
        customerSurname="Surname";
    }    
    
    private int customerID;
    private String customerName;
    private String customerSurname;
    private String customerAddress;
    private String customerBirthdate;

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSurname() {
        return customerSurname;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerBirthdate() {
        return customerBirthdate;
    }

    public void setCustomerBirthdate(String customerBirthdate) {
        this.customerBirthdate = customerBirthdate;
    }

    @Override
    public String toString() {
        return this.customerName+" "+this.customerSurname;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.customerID;
        hash = 59 * hash + Objects.hashCode(this.customerName);
        hash = 59 * hash + Objects.hashCode(this.customerSurname);
        hash = 59 * hash + Objects.hashCode(this.customerAddress);
        hash = 59 * hash + Objects.hashCode(this.customerBirthdate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CustomerCard other = (CustomerCard) obj;
        if (this.customerID != other.customerID) {
            return false;
        }
        return true;
    }
    
    
    
    
}
