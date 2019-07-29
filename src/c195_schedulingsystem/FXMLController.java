/*
 * C195 - Scheduling System
 * By Rebecca Birmingham
 * March 2018
 * 
 */
package c195_schedulingsystem;
//All the imports...
import static c195_schedulingsystem.ConnectionInfo.connectToDB;
import static c195_schedulingsystem.CustomerTableView.updateCustomer;
import static c195_schedulingsystem.CustomerTableView.updateTableView;
import static c195_schedulingsystem.AppointmentTableView.updateApptTable;
import static c195_schedulingsystem.CalendarTableView.updateCalTable;
import static c195_schedulingsystem.LoadInitialData.addCities;
import static c195_schedulingsystem.LoadInitialData.addCountry;
import static c195_schedulingsystem.LoadInitialData.addApptOptions;
import static c195_schedulingsystem.LoadInitialData.checkForApptSoon;
import static c195_schedulingsystem.ReportsAndLogsView.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLController implements Initializable {
    
    //Login Screen objects
    @FXML
    private Button btnLogin;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Label lblLoginPlease;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblPassword;
    
    //Main Menu objects
    @FXML
    private RadioButton rbtnReport1;
    @FXML
    private RadioButton rbtnReport2;
    @FXML
    private RadioButton rbtnReport3;
    @FXML
    private Button btnViewReport;
    @FXML
    private RadioButton rbtnMonthlyView;
    @FXML
    private RadioButton rbtnWeeklyView;
    @FXML
    private Label lblCalendarView;
    @FXML
    private TableView calendarTable;
    @FXML
    private Button btnViewLog;
    @FXML
    private Button btnAddCust;
    @FXML
    private Button btnEditCust;
    @FXML
    private Button btnEditAppt;
    
    //Add Customer Menu
    @FXML
    private TextField txtCustName;
    @FXML
    private TextField txtAddyLine1;
    @FXML
    private TextField txtAddyLine2;
    @FXML
    private TextField txtZipCode;
    @FXML
    private TextField txtPhoneNum;
    @FXML
    private Button btnSaveCust;
    @FXML
    private ComboBox<String> ddCountry;
    @FXML
    private ComboBox<String> ddCity;
    
    //Customer Table
    @FXML
    private TableView<Customer> custTable;
    
    //Edit or Delete Customer
    @FXML
    private Button btnDeleteCust;
    @FXML
    private Button btnSelectCust;
    @FXML
    private Button btnUpdateCust;
    
    //Appt Menu
    @FXML
    private ComboBox<String> ddApptType;
    @FXML
    private ComboBox<String> ddStartTime;
    @FXML
    private ComboBox<String> ddEndTime;
    @FXML
    private Button btnUpdateAppt;
    @FXML
    private Button btnSelectAppt;
    @FXML
    private Button btnDeleteAppt;
    @FXML
    private TextArea txtApptDesc;
    @FXML
    private DatePicker dpApptDate;
    @FXML
    private TableView<Appointment> apptTable;
    
    //Reports and Logs menu
    @FXML
    private Label lblReportsAndLogs;
    @FXML
    private TextArea txtReportsAndLogs;
    
    //Lists
    @FXML
    public static ObservableList<String> temp = FXCollections.observableArrayList(); //used for country
    @FXML
    public static ObservableList<String> temp1 = FXCollections.observableArrayList(); //used for USA city
    @FXML
    public static ObservableList<String> temp2 = FXCollections.observableArrayList(); //used for Mexico city
    @FXML
    public static ObservableList<Customer> temp3 = FXCollections.observableArrayList(); //used for customer table
    @FXML
    public static ObservableList<Appointment> temp4 = FXCollections.observableArrayList(); //used for appt table
    @FXML
    public static ObservableList<Calendar> tempCal = FXCollections.observableArrayList(); //used for calendar table
    @FXML
    public static ObservableList<Calendar> tempCal2 = FXCollections.observableArrayList(); //used for calendar table
    @FXML
    public static ObservableList<String> tempApptSoon = FXCollections.observableArrayList(); //used for appts within 15 mins of current time
    @FXML
    public static ObservableList<String> apptTypeList = FXCollections.observableArrayList(); //Appt type list
    @FXML
    public static ObservableList<String> apptStartList = FXCollections.observableArrayList(); //Appt type list
    @FXML
    public static ObservableList<String> apptEndList = FXCollections.observableArrayList(); //Appt type list
    @FXML
    public static ObservableList<String> txtList = FXCollections.observableArrayList(); //used for Reports and Logs
    @FXML
    public static ObservableList<String> errorList = FXCollections.observableArrayList(); //used for logging failed login attempts
    //Other
    @FXML
    public static String userLoggedIn = ""; //used for storing the user logged in to each session
    @FXML
    public static File logFile = new File(System.getProperty("user.dir"), "src/c195_schedulingsystem/ReportsAndLogs/logFile.txt");
    @FXML
    public static int tempId = 0; //used for storing custId being modified - use for appt too?
    @FXML
    public static int tempId2 = 0; //used for storing city being modified
    @FXML
    public static ZoneId currentZoneId = ZoneId.systemDefault(); //current time zone
    @FXML
    public static ZoneId utcZoneId = ZoneId.of("UTC"); //time zone "stored" in DB
    @FXML
    public static Locale originalLocale;
    @FXML
    public static ResourceBundle rb2;
    //Lambda
    @FXML
    public static String alertText;
    @FXML
    public static Runnable alertPopup = (() -> {Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error Information");
                    alert.setHeaderText("Error!");
                    alert.setContentText(alertText);
                    alert.showAndWait();});
    @FXML //used for appts within 15 mins
    public static Runnable infoPopup = (() -> {Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText("Notice!");
                    alert.setContentText(alertText);
                    alert.showAndWait();});
    @FXML
    String selectedQuery;
    @FXML
    Runnable updateQuery = () -> {try ( // try with resources
                            Connection conn = connectToDB();
                            Statement stmt = conn.createStatement();
                            ) {

                            int rs = stmt.executeUpdate(selectedQuery);
                            //System.out.println(rs); //test that it was successful
                            
                        } catch (SQLException | ClassNotFoundException ex) {
                            //Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                            alertText = "Sorry, something went wrong. Please try again later.";
                            alertPopup.run();
                        }};
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
      try { //try catch for null or invalid number input
        
        Stage stage;
        Parent root;
        
        //opens the 'Add Customer' menu
        if(event.getSource() == btnAddCust){
            stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddCustomer.fxml"));
            loader.setController(this); //instance of the fxml loader
            root = (Parent) loader.load();
            stage.setTitle("Add Customer");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btnAddCust.getScene().getWindow());
            
            ddCountry.getItems().clear();
            ddCountry.setItems(temp);
            stage.showAndWait();
        }
        //Save a newly created customer
        else if(event.getSource() == btnSaveCust){
            String custName = txtCustName.getText();
            String custAddy1 = txtAddyLine1.getText();
            String custAddy2 = txtAddyLine2.getText(); //optional, could be blank
            Integer custZipCode = Integer.parseInt(txtZipCode.getText());
            Long custPhone = Long.parseLong(txtPhoneNum.getText());
            String city = ddCity.getSelectionModel().getSelectedItem();
            //System.out.println(custPhone);
            
            if (custName.isEmpty() || custAddy1.isEmpty()) {
                alertText = "Please do not leave any fields other than Address Line 2 blank.";
                alertPopup.run();
            } else if (custName.length() > 45) {
                alertText = "Customer name cannot exceed 45 characters.";
                alertPopup.run();
            } else if (custAddy1.length() > 50 || custAddy2.length() > 50) {
                alertText = "Address Line cannot exceed 50 characters.";
                alertPopup.run();
            } else if (custZipCode >= 99999) {
                alertText = "Postal Code cannot exceed 5 digits.";
                alertPopup.run();
            } else if (custPhone >= 9999999999L) {
                alertText = "Phone Number cannot exceed 10 digits.";
                alertPopup.run();
            }  
            else { //run the query if everything looks good
            
                String query = "INSERT INTO address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy)" + 
                                "SELECT '" + custAddy1 + "', '" + custAddy2 + "', cityId, '" + custZipCode + "', '" + custPhone + "', now(), '" + userLoggedIn + "', now(), '" + userLoggedIn + "' FROM city WHERE city='" + city + "';" +
                                "SET @lastAddyId = LAST_INSERT_ID();" +
                                "INSERT INTO customer(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)" +
                                "VALUES('" + custName + "'," + "@lastAddyId, 1, now(), '" + userLoggedIn + "', now(), '" + userLoggedIn + "');";

                try ( // try with resources
                    Connection conn = connectToDB();
                    Statement stmt = conn.createStatement();
                    ) {

                    int rs = stmt.executeUpdate(query);
                    //System.out.println(rs); //test that it was successful
                    stage = (Stage) btnSaveCust.getScene().getWindow();
                    stage.close();    
                }  
            }
        }
        // open the edit customer menu
        else if (event.getSource() == btnEditCust) {
            stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditCustomer.fxml"));
            loader.setController(this); //instance of the fxml loader
            root = (Parent) loader.load();
            stage.setTitle("Edit Customer");
            stage.setOnCloseRequest(e -> {try { //updates the calendar after closing
                setCountryCityLists();
                } catch (ClassNotFoundException | SQLException | IOException ex) {
                    Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btnEditCust.getScene().getWindow());
            
            tempId = 0; //just in case this is used for calendar Id too
            //Pull and display partial customer info in table view
            updateTableView();
            custTable.setItems(temp3);
                
            stage.showAndWait();    
        }
        //delete a customer (aka set as inactive)
        else if (event.getSource() == btnDeleteCust) {      
            if (!custTable.getSelectionModel().isEmpty()) {
                //create a variable to remove it from the DB too
                int custId = custTable.getSelectionModel().getSelectedItem().getCustId();

                selectedQuery = "UPDATE customer SET active = 0, lastUpdateBy = '" + userLoggedIn + "', lastUpdate = now() WHERE customerId =" + custId + ";";
                //run the lambda
                updateQuery.run();
                //removes the selected customer
                temp3.remove(custTable.getSelectionModel().getSelectedItem());
                //clears the selection
                custTable.getSelectionModel().clearSelection();
                //repopulate the table
                custTable.setItems(temp3);
                    
                } else {
                alertText = "Please select a customer to delete.";
                alertPopup.run();
            }
        }    
        //Select a customer to edit
        else if (event.getSource() == btnSelectCust) {
            int custId = custTable.getSelectionModel().getSelectedItem().getCustId();
            tempId = custId; //store the custId to know which record I am later updating
            
            String query = "SELECT * FROM customer AS c LEFT JOIN address AS a ON (c.addressId = a.addressId) LEFT JOIN city AS ci ON (a.cityId = ci.cityId) LEFT JOIN country AS co ON (ci.countryId = co.countryId) WHERE c.customerId =" + custId + ";";
            
            try ( // try with resources
                Connection conn = connectToDB();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);) {
                
                while (rs.next()) {
                    txtCustName.setText(rs.getString("c.customerName"));
                    txtAddyLine1.setText(rs.getString("a.address"));
                    txtAddyLine2.setText(rs.getString("a.address2"));
                    txtZipCode.setText(rs.getString("a.postalCode"));
                    txtPhoneNum.setText(rs.getString("a.phone"));
                    
                    String country = rs.getString("co.country");
                    
                    tempId2 = rs.getInt("c.addressId");
                    
                    ddCountry.setItems(temp);
                    ddCountry.setValue(country);
                    
                    if (country.equalsIgnoreCase("USA")) {
                        ddCity.setItems(temp1);
                    } else if (country.equalsIgnoreCase("Mexico")) {
                        ddCity.setItems(temp2);
                    }
                    ddCity.setValue(rs.getString("ci.city"));
                    ddCity.setDisable(false);   
                }   
            }    
        }
        //Update the customer that had been selected previous
        else if (event.getSource() == btnUpdateCust) { 
                if (tempId == 0) {
                    alertText = "Please select a customer to edit.";
                    alertPopup.run();
                } else { //else update the record
                    String custName = txtCustName.getText();
                    String custAddy1 = txtAddyLine1.getText();
                    String custAddy2 = txtAddyLine2.getText(); //optional, could be blank
                    Integer custZipCode = Integer.parseInt(txtZipCode.getText());
                    Long custPhone = Long.parseLong(txtPhoneNum.getText());
                    String city = ddCity.getSelectionModel().getSelectedItem();
                    
                    if (custName.isEmpty() || custAddy1.isEmpty()) {
                        alertText = "Please do not leave any fields other than Address Line 2 blank.";
                        alertPopup.run();
                    } else if (custName.length() > 45) {
                        alertText = "Customer name cannot exceed 45 characters.";
                        alertPopup.run();
                    } else if (custAddy1.length() > 50 || custAddy2.length() > 50) {
                        alertText = "Address Line cannot exceed 50 characters.";
                        alertPopup.run();
                    } else if (custZipCode >= 99999) {
                        alertText = "Postal Code cannot exceed 5 digits.";
                        alertPopup.run();
                    } else if (custPhone >= 9999999999L) {
                        alertText = "Phone Number cannot exceed 10 digits.";
                        alertPopup.run();
                    }  
                    else { //run the query if everything looks good
                        updateCustomer(custName, custAddy1, custAddy2, custZipCode, custPhone, city);

                        txtCustName.clear();
                        txtAddyLine1.clear();
                        txtAddyLine2.clear();
                        txtZipCode.clear();
                        txtPhoneNum.clear();

                        ddCountry.setItems(temp);
                        ddCountry.setPromptText("Select a Country");

                        ddCity.setDisable(true);
                        ddCity.setPromptText("Select Country First");

                        //Pull and display partial customer info in table view
                        updateTableView();
                        custTable.setItems(temp3); 
                    }
                }     
        }    
        //Add or edit an appointment menu
        else if (event.getSource() == btnEditAppt) {
            stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditAppointment.fxml"));
            loader.setController(this); //instance of the fxml loader
            root = (Parent) loader.load();
            stage.setTitle("Add, Edit, or Delete Appointments");
            stage.setOnCloseRequest(e -> {try { //updates the calendar after closing
                setCountryCityLists();
                } catch (ClassNotFoundException | SQLException | IOException ex) {
                    Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btnEditAppt.getScene().getWindow());
            
            ddApptType.setItems(apptTypeList);
            ddStartTime.setItems(apptStartList);
            ddEndTime.setItems(apptEndList);
            
            tempId = 0; //just in case this is used for calendar Id too
            tempId2 = 0;
            //Pull and display partial customer info in table view
            updateTableView();
            custTable.setItems(temp3);
            //appt table
            updateApptTable();
            apptTable.setItems(temp4);
            
            stage.showAndWait();  
        } 
        //delete an appointment
        else if (event.getSource() == btnDeleteAppt) {     
            
            if (!apptTable.getSelectionModel().isEmpty()) {

                //create a variable to remove it from the DB too
                int apptId = apptTable.getSelectionModel().getSelectedItem().getApptId();

                selectedQuery = "DELETE FROM appointment WHERE appointmentId = " + apptId + ";";
                //run the lambda
                updateQuery.run();
                //removes the selected customer
                temp4.remove(apptTable.getSelectionModel().getSelectedItem());
                //clears the selection
                apptTable.getSelectionModel().clearSelection();
                //repopulate the table
                apptTable.setItems(temp4);
                    
            } else {
                alertText = "Please select an appointment to delete.";
                alertPopup.run();
            }
        }
        //Save a new or updated appt
        else if (event.getSource() == btnUpdateAppt) {
            //Add or update appt
            if (!custTable.getSelectionModel().isEmpty()) {
                int custId = custTable.getSelectionModel().getSelectedItem().getCustId();

                String apptType = ddApptType.getSelectionModel().getSelectedItem();
                String apptDesc = txtApptDesc.getText();
                LocalDate currentDate = dpApptDate.getValue();
                LocalTime currentStartTime = LocalTime.parse(ddStartTime.getSelectionModel().getSelectedItem());
                LocalTime currentEndTime = LocalTime.parse(ddEndTime.getSelectionModel().getSelectedItem());
                
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter df2 = DateTimeFormatter.ofPattern("HH:mm");
                DateTimeFormatter df3 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                
                ZonedDateTime currentStart = ZonedDateTime.of((LocalDateTime.of(currentDate, currentStartTime)), currentZoneId);
                String apptDate = currentStart.withZoneSameInstant(utcZoneId).format(df);
                String apptStartTime = currentStart.withZoneSameInstant(utcZoneId).format(df2);
                
                ZonedDateTime currentEnd = ZonedDateTime.of((LocalDateTime.of(currentDate, currentEndTime)), currentZoneId);
                String apptDate2 = currentEnd.withZoneSameInstant(utcZoneId).format(df);
                String apptEndTime = currentEnd.withZoneSameInstant(utcZoneId).format(df2);
                
                if (currentStart.isAfter(currentEnd)) {
                    alertText = "The appointment start time must come before the end time.";
                    alertPopup.run();
                } else if (!currentStartTime.isAfter(LocalTime.of(07, 59)) || !currentEndTime.isBefore(LocalTime.of(18, 01))) {
                    alertText = "The appointment time must between business hours (8am - 6pm).";
                    alertPopup.run();
                } else {
                
                    String query = "SELECT a.appointmentId, DATE_FORMAT(a.start, '%Y-%m-%d %H:%i'), DATE_FORMAT(a.end, '%Y-%m-%d %H:%i'), a.customerId FROM appointment AS a " +
                                        " LEFT JOIN customer AS c ON (a.customerId = c.customerId) WHERE c.active = 1;";

                    Boolean overlaps = false;

                    try ( // try with resources
                    Connection conn = connectToDB();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);) {

                    while (rs.next()) {
                        if (Integer.parseInt(rs.getString("a.customerId")) == custId && Integer.parseInt(rs.getString("a.appointmentId")) != tempId) { //only compare if customer matches and it's not the same appt
                            String txtStartTime = rs.getString("DATE_FORMAT(a.start, '%Y-%m-%d %H:%i')");
                            String txtEndTime = rs.getString("DATE_FORMAT(a.end, '%Y-%m-%d %H:%i')");

                            LocalDateTime localStart = LocalDateTime.parse(txtStartTime, df3);
                            LocalDateTime localEnd = LocalDateTime.parse(txtEndTime, df3);

                            ZonedDateTime utcStart = localStart.atZone(utcZoneId);
                            String currentStart2 = utcStart.withZoneSameInstant(currentZoneId).format(df2);
                            String date = utcStart.withZoneSameInstant(currentZoneId).format(df);

                            ZonedDateTime utcEnd = localEnd.atZone(utcZoneId);
                            String currentEnd2 = utcEnd.withZoneSameInstant(currentZoneId).format(df2);

                            LocalTime currentStartTime2 = LocalTime.parse(currentStart2);
                            LocalTime currentEndTime2 = LocalTime.parse(currentEnd2);
                            if (date.equals(apptDate) || date.equals(apptDate2)) { //only compare on the same date
                                if (currentStartTime.equals(currentStartTime2) || currentStartTime.equals(currentEndTime2) || currentEndTime.equals(currentStartTime2) || currentEndTime.equals(currentEndTime2)) {
                                    overlaps = true;
                                } else if (currentStartTime.isAfter(currentStartTime2) && currentStartTime.isBefore(currentEndTime2)) {
                                    overlaps = true;
                                } else if (currentEndTime.isAfter(currentStartTime2) && currentStartTime.isBefore(currentEndTime2)) {
                                    overlaps = true;
                                }
                              }
                            }  
                           }
                        if (overlaps) {
                            alertText = "The appointment cannot overlap with another for the same customer.";
                            alertPopup.run();
                        } else { //all checks are good
                            
                            //Add a new appt
                            if (tempId == 0) { //if tempId is 0, then it is a new appointment
                                selectedQuery = "INSERT into appointment(customerId, title, description, start, end, createDate, createdBy, lastUpdate, lastUpdateBy)" +
                                                " VALUES(" + custId + ", '" + apptType + "', '" + apptDesc + "', '" + apptDate + " " + apptStartTime + ":00', '" + apptDate2 + " " + apptEndTime + ":00', now(), '" + userLoggedIn + "', now(), '" + userLoggedIn + "');";
                                //run the lambda
                                updateQuery.run();

                            } else { // else tempId is not 0 and it is updating an existing appointment

                                selectedQuery = "UPDATE appointment SET " +
                                                "customerId = " + custId + ", title = '" + apptType + "', description = '" + apptDesc + "', start = '" + apptDate + " " + apptStartTime + ":00', end = '" + apptDate2 + " " + apptEndTime + ":00', lastUpdate = now(), lastUpdateBy = '" + userLoggedIn + "' WHERE appointmentId = " + tempId + ";";
                                //run the lambda
                                updateQuery.run();
                            }
                            ddApptType.getSelectionModel().clearSelection();
                            txtApptDesc.clear();
                            dpApptDate.setValue(null);
                            ddStartTime.getSelectionModel().clearSelection();
                            ddEndTime.getSelectionModel().clearSelection();

                            custTable.getSelectionModel().clearSelection();

                            updateApptTable();
                            apptTable.setItems(temp4);

                            tempId = 0;
                            
                        }
                    
                    }
                
                } 
            } else {
                alertText = "Please select a customer.";
                alertPopup.run();
            }
        }
        else if (event.getSource() == btnSelectAppt) {
            int apptId = apptTable.getSelectionModel().getSelectedItem().getApptId();
            tempId = apptId; //store the apptId to know which record I am later updating
            
            //System.out.println(apptId + " " + tempId);
            
            String query = "SELECT * FROM appointment WHERE appointmentId = " + apptId + ";";
            
            try ( // try with resources
                Connection conn = connectToDB();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);) {
                
                while (rs.next()) {
                    ddApptType.setItems(apptTypeList);
                    ddApptType.setValue(rs.getString("title"));
                    txtApptDesc.setText(rs.getString("description"));
                    
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    DateTimeFormatter df2 = DateTimeFormatter.ofPattern("HH:mm");
                    
                    int index1 = rs.getString("start").indexOf(' ');
                    int index2 = rs.getString("end").indexOf(' ');
                    LocalDate date = LocalDate.parse(rs.getString("start").substring(0, index1));
                    LocalTime startTime = LocalTime.parse((rs.getString("start").substring((index1+1), (index1+6))), df2);
                    LocalTime endTime = LocalTime.parse((rs.getString("end").substring((index2+1), (index2+6))), df2);
                    
                    LocalDateTime localStart = LocalDateTime.of(date, startTime);
                    LocalDateTime localEnd = LocalDateTime.of(date, endTime);
                    
                    ZonedDateTime utcStart = localStart.atZone(utcZoneId);
                    String currentStart = utcStart.withZoneSameInstant(currentZoneId).format(df2);
                    //System.out.println(utcStart + " " + currentStart);
                    
                    ZonedDateTime utcEnd = localEnd.atZone(utcZoneId);
                    String currentEnd = utcEnd.withZoneSameInstant(currentZoneId).format(df2);
                    //System.out.println(utcEnd + " " + currentEnd);
                    
                    dpApptDate.setValue(date);
                    
                    ddStartTime.setItems(apptStartList);
                    ddStartTime.setValue(currentStart);
                    ddEndTime.setItems(apptEndList);
                    ddEndTime.setValue(currentEnd);
                    
                    Integer custId = Integer.parseInt(rs.getString("customerId"));
                    int indexCust = -1;
                    int size = temp3.size();
                    
                    for(int index=0; index < size; index++){

                        if(custId.equals(temp3.get(index).getCustId())){

                            indexCust = index;
                       }
                    }
                    //System.out.println(custId + " " + indexCust);
                    
                    custTable.getSelectionModel().clearAndSelect(indexCust);     
                }     
            }
        } 
        //view login log file
        else if (event.getSource() == btnViewLog) {
            stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportsAndLogs.fxml"));
            loader.setController(this); //instance of the fxml loader
            root = (Parent) loader.load();
            stage.setTitle("Login Activity Log");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btnViewLog.getScene().getWindow());
            
            //FileReader reader = new FileReader(logFile);
            Scanner scanner = new Scanner(logFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                txtReportsAndLogs.appendText(line + "\n");
            }            
            stage.showAndWait();    
        }
        //generate and view a report
        else if (event.getSource() == btnViewReport) {
            stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportsAndLogs.fxml"));
            loader.setController(this); //instance of the fxml loader
            root = (Parent) loader.load();
            stage.setTitle("Reports");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btnViewReport.getScene().getWindow());
            
            String query = "";
            txtList.clear();
            LocalDate currentDate = LocalDate.now();
            
            //report 1 - number of appt types by month
            if (rbtnReport1.isSelected()) {
                //pull data and display report
                query = "SELECT COUNT(title), MONTH(start), MONTHNAME(start), title FROM appointment " +
                        "GROUP BY MONTH(start), title;";
                
                runReport1(query);
                //txtList
                for (String s : txtList) {
                    txtReportsAndLogs.appendText(s + "\n");
                }
                
                lblReportsAndLogs.setText("Viewing Number of Appointment Types by Month:");
                
            } 
            //report 2 - the schedule for each user
            else if (rbtnReport2.isSelected()) {
                
                query = "SELECT c.customerName, DATE_FORMAT(a.start, '%y-%m-%d %H:%i'), DATE_FORMAT(a.end, '%y-%m-%d %H:%i'), title FROM appointment AS a LEFT JOIN customer AS c ON (a.customerId = c.customerId) " +
                        "GROUP BY c.customerName, a.start";
                
                runReport2(query);
                
                for (String s : txtList) {
                    txtReportsAndLogs.appendText(s + "\n");
                }
                lblReportsAndLogs.setText("Viewing All Appointments by Customer: ");    
            } 
            //report 3 - number of appt types by week
            else if (rbtnReport3.isSelected()) {
                
                TemporalAdjuster weekAdj = TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY);
                LocalDate startWeek = currentDate.with(weekAdj);
                LocalDate endWeek = startWeek.plusDays(6);
                
                query = "SELECT COUNT(title), title FROM appointment " +
                        "WHERE DATE(start) >= '" + startWeek + "' AND DATE(start) <= '" + endWeek + "' " + 
                        "GROUP BY title;";
                
                runReport3(query);
                
                for (String s : txtList) {
                    txtReportsAndLogs.appendText(s + "\n");
                }
                lblReportsAndLogs.setText("Viewing Number of Appointment Types for Week: " + startWeek + " - " + endWeek);     
            }
            stage.showAndWait();  
        }
        //report 3 - number of appt types by week
        else if (event.getSource() == btnLogin) {
            Boolean correctUsername = false;
            Boolean correctPassword = false;
            
            String userName2 = txtUsername.getText();
            String password2 = txtPassword.getText();
            
            String query = "SELECT userName, password FROM user;";
            
            try ( // try with resources
            Connection conn = connectToDB();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);) {

            while (rs.next()) {  
                    if (rs.getString("userName").equalsIgnoreCase(userName2)) {
                        userLoggedIn = rs.getString("userName");
                        correctUsername = true;
                        if (rs.getString("password").equals(password2)) {
                            correctPassword = true;
                        }
                    }
                }
            }
            //System.out.println(correctUsername + " " + correctPassword);
            //handle input whether it is a bad username or bad password
            if (correctUsername == false) {
                
                errorList.add("Invalid username " + userName2 + " used at " + LocalDateTime.now());
                alertText = rb2.getString("badUsername");
                alertPopup.run();
                
            } else if (correctPassword == false) {
                
                errorList.add("Invalid password for " + userName2 + " attempted at " + LocalDateTime.now());
                alertText = rb2.getString("badPassword");
                alertPopup.run();
                
            } else {
                    //bring up the main menu and create initial data
                    stage = (Stage) btnLogin.getScene().getWindow();
                    stage.close();

                    Locale.setDefault(originalLocale); //setting the locale back since it is only needed for the login screen
                    stage = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("SchedulingSystem.fxml"));
                    loader.setController(this); //instance of the fxml loader
                    root = (Parent) loader.load();
                    stage.setTitle("Scheduling System - Main Menu");
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    setCountryCityLists();
                    stage.show();
                }
            }
        
        }
     catch (NullPointerException e){
            alertText = "Please enter or select valid data.";
            alertPopup.run();
        }
     catch (NumberFormatException e){
            alertText = "Please enter valid numbers.";
            alertPopup.run();
        }
    }  
    
    @FXML
    private void handleCityCountyAction(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
        //display correct cities plus change prompt text for ddCity
        if (!ddCountry.getSelectionModel().isEmpty()) { //ensuring there is a country selected
            String countrySelected = ddCountry.getSelectionModel().getSelectedItem();
            if (countrySelected.equalsIgnoreCase("USA")) {
                ddCity.setItems(temp1);
            } else if (countrySelected.equalsIgnoreCase("Mexico")) {
                ddCity.setItems(temp2);
            }
                ddCity.setDisable(false);
                ddCity.setPromptText("Select a City");

            }
        }
    
    @FXML
    private void handleCalendarAction(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
        
        LocalDate currentDate = LocalDate.now();
        tempCal.clear();
        tempCal2.clear();
        
        String query = "";
        
        if (event.getSource() == rbtnWeeklyView) {
            
            TemporalAdjuster weekAdj = TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY);
            LocalDate startWeek = currentDate.with(weekAdj);
            LocalDate endWeek = startWeek.plusDays(6);
            
            query = "SELECT a.title, DAY(a.start), DATE_FORMAT(a.start, '%y-%m-%d %H:%i'), DATE_FORMAT(a.end, '%y-%m-%d %H:%i'), c.customerName FROM appointment AS a INNER JOIN customer AS c ON (a.customerId = c.customerId) " +
                         "WHERE DATE(a.start) >= '" + startWeek + "' AND DATE(a.start) <= '" + endWeek + "' AND c.active = 1;";
            
            updateCalTable(query);

            for (int day = 0; day <= 6; day++) {
             LocalDate eachDate = startWeek.plusDays(day);
             String eachDateDay = String.valueOf(eachDate.getDayOfMonth());
             String eachDay = String.valueOf(eachDate.getDayOfWeek());
             String appts = "";
             for (Calendar c : tempCal) {
                 //System.out.println(c.getDayCol() + " " + eachDateDay);
                 if (c.getDayCol().equals(eachDateDay)) {
                     appts += (" " + c.getApptsCol() + " |");
                 }
              }
             tempCal2.add(new Calendar(eachDay, appts));
            }
            
            lblCalendarView.setText("Current week: " + startWeek + " - " + endWeek);
            
        } else {
            
            query = "SELECT a.title, DAY(a.start), DATE_FORMAT(a.start, '%y-%m-%d %H:%i'), DATE_FORMAT(a.end, '%y-%m-%d %H:%i'), c.customerName FROM appointment AS a INNER JOIN customer AS c ON (a.customerId = c.customerId) " +
                         "WHERE YEAR(a.start) = YEAR(CURRENT_DATE()) AND MONTH(a.start) = MONTH(CURRENT_DATE()) AND c.active = 1;";
            
            updateCalTable(query);
            
            YearMonth yearMonthObj = YearMonth.of(currentDate.getYear(), currentDate.getMonth());
            int daysInMonth = yearMonthObj.lengthOfMonth();

            for (int day = 1; day <= daysInMonth; day++) {
             String eachDay = String.valueOf(day);
             String appts = "";
             for (Calendar c : tempCal) {
                 if (c.getDayCol().equals(eachDay)) {
                     appts += (" " + c.getApptsCol() + " |");
                 }
              }
             tempCal2.add(new Calendar(eachDay, appts));
            }
            
            lblCalendarView.setText("Viewing the current month: " + currentDate.getMonth() + " " + currentDate.getYear());
        }
        
        calendarTable.setItems(tempCal2);
        
    }
            
    @FXML
    private void setCountryCityLists() throws ClassNotFoundException, SQLException, IOException {
                
        if (temp.isEmpty() || temp1.isEmpty() || temp2.isEmpty()) { //so this will not run when appt/cust windows close
            //pull countries from DB and put in list
            addCountry();
            //pull cities from DB and put into lists
            addCities();
            //add appt types and time options
            addApptOptions();

            //create log file, if it doesn't exist
           logFile.createNewFile();
           try ( //Write to the text file - need to also record failed login attempts
                    FileWriter writer = new FileWriter(logFile, true)) {
                if (!errorList.isEmpty()) { //failed login attempts
                    for (String s : errorList) {
                        writer.write(s + "\n");
                    }
                }    writer.write("User " + userLoggedIn + " logged in successfully at " + LocalDateTime.now() + "\n");
                //System.getProperty("user.dir");
                //System.out.println(logFile.getAbsolutePath());
                //System.out.println(System.getProperty("user.dir"));
            }
           //check for appts that are within 15 mins after everything else and create popup alert
           checkForApptSoon();
           if (!tempApptSoon.isEmpty()) {
               alertText = "The following appointment(s) start within the next 15 minutes:" + "\n";
               for (String s : tempApptSoon) {
                   alertText += (s + "\n");
               }
               infoPopup.run();
           }
        }
        
        //set the initial monthly calendar view
        tempCal.clear();
        tempCal2.clear();
        
        lblCalendarView.setText("Viewing the current month: " + LocalDate.now().getMonth() + " " + LocalDate.now().getYear());
        
        String query3 = "SELECT a.title, DAY(a.start), DATE_FORMAT(a.start, '%y-%m-%d %H:%i'), DATE_FORMAT(a.end, '%y-%m-%d %H:%i'), c.customerName FROM appointment AS a INNER JOIN customer AS c ON (a.customerId = c.customerId) " +
                         "WHERE YEAR(a.start) = YEAR(CURRENT_DATE()) AND MONTH(a.start) = MONTH(CURRENT_DATE()) AND c.active = 1;";

        updateCalTable(query3);
        
       //loop through the data to create a full calendar
       YearMonth yearMonthObj = YearMonth.of(LocalDate.now().getYear(), LocalDate.now().getMonth());
       int daysInMonth = yearMonthObj.lengthOfMonth();
       
       for (int day = 1; day <= daysInMonth; day++) {
        String eachDay = String.valueOf(day);
        String appts = "";
        for (Calendar c : tempCal) {
            if (c.getDayCol().equals(eachDay)) {
                appts += (" " + c.getApptsCol() + " |");
            }
         }
        tempCal2.add(new Calendar(eachDay, appts));
       }
       calendarTable.setItems(tempCal2); 
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (temp.isEmpty() || temp1.isEmpty() || temp2.isEmpty()) {
            //System.out.println(Locale.getDefault());
            originalLocale = Locale.getDefault(); //save the original Locale to revert back to after login
            Locale.setDefault(new Locale("en", "EN-US")); //other supported language is "es", "ES-MX"
            rb2 = ResourceBundle.getBundle("c195_schedulingsystem/LanguageFiles/rb");
            //System.out.println(Locale.getDefault());
            
            lblLoginPlease.setText(rb2.getString("loginPlease"));
            lblUsername.setText(rb2.getString("username"));
            lblPassword.setText(rb2.getString("password"));
            btnLogin.setText(rb2.getString("loginButton"));
        }
    }    
    
}
