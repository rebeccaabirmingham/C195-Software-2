/*
ConnectionInfo String
Connection information removed for security reasons
Server name:  
Database name:  
Username:  
Password:  
 */
package c195_schedulingsystem;

import static c195_schedulingsystem.FXMLController.alertPopup;
import static c195_schedulingsystem.FXMLController.alertText;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionInfo {
    
    public static Connection connectToDB() throws ClassNotFoundException {
        Connection conn = null;
        String driver = "com.mysql.jdbc.Driver";
        String db = "";
        String allowVars = "?allowMultiQueries=true";
        String url = "" + db + allowVars;
        String user = "";
        String pass = "";
        try {
        Class.forName(driver);
        conn = DriverManager.getConnection(url,user,pass);
        System.out.println("Connected to database : " + db); //comment this out later
        } catch (SQLException e) {
        //System.out.println("SQLException: "+e.getMessage());
        //System.out.println("SQLState: "+e.getSQLState());
        //System.out.println("VendorError: "+e.getErrorCode());
            alertText = "Sorry, something went wrong with the connection. Please try again later.";
            alertPopup.run();
        }
        
        return conn;
    }
    
}
