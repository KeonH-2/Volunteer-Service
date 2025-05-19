import java.awt.*;
import javax.swing.*;

public class LoginPanel extends BasePanel {
    private JTextField idField;
    private JPasswordField pwField;

    public LoginPanel(VolunteerServiceGUI mainFrame) {
        super(mainFrame);
    }

    @Override
    protected void initializePanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // 제목 레이블 추가
        JLabel titleLabel = new JLabel("봉사 예약 신청 서비스");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 30, 5);
        add(titleLabel, gbc);

        idField = new JTextField(20);
        pwField = new JPasswordField(20);
        JButton loginButton = new JButton("로그인");
        JButton registerButton = new JButton("회원가입");

        // 버튼 스타일 설정
        loginButton.setBackground(new Color(240, 240, 240));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        registerButton.setBackground(new Color(240, 240, 240));
        registerButton.setForeground(Color.BLACK);
        registerButton.setFocusPainted(false);

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(new JLabel("아이디:"), gbc);
        gbc.gridx = 1;
        add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("비밀번호:"), gbc);
        gbc.gridx = 1;
        add(pwField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        add(loginButton, gbc);

        gbc.gridy = 4;
        add(registerButton, gbc);

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> showRegisterDialog());
    }

    private void handleLogin() {
        String id = idField.getText();
        String password = new String(pwField.getPassword());
        User user = userManager.login(id, password);
        
        if (user != null) {
            mainFrame.setLoggedInUser(user);
            if (user.isAdmin()) {
                mainFrame.showAdminPanel();
            } else {
                mainFrame.showUserPanel();
            }
        } else {
            showMessage("로그인에 실패했습니다.");
        }
    }

    private void showRegisterDialog() {
        mainFrame.showRegisterDialog();
    }
} 