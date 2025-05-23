package main;

import javax.swing.*;
import main.gui.*;
import java.awt.*;

public class VolunteerServiceGUI extends JFrame {
    private UserManager userManager = new UserManager();
    private VolunteerProgramManager programManager = new VolunteerProgramManager();
    private ReservationManager reservationManager = new ReservationManager();
    private User loggedInUser = null;

    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private MainMenuPanel mainMenuPanel;
    private ProgramListPanel programListPanel;
    private ProgramRegisterPanel programRegisterPanel;
    private ReservationPanel reservationPanel;
    private MyPagePanel myPagePanel;

    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);

    public VolunteerServiceGUI() {
        setTitle("봉사 신청 시스템");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);

        userManager.loadUsers();
        programManager.loadPrograms();
        reservationManager.loadReservations();

        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        mainMenuPanel = new MainMenuPanel(this);
        programListPanel = new ProgramListPanel(this);
        programRegisterPanel = new ProgramRegisterPanel(this);
        reservationPanel = new ReservationPanel(this);
        myPagePanel = new MyPagePanel(this);

        cardPanel.add(loginPanel, "login");
        cardPanel.add(registerPanel, "register");
        cardPanel.add(mainMenuPanel, "mainMenu");
        cardPanel.add(programListPanel, "programList");
        cardPanel.add(programRegisterPanel, "programRegister");
        cardPanel.add(reservationPanel, "reservation");
        cardPanel.add(myPagePanel, "myPage");

        add(cardPanel);
        showLoginPanel();
    }

    public UserManager getUserManager() { return userManager; }
    public VolunteerProgramManager getProgramManager() { return programManager; }
    public ReservationManager getReservationManager() { return reservationManager; }
    public User getLoggedInUser() { return loggedInUser; }
    public void setLoggedInUser(User user) { this.loggedInUser = user; }
    
    public void showLoginPanel() { cardLayout.show(cardPanel, "login"); loginPanel.onShow(); }
    public void showRegisterPanel() { cardLayout.show(cardPanel, "register"); registerPanel.onShow(); }
    public void showMainMenuPanel() { cardLayout.show(cardPanel, "mainMenu"); mainMenuPanel.onShow(); }
    public void showProgramListPanel() { cardLayout.show(cardPanel, "programList"); programListPanel.onShow(); }
    public void showProgramRegisterPanel() { cardLayout.show(cardPanel, "programRegister"); programRegisterPanel.onShow(); }
    public void showReservationPanel() { cardLayout.show(cardPanel, "reservation"); reservationPanel.onShow(); }
    public void showMyPagePanel() { cardLayout.show(cardPanel, "myPage"); myPagePanel.onShow(); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VolunteerServiceGUI().setVisible(true));
    }
}
