package main.gui;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends BasePanel {
    private MainFrame frame;

    public MainMenuPanel(MainFrame mainFrame) {
        super(mainFrame);
        setLayout(new BorderLayout());
        add(new JLabel("메인 메뉴", SwingConstants.CENTER), BorderLayout.CENTER);

    }
}
