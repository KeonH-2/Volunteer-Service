import java.awt.*;
import javax.swing.*;

public class UserPanel extends BasePanel {
    public UserPanel(VolunteerServiceGUI mainFrame) {
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
        // ... 기존 프로그램 조회/필터 다이얼로그 코드 ...
    }

    private void showReservationDialog() {
        // 예약 신청 다이얼로그 표시 로직
        JDialog dialog = new JDialog(mainFrame, "예약 신청", true);
        // ... 기존 예약 신청 다이얼로그 코드 ...
    }

    private void showMyPageDialog() {
        // 마이페이지 다이얼로그 표시 로직
        JDialog dialog = new JDialog(mainFrame, "마이페이지", true);
        // ... 기존 마이페이지 다이얼로그 코드 ...
    }

    private void showUpdateInfoDialog() {
        // 회원정보 수정 다이얼로그 표시 로직
        JDialog dialog = new JDialog(mainFrame, "회원정보 수정", true);
        // ... 기존 회원정보 수정 다이얼로그 코드 ...
    }
} 