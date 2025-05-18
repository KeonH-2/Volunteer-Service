package main;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String phonenumber;
    private String id;
    private String password;
    private boolean isAdmin;
    private int totalVolunteerHours;
    private List<String> notifications = new ArrayList<>();

    public User(String name, String phonenumber, String id, String password, boolean isAdmin) {
        this.name = name;
        this.phonenumber = phonenumber;
        this.id = id;
        this.password = password;
        this.isAdmin = isAdmin;
        this.totalVolunteerHours = 0;
    }

    // Getter/Setter
    public String getName() { return name; }
    public String getPhonenumber() { return phonenumber; }
    public String getId() { return id; }
    public String getPassword() { return password; }
    public boolean isAdmin() { return isAdmin; }
    public int getTotalVolunteerHours() { return totalVolunteerHours; }
    public void addVolunteerHours(int hours) { this.totalVolunteerHours += hours; }
    public void setTotalVolunteerHours(int hours) { this.totalVolunteerHours = hours; }
    
    public List<String> getNotifications() { return notifications; }
    public void addNotification(String msg) { notifications.add(msg); }
    public void clearNotifications() { notifications.clear(); }
    
    public void setName(String name) { this.name = name; }
    public void setPhonenumber(String phonenumber) { this.phonenumber = phonenumber; }
    public void setPassword(String password) { this.password = password; }
}
