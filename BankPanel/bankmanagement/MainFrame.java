/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bankmanagement;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author mth
 */
public interface MainFrame {
    public JdbcTemplate getJdbcTemplate();
    public AbstractApplicationContext getContext();
}
