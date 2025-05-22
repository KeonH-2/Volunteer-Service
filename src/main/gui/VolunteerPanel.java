package main.gui;

import java.util.List;

import javax.swing.*;

public class VolunteerPanel extends BasePanel {
    public VolunteerPanel(main.VolunteerServiceGUI mainFrame) {
        super(mainFrame);
    }

    @Override
    protected void initializePanel() {

        JDialog dialog = new JDialog(mainFrame, "봉사 프로그램 조회/필터", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 5, 5));

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

            // 프로그램 필터링 - mainFrame에 프로그램 매니저가 있다고 가정
            List<main.VolunteerProgram> filtered = mainFrame.getProgramManager().filterPrograms(location, date,
                    category);

            if (filtered.isEmpty()) {
                resultArea.setText("조건에 맞는 봉사 프로그램이 없습니다.");
            } else {
                StringBuilder sb = new StringBuilder("[봉사 프로그램 목록]\n");
                for (main.VolunteerProgram p : filtered) {
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
