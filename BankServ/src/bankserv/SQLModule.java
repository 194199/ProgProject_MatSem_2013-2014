/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bankserv;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author mth
 */
public class SQLModule {

    private ApplicationContext context;
    private JdbcTemplate jdbcTemplate;
    
    public SQLModule(ApplicationContext context) {
        this.context = context;
        jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
    }
    
    
    
}
