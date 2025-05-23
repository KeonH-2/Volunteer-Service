package main.gui;

import javax.swing.*;
import java.awt.*;

public class ProgramRegisterPanel extends BasePanel {
    private JTextField nameField, dateField, locationField, categoryField, maxField, hoursField;

    public ProgramRegisterPanel(main.VolunteerServiceGUI mainFrame) {
        super(mainFrame);
    }

    @Override
    protected void initializePanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        nameField = new JTextField(10);
        dateField = new JTextField(10);
        locationField = new JTextField(10);
        categoryField = new JTextField(10);
        maxField = new JTextField(5);
        hoursField = new JTextField(5);

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("이름:"), gbc);
        gbc.gridx = 1; add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("날짜(yyyy-MM-dd):"), gbc);
        gbc.gridx = 1; add(dateField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("장소:"), gbc);
        gbc.gridx = 1; add(locationField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("카테고리:"), gbc);
        gbc.gridx = 1; add(categoryField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("정원:"), gbc);
        gbc.gridx = 1; add(maxField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; add(new JLabel("시간:"), gbc);
        gbc.gridx = 1; add(hoursField, gbc);

        JButton regBtn = new JButton("등록");
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; add(regBtn, gbc);

        regBtn.addActionListener(e -> doRegister());
    }

    private void doRegister() {
        String name = nameField.getText().trim();
        String date = dateField.getText().trim();
        String location = locationField.getText().trim();
        String category = categoryField.getText().trim();
        int max, hours;
        try {
            max = Integer.parseInt(maxField.getText().trim());
            hours = Integer.parseInt(hoursField.getText().trim());
        } catch (Exception e) {
            showMessage("정원과 시간은 숫자로 입력하세요.");
            return;
        }
        if (name.isEmpty() || date.isEmpty() || location.isEmpty() || category.isEmpty()) {
            showMessage("모든 항목을 입력하세요.");
            return;
        }
        if (programManager.getProgramByName(name) != null) {
            showMessage("이미 존재하는 프로그램입니다.");
            return;
        }
        programManager.getPrograms().add(new main.VolunteerProgram(name, date, location, category, max, hours));
        programManager.savePrograms();
        showMessage("프로그램이 등록되었습니다.");
        mainFrame.showMainMenuPanel();
    }

    @Override
    public void onShow() {
        nameField.setText("");
        dateField.setText("");
        locationField.setText("");
        categoryField.setText("");
        maxField.setText("");
        hoursField.setText("");
    }
}
