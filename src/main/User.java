package main;

public class User {
	private String name;
	private String phonenumber;
    private String id;
    private String password;
    
    public User(String name, String phonenumber, String id, String password) {
    	this.name = name;
    	this.phonenumber = phonenumber;
    	this.id = id;
    	this.password = password;
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
    
    void setName() {
    	this.name = name;
    }
    
    void setPhonenumber() {
    	this.phonenumber = phonenumber;
    }
    
    void setId() {
    	this.id = id;
    }

    void setPassword() {
    	this.password = password;
    }
}