import javax.swing.*;

public abstract class BasePanel extends JPanel {
    protected VolunteerServiceGUI mainFrame;
    protected UserManager userManager;
    protected VolunteerProgramManager programManager;
    protected ReservationManager reservationManager;
    protected User loggedInUser;

    public BasePanel(VolunteerServiceGUI mainFrame) {
        this.mainFrame = mainFrame;
        this.userManager = mainFrame.getUserManager();
        this.programManager = mainFrame.getProgramManager();
        this.reservationManager = mainFrame.getReservationManager();
        this.loggedInUser = mainFrame.getLoggedInUser();
        initializePanel();
    }

    protected abstract void initializePanel();

    protected void showMessage(String message) {
        JOptionPane.showMessageDialog(mainFrame, message);
    }

    protected void logout() {
        mainFrame.setLoggedInUser(null);
        mainFrame.showLoginPanel();
    }
} 