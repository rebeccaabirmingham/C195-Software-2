/*
 * C195 - Scheduling System
 * By Rebecca Birmingham
 * March 2018
 */
package c195_schedulingsystem;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author rbirmi001c
 */
public class Calendar {
    
    private final SimpleStringProperty dayCol = new SimpleStringProperty("");
    private final SimpleStringProperty apptsCol = new SimpleStringProperty("");
    
    public Calendar() {
        this("", "");
    }
    
    public Calendar(String dayCol, String apptsCol) {
        
        setDayCol(dayCol);
        setApptsCol(apptsCol);
        
    }
    
    public void setDayCol(String myDayCol) {
        dayCol.set(myDayCol);
    }

    public String getDayCol() {
        return dayCol.get();
    }
    
    public void setApptsCol(String myApptsCol) {
        apptsCol.set(myApptsCol);
    }

    public String getApptsCol() {
        return apptsCol.get();
    }
    
}
