/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bankserv;

import ghost.bankaccountclient.net.utils.InformationToken;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author mth
 */
public class CommandInterpreter {
    
    private JdbcTemplate jdbc;
    
    public CommandInterpreter(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    
    public InformationToken executeObject(InformationToken token) {
        
        int commandCode = token.getPackageType();
        InformationToken t = new InformationToken();
        System.out.println("Otzymana treść zaputania: "+token.getValue()+" :: NUmer "+token.getPackageType());
        switch(commandCode) {
            case PackageInfo.INFO_NUMBER_ACCOUNT:
                
       //         t.setPackageType(PackageInfo.INFO_ACCOUNT_DETAILS);
                Map<String,Object> result = jdbc.queryForMap("select Name,Surname,Balance,AccessDisabled from bankAccounts "
                        + "inner join customers "
                        + "on bankAccounts.CustomerID=customers.CustomerID "
                        + "where AccountNumber='"+token.getValue()+"' and Active=1;");
                
                if (result.isEmpty()) {
                    t.setPackageType(PackageInfo.ACCOUNT_NOT_EXISTS);
                } else if (result.get("AccessDisabled").toString().equals("1") || true) {
                    t.setPackageType(PackageInfo.ACCOUNT_BLOCKED);
                } else {
                    t.setPackageType(PackageInfo.ID_IMEI_REQUEST);  
                }
                break;
            default:
                return null;
        }
        System.out.println("Wysłano: "+t.getValue());
        return t;
    }
}
