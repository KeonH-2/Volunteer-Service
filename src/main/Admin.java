package main;

public class Admin extends User {
    public Admin(String name, String phonenumber, String id, String password) {
        super(name, phonenumber, id, password);
    }

    @Override
    public boolean isAdmin() { return true; }
}
