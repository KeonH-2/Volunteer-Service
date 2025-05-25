package main.gui;

import javax.swing.*;
import java.awt.*;

public class MyPagePanel extends BasePanel {
    private JLabel nameLabel = new JLabel();
    private JLabel phoneLabel = new JLabel();
    private JLabel idLabel = new JLabel();
    private JLabel hoursLabel = new JLabel();

    public MyPagePanel(main.VolunteerServiceGUI mainFrame) {
        super(mainFrame);
        initializePanel();
    }

    @Override
    protected void initializePanel() {
        setLayout(new BorderLayout());

        // 중앙 정보 패널 (BoxLayout)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // 제목
        JLabel titleLabel = new JLabel("마이페이지");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(titleLabel);

        // 구분선
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(new JSeparator());
        infoPanel.add(Box.createVerticalStrut(20));

        // 사용자 정보 라벨 (아이콘+텍스트)
        nameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
        phoneLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
        idLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
        hoursLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        hoursLabel.setForeground(new Color(0, 102, 204));

        nameLabel.setIcon(UIManager.getIcon("FileView.fileIcon"));
        phoneLabel.setIcon(UIManager.getIcon("FileView.directoryIcon"));
        idLabel.setIcon(UIManager.getIcon("FileChooser.detailsViewIcon"));
        hoursLabel.setIcon(UIManager.getIcon("OptionPane.informationIcon"));

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(phoneLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(idLabel);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(hoursLabel);

        // 버튼 패널 (오른쪽 정렬)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton infoBtn = new JButton("회원정보 수정");
        JButton backBtn = new JButton("메인으로");
        buttonPanel.add(infoBtn);
        buttonPanel.add(backBtn);

        // 전체 배치
        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 이벤트
        infoBtn.addActionListener(e -> showEditDialog());
        backBtn.addActionListener(e -> mainFrame.showMainMenuPanel());
    }

    @Override
    public void onShow() {
        super.onShow();
        this.loggedInUser = mainFrame.getLoggedInUser();
        if (loggedInUser != null) {
            nameLabel.setText("  이름: " + loggedInUser.getName());
            phoneLabel.setText("  전화번호: " + loggedInUser.getPhonenumber());
            idLabel.setText("  아이디: " + loggedInUser.getId());
            hoursLabel.setText("  누적 봉사시간: " + loggedInUser.getTotalVolunteerHours() + "시간");
        }
    }

    private void showEditDialog() {
        JTextField nameField = new JTextField(loggedInUser.getName());
        JTextField phoneField = new JTextField(loggedInUser.getPhonenumber());
        JPasswordField pwField = new JPasswordField(loggedInUser.getPassword());

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("이름:"));
        panel.add(nameField);
        panel.add(new JLabel("전화번호:"));
        panel.add(phoneField);
        panel.add(new JLabel("비밀번호:"));
        panel.add(pwField);

        int result = JOptionPane.showConfirmDialog(this, panel, "회원정보 수정", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String pw = new String(pwField.getPassword());
            if (name.isEmpty() || phone.isEmpty() || pw.isEmpty()) {
                showMessage("모든 항목을 입력하세요.");
                return;
            }
            loggedInUser.setName(name);
            loggedInUser.setPhonenumber(phone);
            loggedInUser.setPassword(pw);
            userManager.saveUsers();
            showMessage("회원정보가 수정되었습니다.");
            onShow();
        }
    }
}
