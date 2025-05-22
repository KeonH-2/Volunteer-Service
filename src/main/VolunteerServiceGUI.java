package main;

import javax.swing.*;
import main.gui.*;
import java.awt.*;

public class VolunteerServiceGUI extends JFrame {
    // 핵심 매니저 객체
    private UserManager userManager = new UserManager();
    private VolunteerProgramManager programManager = new VolunteerProgramManager();
    private ReservationManager reservationManager = new ReservationManager();
    private User loggedInUser = null;

    // 패널
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private MainMenuPanel mainMenuPanel;
    private AdminPanel adminPanel;
    private UserPanel userPanel;
    // ... 필요시 더 추가

    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);

    public VolunteerServiceGUI() {
        setTitle("봉사 신청 시스템");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        // 데이터 로드
        userManager.loadUsers();
        programManager.loadPrograms();
        reservationManager.loadReservations();

        // 패널 생성
        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        mainMenuPanel = new MainMenuPanel(this);
        adminPanel = new AdminPanel(this);
        userPanel = new UserPanel(this);
        // ... 필요시 더 추가

        // 패널 등록
        cardPanel.add(loginPanel, "login");
        cardPanel.add(registerPanel, "register");
        cardPanel.add(mainMenuPanel, "mainMenu");
        cardPanel.add(adminPanel, "admin");
        cardPanel.add(userPanel, "user");
        // ... 필요시 더 추가

        add(cardPanel);
        showLoginPanel();
    }

    // 매니저/유저 getter
    public UserManager getUserManager() {
        return userManager;
    }

    public VolunteerProgramManager getProgramManager() {
        return programManager;
    }

    public ReservationManager getReservationManager() {
        return reservationManager;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    // UserPanel에서 사용
    public User getCurrentUser() {
        return loggedInUser;
    }

    // 로그인 유저 setter
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    // 패널 전환 메서드들
    public void showLoginPanel() {
        cardLayout.show(cardPanel, "login");
    }

    public void showRegisterPanel() {
        cardLayout.show(cardPanel, "register");
    }

    public void showMainMenuPanel() {
        cardLayout.show(cardPanel, "mainMenu");
    }

    public void showAdminPanel() {
        cardLayout.show(cardPanel, "admin");
    }

    public void showUserPanel() {
        cardLayout.show(cardPanel, "user");
    }
    // ... 필요시 더 추가

    // 실행 진입점
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VolunteerServiceGUI().setVisible(true);
        });
    }
}
