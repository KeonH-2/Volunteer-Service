package main.gui;

import javax.swing.*;
import main.*;

public abstract class BasePanel extends JPanel {
    protected main.VolunteerServiceGUI mainFrame;
    protected main.UserManager userManager;
    protected main.VolunteerProgramManager programManager;
    protected main.ReservationManager reservationManager;
    protected main.User loggedInUser;

    public BasePanel(VolunteerServiceGUI mainFrame) {
        this.mainFrame = mainFrame;
        this.userManager = mainFrame.getUserManager();
        this.programManager = mainFrame.getProgramManager();
        this.reservationManager = mainFrame.getReservationManager();
        this.loggedInUser = mainFrame.getLoggedInUser();
        initializePanel();
    }

    protected abstract void initializePanel();
    public void onShow() {}

    protected void showMessage(String message) {
        JOptionPane.showMessageDialog(mainFrame, message);
    }

    protected void logout() {
        mainFrame.setLoggedInUser(null);
        mainFrame.showLoginPanel();
    }
}
