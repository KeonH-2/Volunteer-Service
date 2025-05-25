package main.gui;

import javax.swing.*;
import java.awt.*;
import main.User;

public class LoginPanel extends BasePanel {
    private JTextField idField;
    private JPasswordField pwField;

    public LoginPanel(main.VolunteerServiceGUI mainFrame) {
        super(mainFrame);
        initializePanel();
    }

    @Override
    protected void initializePanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        JLabel idLabel = new JLabel("아이디:");
        JLabel pwLabel = new JLabel("비밀번호:");
        idField = new JTextField(10);
        pwField = new JPasswordField(10);
        JButton loginBtn = new JButton("로그인");
        JButton registerBtn = new JButton("회원가입");

        gbc.gridx = 0; gbc.gridy = 0; add(idLabel, gbc);
        gbc.gridx = 1; add(idField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(pwLabel, gbc);
        gbc.gridx = 1; add(pwField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; add(loginBtn, gbc);
        gbc.gridy = 3; add(registerBtn, gbc);

        loginBtn.addActionListener(e -> doLogin());
        registerBtn.addActionListener(e -> mainFrame.showRegisterPanel());
    }

    private void doLogin() {
        String id = idField.getText().trim();
        String pw = new String(pwField.getPassword());
        if (id.isEmpty() || pw.isEmpty()) {
            showMessage("아이디와 비밀번호를 입력하세요.");
            return;
        }
        User user = userManager.getUserById(id);
        if (user != null && user.getPassword().equals(pw)) {
            mainFrame.setLoggedInUser(user);
            // 알림 출력
            if (!user.getNotifications().isEmpty()) {
                StringBuilder sb = new StringBuilder("로그인 성공!\n== 알림 ==\n");
                for (String msg : user.getNotifications()) sb.append(msg).append("\n");
                user.clearNotifications();
                userManager.saveUsers();
                showMessage(sb.toString());
            } else {
                showMessage("로그인 성공!");
            }
            mainFrame.showMainMenuPanel();
        } else {
            showMessage("아이디 또는 비밀번호가 틀렸습니다.");
        }
    }

    @Override
    public void onShow() {
        super.onShow();
        idField.setText("");
        pwField.setText("");
        idField.requestFocusInWindow();
    }
}
