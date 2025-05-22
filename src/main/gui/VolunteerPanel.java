package main.gui;

import java.util.List;

import javax.swing.*;

public class VolunteerPanel extends BasePanel {
    public VolunteerPanel(main.VolunteerServiceGUI mainFrame) {
        super(mainFrame);
    }

    @Override
    protected void initializePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
}