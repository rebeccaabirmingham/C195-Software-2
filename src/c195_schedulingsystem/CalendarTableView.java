/*
 * C195 - Scheduling System
 * By Rebecca Birmingham
 * March 2018
 */
package c195_schedulingsystem;

import static c195_schedulingsystem.ConnectionInfo.connectToDB;
import static c195_schedulingsystem.FXMLController.currentZoneId;
import static c195_schedulingsystem.FXMLController.tempCal;
import static c195_schedulingsystem.FXMLController.utcZoneId;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 *
 * @author rbirmi001c
 */
public class CalendarTableView {
    
    public static void updateCalTable (String query) throws IOException, ClassNotFoundException, SQLException {
        
         try ( // try with resources
            Connection conn = connectToDB();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);) {

            while (rs.next()) {
                System.out.println(rs.getString("DAY(a.start)"));
                //System.out.println(rs.getString("TIME_FORMAT(a.start, '%H:%i')"));
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");
                DateTimeFormatter df2 = DateTimeFormatter.ofPattern("HH:mm"); 
                
                String txtStartTime = rs.getString("DATE_FORMAT(a.start, '%y-%m-%d %H:%i')");
                String txtEndTime = rs.getString("DATE_FORMAT(a.end, '%y-%m-%d %H:%i')");
                
                LocalDateTime localStart = LocalDateTime.parse(txtStartTime, df);
                LocalDateTime localEnd = LocalDateTime.parse(txtEndTime, df);
                
                
                ZonedDateTime utcStart = localStart.atZone(utcZoneId);
                String currentStart = utcStart.withZoneSameInstant(currentZoneId).format(df2);
                System.out.println(utcStart + " " + currentStart);
                
                ZonedDateTime utcEnd = localEnd.atZone(utcZoneId);
                String currentEnd = utcEnd.withZoneSameInstant(currentZoneId).format(df2);
                System.out.println(utcEnd + " " + currentEnd);
                
                
                String allApptsForDay = rs.getString("a.title") + " with " + rs.getString("c.customerName") + " from " + currentStart + " to " + currentEnd + " " + currentZoneId.getDisplayName(TextStyle.SHORT, Locale.getDefault());
                tempCal.add(new Calendar(rs.getString("DAY(a.start)"), allApptsForDay));

            }
        }
        
    }
    
}
