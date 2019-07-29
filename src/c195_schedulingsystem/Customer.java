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
public class Customer {
    
    private final SimpleIntegerProperty custId = new SimpleIntegerProperty(0);
    private final SimpleStringProperty custName = new SimpleStringProperty("");
    private final SimpleStringProperty custAddy = new SimpleStringProperty("");

    public Customer() {
        this(0, "", "");
    }
    
    public Customer(Integer custID, String custName, String custAddy) {
        
        setCustId(custID);
        setCustName(custName);
        setCustAddy(custAddy);
        
    }
    
    public void setCustId(Integer myCustId) {
        custId.set(myCustId);
    }

    public Integer getCustId() {
        return custId.get();
    }
    
    public void setCustName(String myCustName) {
        custName.set(myCustName);
    }

    public String getCustName() {
        return custName.get();
    }
    
    public void setCustAddy(String myCustAddy) {
        custAddy.set(myCustAddy);
    }

    public String getCustAddy() {
        return custAddy.get();
    }
    
    
    
}
