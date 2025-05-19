// package main; // 삭제

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String password;
    private String name;
    private String phone;
    private boolean isAdmin;
    private int totalVolunteerHours;
    private List<String> notifications = new ArrayList<>();

    public User(String id, String password, String name, String phone, boolean isAdmin) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.isAdmin = isAdmin;
        this.totalVolunteerHours = 0;
    }

    // Getter/Setter
    public String getId() { return id; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public boolean isAdmin() { return isAdmin; }
    public int getTotalVolunteerHours() { return totalVolunteerHours; }
    public void addVolunteerHours(int hours) { this.totalVolunteerHours += hours; }
    public void setTotalVolunteerHours(int hours) { this.totalVolunteerHours = hours; }
    public List<String> getNotifications() { return notifications; }
    public void addNotification(String msg) { notifications.add(msg); }
    public void clearNotifications() { notifications.clear(); }
    
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
}
