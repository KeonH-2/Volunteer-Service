package main.gui;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends BasePanel {
    private JButton registerBtn, programBtn, reserveBtn, myPageBtn, logoutBtn;

    public MainMenuPanel(main.VolunteerServiceGUI mainFrame) {
        super(mainFrame);
        initializePanel();
    }

    @Override
    protected void initializePanel() {
        setLayout(new GridLayout(0, 1, 10, 10));
        programBtn = new JButton("봉사 프로그램 조회/신청");
        registerBtn = new JButton("프로그램 등록(관리자)");
        reserveBtn = new JButton("내 예약/대기 내역");
        myPageBtn = new JButton("마이페이지");
        logoutBtn = new JButton("로그아웃");

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

    @Override
    public void onShow() {
        super.onShow();
        registerBtn.setVisible(loggedInUser != null && loggedInUser.isAdmin());
    }
}
