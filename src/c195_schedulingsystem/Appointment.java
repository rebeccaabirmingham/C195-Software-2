/*
 * C195 - Scheduling System
 * By Rebecca Birmingham
 * March 2018
 */
package c195_schedulingsystem;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author rbirmi001c
 */
public class Appointment {
    
    private final SimpleIntegerProperty apptId = new SimpleIntegerProperty(0);
    private final SimpleStringProperty apptType = new SimpleStringProperty("");
    private final SimpleStringProperty apptDateTime = new SimpleStringProperty("");

    public Appointment() {
        this(0, "", "");
    }
    
    public Appointment(Integer apptID, String apptType, String apptDateTime) {
        
        setApptId(apptID);
        setApptType(apptType);
        setApptDateTime(apptDateTime);
        
    }
    
    public void setApptId(Integer myApptId) {
        apptId.set(myApptId);
    }

    public Integer getApptId() {
        return apptId.get();
    }
    
    public void setApptType(String myApptType) {
        apptType.set(myApptType);
    }

    public String getApptType() {
        return apptType.get();
    }
    
    public void setApptDateTime(String myApptDateTime) {
        apptDateTime.set(myApptDateTime);
    }

    public String getApptDateTime() {
        return apptDateTime.get();
    }
    
}
