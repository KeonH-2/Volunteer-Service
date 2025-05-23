package main.gui;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends BasePanel {
    public AdminPanel(main.VolunteerServiceGUI mainFrame) {
        super(mainFrame);
    }

    @Override
    protected void initializePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("관리자 메뉴", SwingConstants.CENTER));

        JButton programRegisterBtn = new JButton("프로그램 등록");
        JButton programListBtn = new JButton("프로그램 목록");
        JButton logoutBtn = new JButton("로그아웃");

        add(programRegisterBtn);
        add(programListBtn);
        add(logoutBtn);

        programRegisterBtn.addActionListener(e -> {
            // 프로그램 등록 패널로 이동 (예시)
            // mainFrame.showProgramRegisterPanel();
            showMessage("프로그램 등록 기능은 여기에 구현하세요.");
        });
        programListBtn.addActionListener(e -> {
            // 프로그램 목록 패널로 이동 (예시)
            // mainFrame.showProgramListPanel();
            showMessage("프로그램 목록 기능은 여기에 구현하세요.");
        });
        logoutBtn.addActionListener(e -> logout());
    }

    @Override
    public void onShow() {
        // 필요시 관리자 화면 갱신
    }
}
