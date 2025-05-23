package main.gui;

import javax.swing.*;
import java.awt.*;

public class MyPagePanel extends BasePanel {
    public MyPagePanel(main.VolunteerServiceGUI mainFrame) {
        super(mainFrame);
    }

    @Override
    protected void initializePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("마이페이지"));

        JLabel nameLabel = new JLabel();
        JLabel phoneLabel = new JLabel();
        JLabel idLabel = new JLabel();
        JLabel hoursLabel = new JLabel();

        add(nameLabel);
        add(phoneLabel);
        add(idLabel);
        add(hoursLabel);

        JButton backBtn = new JButton("메인으로");
        add(backBtn);

        backBtn.addActionListener(e -> mainFrame.showMainMenuPanel());
    }

    @Override
    public void onShow() {
        removeAll();
        initializePanel();
        revalidate();
        repaint();
    }
}
