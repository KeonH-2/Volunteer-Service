package main.gui;

import javax.swing.*;
import java.awt.*;
import main.UserManager;
import main.User;

public class LoginPanel extends BasePanel {
    private JTextField idField = new JTextField(15);
    private JPasswordField pwField = new JPasswordField(15);
    private JButton loginBtn = new JButton("로그인");
    private JButton registerBtn = new JButton("회원가입");

    private UserManager userManager;

    public LoginPanel(MainFrame mainFrame, UserManager userManager) {
        super(mainFrame);
        this.userManager = userManager;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("아이디:"), gbc);
        gbc.gridx = 1;
        add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("비밀번호:"), gbc);
        gbc.gridx = 1;
        add(pwField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(loginBtn, gbc);

        gbc.gridy = 3;
        add(registerBtn, gbc);

        loginBtn.addActionListener(e -> doLogin());
        registerBtn.addActionListener(e -> goTo("register"));
    }

    private void doLogin() {
        String id = idField.getText().trim();
        String pw = new String(pwField.getPassword());
        User user = userManager.getUserById(id);
        if (user != null && user.getPassword().equals(pw)) {
            showMessage("로그인 성공!");
            goTo("mainMenu");
        } else {
            showMessage("아이디 또는 비밀번호가 틀렸습니다.");
        }
    }

    @Override
    public void onShow() {
        idField.setText("");
        pwField.setText("");
    }
}
