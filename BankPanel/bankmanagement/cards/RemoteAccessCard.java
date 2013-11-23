/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bankmanagement.cards;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author mth
 */
public class RemoteAccessCard {
       
    private int remoteAccessID;
    private int accountID;
    private String confidentialKey = "";
    private String aosid;
    private String imei;

    public RemoteAccessCard() {
        Random rand = new Random();
        
        for (int i=0; i<8; i++) {
            confidentialKey += ""+rand.nextInt(10);
        }
        remoteAccessID = -1;
    }

    public RemoteAccessCard(Map<String,Object> rac) {
        remoteAccessID = Integer.parseInt(rac.get("RemoteAccessID").toString());
        accountID = Integer.parseInt(rac.get("AccountID").toString());
        confidentialKey = rac.get("ConfidentialKey").toString();
        aosid = (rac.get("ClientAOSID") != null ? rac.get("ClientAOSID").toString() : "");
        imei = (rac.get("ClientIMEI") != null ? rac.get("AclientIMEI").toString() : "");
    }
    
    public int getRemoteAccessID() {
        return remoteAccessID;
    }

    public void setRemoteAccessID(int remoteAccessID) {
        this.remoteAccessID = remoteAccessID;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getConfidentialKey() {
        return confidentialKey;
    }

    public void setConfidentialKey(String confidentialKey) {
        this.confidentialKey = confidentialKey;
    }

    public String getAosid() {
        return aosid;
    }

    public void setAosid(String aosid) {
        this.aosid = aosid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.remoteAccessID;
        hash = 29 * hash + this.accountID;
        hash = 29 * hash + Objects.hashCode(this.confidentialKey);
        hash = 29 * hash + Objects.hashCode(this.aosid);
        hash = 29 * hash + Objects.hashCode(this.imei);
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
        final RemoteAccessCard other = (RemoteAccessCard) obj;
        if (this.remoteAccessID != other.remoteAccessID) {
            return false;
        }
        if (!Objects.equals(this.confidentialKey, other.confidentialKey)) {
            return false;
        }
        return true;
    }

    
    

    @Override
    public String toString() {
        return confidentialKey;
    }
}
