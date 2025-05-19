// package main;

public class Admin extends User {
    public Admin(String id, String password, String name, String phone) {
        super(id, password, name, phone, true);
    }

    public boolean isAdmin() { return true; }
}
