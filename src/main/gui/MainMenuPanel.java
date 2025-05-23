package main.gui;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends BasePanel {
    public MainMenuPanel(main.VolunteerServiceGUI mainFrame) {
        super(mainFrame);
    }

    @Override
    protected void initializePanel() {
        setLayout(new GridLayout(0, 1, 10, 10));
        JButton programBtn = new JButton("봉사 프로그램 조회");
        JButton registerBtn = new JButton("프로그램 등록(관리자)");
        JButton reserveBtn = new JButton("내 예약/대기자");
        JButton myPageBtn = new JButton("마이페이지");
        JButton logoutBtn = new JButton("로그아웃");

        add(programBtn);
        add(registerBtn);
        add(reserveBtn);
        add(myPageBtn);
        add(logoutBtn);

        programBtn.addActionListener(e -> mainFrame.showProgramListPanel());
        registerBtn.addActionListener(e -> mainFrame.showProgramRegisterPanel());
        reserveBtn.addActionListener(e -> mainFrame.showReservationPanel());
        myPageBtn.addActionListener(e -> mainFrame.showMyPagePanel());
        logoutBtn.addActionListener(e -> logout());
    }
}
