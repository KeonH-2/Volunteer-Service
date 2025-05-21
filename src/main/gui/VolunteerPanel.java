package main.gui;

import javax.swing.*;

public class VolunteerPanel extends BasePanel {
    public VolunteerPanel(main.VolunteerServiceGUI mainFrame) {
        super(mainFrame);
    }

    @Override
    protected void initializePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("봉사자 전용 메뉴"));
        // 예시: 봉사 프로그램 신청, 내 봉사 내역 등
    }
}
