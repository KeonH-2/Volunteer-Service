import java.awt.*;
import javax.swing.*;

public class AdminPanel extends BasePanel {
    public AdminPanel(VolunteerServiceGUI mainFrame) {
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
        // ... 기존 프로그램 등록 다이얼로그 코드 ...
    }

    private void showProgramListDialog() {
        // 프로그램 조회/필터 다이얼로그 표시 로직
        JDialog dialog = new JDialog(mainFrame, "봉사 프로그램 조회/필터", true);
        // ... 기존 프로그램 조회/필터 다이얼로그 코드 ...
    }
} 