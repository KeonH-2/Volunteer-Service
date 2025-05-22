package main.gui;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

import main.ReservationManager;
import main.User;
import main.VolunteerProgram;
import main.VolunteerProgramManager;

public class UserPanel extends BasePanel {
    public UserPanel(main.VolunteerServiceGUI mainFrame) {
        super(mainFrame);
    }

    @Override
    protected void initializePanel() {
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        JButton programListButton = new JButton("봉사 프로그램 조회/필터");
        JButton reservationButton = new JButton("예약 신청");
        JButton myPageButton = new JButton("마이페이지");
        JButton updateInfoButton = new JButton("회원정보 수정");
        JButton logoutButton = new JButton("로그아웃");

        // 버튼 스타일 설정
        programListButton.setBackground(new Color(240, 240, 240));
        programListButton.setForeground(Color.BLACK);
        programListButton.setFocusPainted(false);
        reservationButton.setBackground(new Color(240, 240, 240));
        reservationButton.setForeground(Color.BLACK);
        reservationButton.setFocusPainted(false);
        myPageButton.setBackground(new Color(240, 240, 240));
        myPageButton.setForeground(Color.BLACK);
        myPageButton.setFocusPainted(false);
        updateInfoButton.setBackground(new Color(240, 240, 240));
        updateInfoButton.setForeground(Color.BLACK);
        updateInfoButton.setFocusPainted(false);
        logoutButton.setBackground(new Color(240, 240, 240));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setFocusPainted(false);

        buttonPanel.add(programListButton);
        buttonPanel.add(reservationButton);
        buttonPanel.add(myPageButton);
        buttonPanel.add(updateInfoButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.NORTH);

        programListButton.addActionListener(e -> showProgramListDialog());
        reservationButton.addActionListener(e -> showReservationDialog());
        myPageButton.addActionListener(e -> showMyPageDialog());
        updateInfoButton.addActionListener(e -> showUpdateInfoDialog());
        logoutButton.addActionListener(e -> logout());
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

    private void showReservationDialog() {
        // 예약 신청 다이얼로그 표시 로직
        JDialog dialog = new JDialog(mainFrame, "예약 신청", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        List<VolunteerProgram> programs = mainFrame.getProgramManager().getPrograms();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (int i = 0; i < programs.size(); i++) {
            VolunteerProgram p = programs.get(i);
            int current = mainFrame.getReservationManager().getReservationList(p.getName()).size();
            int max = p.getMaxParticipants();
            String item = String.format("[%d] %s | %s | %s | %s | %d/%d명 신청 중",
                    i + 1, p.getName(), p.getDate(), p.getLocation(), p.getCategory(), current, max);
            listModel.addElement(item);
        }

        JList<String> programList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(programList);

        JButton applyButton = new JButton("신청하기");
        JLabel instructionLabel = new JLabel("신청할 프로그램을 선택한 후 버튼을 누르세요.");

        applyButton.addActionListener(e -> {
            int selectedIndex = programList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(dialog, "프로그램을 선택해주세요.");
                return;
            }

            VolunteerProgram selected = programs.get(selectedIndex);
            String result = mainFrame.getReservationManager().tryMakeReservation(
                    mainFrame.getCurrentUser().getId(),
                    selected,
                    mainFrame.getProgramManager());
            JOptionPane.showMessageDialog(dialog, result);
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(instructionLabel, BorderLayout.WEST);
        bottomPanel.add(applyButton, BorderLayout.EAST);

        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showMyPageDialog() {
        // 마이페이지 다이얼로그 표시 로직
        JDialog dialog = new JDialog(mainFrame, "마이페이지", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        String userId = mainFrame.getCurrentUser().getId();
        VolunteerProgramManager vpm = mainFrame.getProgramManager();
        ReservationManager rm = mainFrame.getReservationManager();

        List<String> myPrograms = new ArrayList<>();
        for (String programName : rm.getReservationManager().keySet()) {
            if (rm.getReservationManager().get(programName).contains(userId)) {
                myPrograms.add(programName);
            }
        }

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String programName : myPrograms) {
            VolunteerProgram p = vpm.getProgramByName(programName);
            if (p != null) {
                listModel.addElement(
                        String.format("%s (%s, %s, %s)", p.getName(), p.getDate(), p.getLocation(), p.getCategory()));
            }
        }

        JList<String> programList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(programList);
        dialog.add(scrollPane, BorderLayout.CENTER);

        if (myPrograms.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "예약된 봉사 프로그램이 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            return;
        }

        JPanel bottomPanel = new JPanel();
        JButton cancelBtn = new JButton("선택한 예약 취소");
        JButton closeBtn = new JButton("닫기");

        cancelBtn.addActionListener(e -> {
            int selectedIndex = programList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedProgramName = myPrograms.get(selectedIndex);

                int confirm = JOptionPane.showConfirmDialog(dialog,
                        selectedProgramName + " 예약을 취소하시겠습니까?", "예약 취소 확인",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    rm.cancelReservation(userId, vpm, myPrograms, mainFrame.getUserManager());
                    dialog.dispose(); // 취소 후 다이얼로그 닫기
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "예약을 선택해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            }
        });

        closeBtn.addActionListener(e -> dialog.dispose());

        bottomPanel.add(cancelBtn);
        bottomPanel.add(closeBtn);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showUpdateInfoDialog() {
        // 회원정보 수정 다이얼로그 표시 로직
        User user = mainFrame.getLoggedInUser();
        JDialog dialog = new JDialog(mainFrame, "회원정보 수정", true);
        dialog.setSize(300, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));

        JTextField nameField = new JTextField(user.getName());
        JPasswordField passwordField = new JPasswordField(user.getPassword());
        JTextField phoneField = new JTextField(user.getPhonenumber());

        dialog.add(new JLabel("이름:"));
        dialog.add(nameField);

        dialog.add(new JLabel("비밀번호:"));
        dialog.add(passwordField);

        dialog.add(new JLabel("전화번호:"));
        dialog.add(phoneField);

        JButton saveButton = new JButton("저장");
        JButton cancelButton = new JButton("취소");

        saveButton.addActionListener(e -> {
            user.setName(nameField.getText());
            user.setPassword(new String(passwordField.getPassword()));
            user.setPhonenumber(phoneField.getText());

            mainFrame.getUserManager().saveUsers();

            JOptionPane.showMessageDialog(dialog, "회원정보가 수정되었습니다.");
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(saveButton);
        dialog.add(cancelButton);

        dialog.setVisible(true);
    }