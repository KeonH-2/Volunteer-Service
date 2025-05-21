package main.gui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import main.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);

    private Map<String, BasePanel> panelMap = new HashMap<>();

    public MainFrame(VolunteerServiceGUI mainFrame) {
        setTitle("봉사 신청 시스템");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // 패널 생성 및 등록
        panelMap.put("login", new LoginPanel(mainFrame));
        panelMap.put("register", new RegisterPanel(mainFrame));
        panelMap.put("mainMenu", new MainMenuPanel(mainFrame));
        panelMap.put("admin", new AdminPanel(mainFrame));
        panelMap.put("user", new UserPanel(mainFrame));
        panelMap.put("volunteer", new VolunteerPanel(mainFrame));

        for (Map.Entry<String, BasePanel> entry : panelMap.entrySet()) {
            cardPanel.add(entry.getValue(), entry.getKey());
        }
        add(cardPanel);

        showPanel("login");
    }

    public void showPanel(String name) {
        cardLayout.show(cardPanel, name);
        BasePanel panel = panelMap.get(name);
        if (panel != null) panel.loggedInUser = ((VolunteerServiceGUI) SwingUtilities.getWindowAncestor(this)).getLoggedInUser();
    }
}
