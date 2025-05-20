package main.gui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import main.UserManager;

public class MainFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);

    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private MainMenuPanel mainMenuPanel;

    private UserManager userManager = new UserManager();

    private Map<String, BasePanel> panelMap = new HashMap<>();

    public MainFrame() {
        setTitle("봉사 신청 시스템");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        userManager.loadUsers();

        loginPanel = new LoginPanel(this, userManager);
        registerPanel = new RegisterPanel(this, userManager);
        mainMenuPanel = new MainMenuPanel(this);

        cardPanel.add(loginPanel, "login");
        cardPanel.add(registerPanel, "register");
        cardPanel.add(mainMenuPanel, "mainMenu");
        
        panelMap.put("login", loginPanel);
        panelMap.put("register", registerPanel);
        panelMap.put("mainMenu", mainMenuPanel);

        add(cardPanel);

        showPanel("login");
    }

    public void showPanel(String name) {
        cardLayout.show(cardPanel, name);
        BasePanel panel = panelMap.get(name);
        if (panel != null) {
            panel.onShow();
        }
    }
}
