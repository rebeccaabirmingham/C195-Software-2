/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195_schedulingsystem;

import static c195_schedulingsystem.ConnectionInfo.connectToDB;
import static c195_schedulingsystem.FXMLController.apptEndList;
import static c195_schedulingsystem.FXMLController.apptStartList;
import static c195_schedulingsystem.FXMLController.apptTypeList;
import static c195_schedulingsystem.FXMLController.currentZoneId;
import static c195_schedulingsystem.FXMLController.temp;
import static c195_schedulingsystem.FXMLController.temp1;
import static c195_schedulingsystem.FXMLController.temp2;
import static c195_schedulingsystem.FXMLController.tempApptSoon;
import static c195_schedulingsystem.FXMLController.tempCal;
import static c195_schedulingsystem.FXMLController.utcZoneId;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 *
 * @author rbirmi001c
 */
public class LoadInitialData {
    
    public static void addCountry () throws IOException, ClassNotFoundException, SQLException {
        
        //Pull and display Country
        String query = "SELECT country FROM country;";

        //temp.clear();

        try ( // try with resources
            Connection conn = connectToDB();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);) {

            while (rs.next()) {
                System.out.println(rs.getString("country"));
                temp.add(rs.getString("country"));

            }
        }
        
    }
    
    public static void addCities () throws ClassNotFoundException, SQLException {
        
        String query1 = "SELECT city FROM city WHERE countryId = 1;";

        //temp.clear();

        try ( // try with resources
            Connection conn = connectToDB();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query1);) {

            while (rs.next()) {
                //System.out.println(rs.getString("country"));
                temp1.add(rs.getString("city"));

            }
        }
        
        String query2 = "SELECT city FROM city WHERE countryId = 2;";

        //temp.clear();

        try ( // try with resources
            Connection conn = connectToDB();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query2);) {

            while (rs.next()) {
                //System.out.println(rs.getString("country"));
                temp2.add(rs.getString("city"));

            }
        }
        
    }
    
    public static void addApptOptions () {
        
        //Add appt types
        apptTypeList.add("Initial Consult");
        apptTypeList.add("Project Phase 1");
        apptTypeList.add("Project Phase 2");
        apptTypeList.add("Project Phase 3");
        apptTypeList.add("Project Phase 4");
        apptTypeList.add("Project Phase 5");
        apptTypeList.add("Project Followup");
        
        //Add appt start times
        apptStartList.add("08:00");
        apptStartList.add("08:30");
        apptStartList.add("09:00");
        apptStartList.add("09:30");
        apptStartList.add("10:00");
        apptStartList.add("10:30");
        apptStartList.add("11:00");
        apptStartList.add("11:30");
        apptStartList.add("12:00");
        apptStartList.add("12:30");
        apptStartList.add("13:00");
        apptStartList.add("13:30");
        apptStartList.add("14:00");
        apptStartList.add("14:30");
        apptStartList.add("15:00");
        apptStartList.add("15:30");
        apptStartList.add("16:00");
        apptStartList.add("16:30");
        apptStartList.add("17:00");
        apptStartList.add("17:30");
        
        //Add appt end times
        apptEndList.add("08:30");
        apptEndList.add("09:00");
        apptEndList.add("09:30");
        apptEndList.add("10:00");
        apptEndList.add("10:30");
        apptEndList.add("11:00");
        apptEndList.add("11:30");
        apptEndList.add("12:00");
        apptEndList.add("12:30");
        apptEndList.add("13:00");
        apptEndList.add("13:30");
        apptEndList.add("14:00");
        apptEndList.add("14:30");
        apptEndList.add("15:00");
        apptEndList.add("15:30");
        apptEndList.add("16:00");
        apptEndList.add("16:30");
        apptEndList.add("17:00");
        apptEndList.add("17:30");
        apptEndList.add("18:00");
        
    }
    
    public static void checkForApptSoon () throws ClassNotFoundException, SQLException {
        String query = "SELECT a.title, DATE_FORMAT(a.start, '%Y-%m-%d %H:%i'), DATE_FORMAT(a.end, '%Y-%m-%d %H:%i'), c.customerName FROM appointment AS a INNER JOIN customer AS c ON (a.customerId = c.customerId) " +
                         "WHERE YEAR(a.start) = YEAR(CURRENT_DATE()) AND MONTH(a.start) = MONTH(CURRENT_DATE()) AND c.active = 1;";
        
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter df2 = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter df3 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        
        String currentDate = LocalDate.now().format(df3);
        LocalDateTime currentTime = LocalDateTime.now();
        
        try ( // try with resources
            Connection conn = connectToDB();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);) {

            while (rs.next()) {
                //System.out.println(rs.getString("DAY(a.start)"));
                //System.out.println(rs.getString("TIME_FORMAT(a.start, '%H:%i')"));
                
                
                String txtStartTime = rs.getString("DATE_FORMAT(a.start, '%Y-%m-%d %H:%i')");
                String txtEndTime = rs.getString("DATE_FORMAT(a.end, '%Y-%m-%d %H:%i')");
                
                LocalDateTime localStart = LocalDateTime.parse(txtStartTime, df);
                LocalDateTime localEnd = LocalDateTime.parse(txtEndTime, df);
                
                
                ZonedDateTime utcStart = localStart.atZone(utcZoneId);
                LocalDateTime fullStart = utcStart.withZoneSameInstant(currentZoneId).toLocalDateTime();
                String currentStart = utcStart.withZoneSameInstant(currentZoneId).format(df2);
                String date = utcStart.withZoneSameInstant(currentZoneId).format(df3);
                
                ZonedDateTime utcEnd = localEnd.atZone(utcZoneId);
                String currentEnd = utcEnd.withZoneSameInstant(currentZoneId).format(df2);
                if (date.equals(currentDate)) {
                    if (((fullStart.minusMinutes(15)).equals(currentTime) || (fullStart.minusMinutes(15)).isBefore(currentTime)) && fullStart.isAfter(currentTime)) { //check if start time is within 15 mins but also after current time
                        String allApptsSoon = rs.getString("a.title") + " with " + rs.getString("c.customerName") + " from " + currentStart + " to " + currentEnd + " " + currentZoneId.getDisplayName(TextStyle.SHORT, Locale.getDefault());
                        tempApptSoon.add(allApptsSoon);
                   }
                }
                
                
                

            }
        }
    
    
    
    }
    
}
