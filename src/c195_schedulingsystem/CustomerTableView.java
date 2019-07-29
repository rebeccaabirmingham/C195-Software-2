/*
 * C195 - Scheduling System
 * By Rebecca Birmingham
 * March 2018
 */
package c195_schedulingsystem;

import static c195_schedulingsystem.ConnectionInfo.connectToDB;
import static c195_schedulingsystem.FXMLController.temp3;
import static c195_schedulingsystem.FXMLController.tempId;
import static c195_schedulingsystem.FXMLController.tempId2;
import static c195_schedulingsystem.FXMLController.userLoggedIn;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerTableView {
    
    public static void updateTableView () throws IOException, ClassNotFoundException, SQLException {
        
        //Pull and display partial customer info in table view
                    String query = "SELECT c.customerId, c.customerName, a.address FROM customer AS c INNER JOIN address AS a where c.addressId = a.addressId AND c.active = 1;";

                    temp3.clear();

                    try ( // try with resources
                        Connection conn = connectToDB();
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(query);) {

                        while (rs.next()) {
                            System.out.println(rs.getString("c.customerId"));
                            temp3.add(new Customer (Integer.parseInt(rs.getString("c.customerId")), rs.getString("c.customerName"), rs.getString("a.address")));
                            //System.out.println(temp.toString());

                        }

                        //custTable.setItems(temp3);
                    }
        
    }
    
    public static void updateCustomer (String custName, String custAddy1, String custAddy2, Integer custZipCode, Long custPhone, String city) throws IOException, ClassNotFoundException, SQLException { 
        
        

                String query = "SET @thisCityId = (SELECT cityId FROM city WHERE city = '" + city + "');" +
                                "UPDATE address " + 
                                "SET address = '" + custAddy1 + "', address2 = '" + custAddy2 + "', cityId = @thisCityId, postalCode = '" + custZipCode + "', phone = '" + custPhone + "', lastUpdate = now(), lastUpdateBy = '" + userLoggedIn + "' WHERE addressId = " + tempId2 + ";" + 
                                "UPDATE customer " +
                                "SET customerName = '" + custName + "', lastUpdate = now(), lastUpdateBy = '" + userLoggedIn + "' WHERE customerId = " + tempId + ";";

                try ( // try with resources
                    Connection conn = connectToDB();
                    Statement stmt = conn.createStatement();
                    ) {

                    int rs = stmt.executeUpdate(query);
                    System.out.println(rs); //test that it was successful
                    tempId = 0; //set tempId back to 0, clear selections, and reload custTable
                    tempId2 = 0;
                    
                }
        
    }
    
}
