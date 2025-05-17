package main;

public class User {
    private String name;
    private String phonenumber;
    private String id;
    private String password;
    private int totalVolunteerHours;

    public User(String name, String phonenumber, String id, String password) {
        this.name = name;
        this.phonenumber = phonenumber;
        this.id = id;
        this.password = password;
        this.totalVolunteerHours = 0;
    }

    public String getName() { 
    	return name; 
    }
    
    public String getPhonenumber() { 
    	return phonenumber; 
    }
    
    public String getId() { 
    	return id; 
    }
    
    public String getPassword() { 
    	return password; 
    }
    
    public int getTotalVolunteerHours() { 
    	return totalVolunteerHours; 
    }
    
    public void addVolunteerHours(int hours) { 
    	this.totalVolunteerHours += hours; 
    }
    
    public void setTotalVolunteerHours(int hours) { 
    	this.totalVolunteerHours = hours; 
    }
}
