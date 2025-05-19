/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class Reservation {
    private String userId;
    private String programName;
    private boolean confirmed;

    public Reservation(String userId, String programName, boolean confirmed) {
        this.userId = userId;
        this.programName = programName;
        this.confirmed = confirmed;
    }

    public String getUserId() { return userId; }
    public String getProgramName() { return programName; }
    public boolean isConfirmed() { return confirmed; }
    public void setConfirmed(boolean confirmed) { this.confirmed = confirmed; }
}
