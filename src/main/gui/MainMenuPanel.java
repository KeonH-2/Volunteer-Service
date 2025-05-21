package main.gui;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends BasePanel {
    public MainMenuPanel(main.VolunteerServiceGUI mainFrame) {
        super(mainFrame);
    }

    @Override
    protected void initializePanel() {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("메인 메뉴", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JButton userBtn = new JButton("회원 메뉴");
        JButton adminBtn = new JButton("관리자 메뉴");
        JButton logoutBtn = new JButton("로그아웃");

        btnPanel.add(userBtn);
        btnPanel.add(adminBtn);
        btnPanel.add(logoutBtn);
        add(btnPanel, BorderLayout.CENTER);

        userBtn.addActionListener(e -> mainFrame.showUserPanel());
        adminBtn.addActionListener(e -> mainFrame.showAdminPanel());
        logoutBtn.addActionListener(e -> logout());
    }
}
