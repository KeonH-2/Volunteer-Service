package main.gui;

import java.awt.*;
import javax.swing.*;

import java.util.List;
import main.VolunteerProgram;

public class AdminPanel extends BasePanel {
    public AdminPanel(main.VolunteerServiceGUI mainFrame) {
        super(mainFrame);
    }

    @Override
    protected void initializePanel() {
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        JButton programRegisterButton = new JButton("봉사 프로그램 등록");
        JButton programListButton = new JButton("봉사 프로그램 조회/필터");
        JButton logoutButton = new JButton("로그아웃");

        // 버튼 스타일 설정
        programRegisterButton.setBackground(new Color(240, 240, 240));
        programRegisterButton.setForeground(Color.BLACK);
        programRegisterButton.setFocusPainted(false);
        programListButton.setBackground(new Color(240, 240, 240));
        programListButton.setForeground(Color.BLACK);
        programListButton.setFocusPainted(false);
        logoutButton.setBackground(new Color(240, 240, 240));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setFocusPainted(false);

        buttonPanel.add(programRegisterButton);
        buttonPanel.add(programListButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.NORTH);

        programRegisterButton.addActionListener(e -> showProgramRegisterDialog());
        programListButton.addActionListener(e -> showProgramListDialog());
        logoutButton.addActionListener(e -> logout());
    }

    private void showProgramRegisterDialog() {
        // 프로그램 등록 다이얼로그 표시 로직
        JDialog dialog = new JDialog(mainFrame, "봉사 프로그램 등록", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(7, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField maxParticipantsField = new JTextField();
        JTextField hoursField = new JTextField();

        dialog.add(new JLabel("봉사 이름:"));
        dialog.add(nameField);
        dialog.add(new JLabel("봉사 날짜 (예: 2025-06-15):"));
        dialog.add(dateField);
        dialog.add(new JLabel("장소 (예: 서울 강남구):"));
        dialog.add(locationField);
        dialog.add(new JLabel("카테고리 (예: 환경/교육/복지):"));
        dialog.add(categoryField);
        dialog.add(new JLabel("최대 신청 인원:"));
        dialog.add(maxParticipantsField);
        dialog.add(new JLabel("봉사 시간(시간):"));
        dialog.add(hoursField);

        JButton submitButton = new JButton("등록");
        JButton cancelButton = new JButton("취소");

        dialog.add(submitButton);
        dialog.add(cancelButton);

        submitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String date = dateField.getText().trim();
            String location = locationField.getText().trim();
            String category = categoryField.getText().trim();
            int max;
            int hours;

            try {
                max = Integer.parseInt(maxParticipantsField.getText().trim());
                hours = Integer.parseInt(hoursField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "인원과 시간은 숫자로 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 중복 검사
            boolean exists = mainFrame.getProgramManager().getPrograms().stream()
                    .anyMatch(p -> p.getName().equalsIgnoreCase(name));
            if (exists) {
                JOptionPane.showMessageDialog(dialog, "이미 존재하는 봉사 프로그램입니다.", "중복 오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            mainFrame.getProgramManager().addProgram(new VolunteerProgram(name, date, location, category, max, hours));
            mainFrame.getProgramManager().savePrograms();

            JOptionPane.showMessageDialog(dialog, "[" + name + "] 봉사 프로그램이 등록되었습니다.");
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void showProgramListDialog() {
        // 프로그램 조회/필터 다이얼로그 표시 로직
        JDialog dialog = new JDialog(mainFrame, "봉사 프로그램 조회/필터", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.add(new JLabel("장소(구 단위, 예: 강남구, 전체면 엔터):"));
        JTextField locationField = new JTextField();
        inputPanel.add(locationField);

        inputPanel.add(new JLabel("날짜(yyyy-MM-dd, 전체면 엔터):"));
        JTextField dateField = new JTextField();
        inputPanel.add(dateField);

        inputPanel.add(new JLabel("카테고리(예: 환경/교육/복지, 전체면 엔터):"));
        JTextField categoryField = new JTextField();
        inputPanel.add(categoryField);

        dialog.add(inputPanel, BorderLayout.NORTH);

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton searchButton = new JButton("검색");
        dialog.add(searchButton, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> {
            String location = locationField.getText().trim();
            String date = dateField.getText().trim();
            String category = categoryField.getText().trim();

            List<VolunteerProgram> filtered = mainFrame.getProgramManager().filterPrograms(location, date, category);

            if (filtered.isEmpty()) {
                resultArea.setText("조건에 맞는 봉사 프로그램이 없습니다.");
            } else {
                StringBuilder sb = new StringBuilder("[봉사 프로그램 목록]\n");
                for (VolunteerProgram p : filtered) {
                    sb.append(String.format("- %s | %s | %s | %s | 인원: %d | 시간: %d\n",
                            p.getName(), p.getDate(), p.getLocation(), p.getCategory(),
                            p.getMaxParticipants(), p.getHours()));
                }
                resultArea.setText(sb.toString());
            }
        });

        dialog.setVisible(true);
    }
}