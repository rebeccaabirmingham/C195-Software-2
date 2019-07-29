/*
 * C195 - Scheduling System
 * By Rebecca Birmingham
 * March 2018
 */
package c195_schedulingsystem;

import static c195_schedulingsystem.ConnectionInfo.connectToDB;
import static c195_schedulingsystem.FXMLController.currentZoneId;
import static c195_schedulingsystem.FXMLController.temp4;
import static c195_schedulingsystem.FXMLController.utcZoneId;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class AppointmentTableView {
    
    public static void updateApptTable () throws IOException, ClassNotFoundException, SQLException {
        
        //Pull and display partial customer info in table view
                    String query = "SELECT a.appointmentId, a.title, DATE_FORMAT(a.start, '%y-%m-%d %H:%i'), DATE_FORMAT(a.end, '%y-%m-%d %H:%i') FROM appointment AS a " +
                                    " LEFT JOIN customer AS c ON (a.customerId = c.customerId) WHERE c.active = 1;"; //only display appts for active customers

                    temp4.clear();

                    try ( // try with resources
                        Connection conn = connectToDB();
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(query);) {

                        while (rs.next()) {
                            DateTimeFormatter df = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");
                            DateTimeFormatter df2 = DateTimeFormatter.ofPattern("HH:mm");
                            DateTimeFormatter df3 = DateTimeFormatter.ofPattern("yy-MM-dd");
                            
                            System.out.println(rs.getString("a.appointmentId"));

                            String txtStartTime = rs.getString("DATE_FORMAT(a.start, '%y-%m-%d %H:%i')");
                            String txtEndTime = rs.getString("DATE_FORMAT(a.end, '%y-%m-%d %H:%i')");

                            LocalDateTime localStart = LocalDateTime.parse(txtStartTime, df);
                            LocalDateTime localEnd = LocalDateTime.parse(txtEndTime, df);

                            ZonedDateTime utcStart = localStart.atZone(utcZoneId);
                            String currentStart = utcStart.withZoneSameInstant(currentZoneId).format(df2);
                            String date = utcStart.withZoneSameInstant(currentZoneId).format(df3);
                            System.out.println(utcStart + " " + currentStart);

                            ZonedDateTime utcEnd = localEnd.atZone(utcZoneId);
                            String currentEnd = utcEnd.withZoneSameInstant(currentZoneId).format(df2);
                            System.out.println(utcEnd + " " + currentEnd);
                            
                            String apptTime = date + " " + currentStart + " - " + currentEnd + " " + currentZoneId.getDisplayName(TextStyle.SHORT, Locale.getDefault());
                            
                            temp4.add(new Appointment (Integer.parseInt(rs.getString("a.appointmentId")), rs.getString("a.title"), apptTime));
                            
                        }

                    }
        
    }
    
}
