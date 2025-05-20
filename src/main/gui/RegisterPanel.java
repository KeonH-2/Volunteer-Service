package main.gui;

import javax.swing.*;
import java.awt.*;
import main.UserManager;
import main.Volunteer;

public class RegisterPanel extends BasePanel {
    private JTextField nameField = new JTextField(10);
    private JTextField phoneField = new JTextField(10);
    private JTextField idField = new JTextField(10);
    private JPasswordField pwField = new JPasswordField(10);
    private JButton registerBtn = new JButton("가입");
    private JButton backBtn = new JButton("뒤로가기");

    private UserManager userManager;

    public RegisterPanel(MainFrame mainFrame, UserManager userManager) {
        super(mainFrame);
        this.userManager = userManager;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("이름:"), gbc);
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("전화번호:"), gbc);
        gbc.gridx = 1;
        add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("아이디:"), gbc);
        gbc.gridx = 1;
        add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("비밀번호:"), gbc);
        gbc.gridx = 1;
        add(pwField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(registerBtn, gbc);

        gbc.gridy = 5;
        add(backBtn, gbc);

        registerBtn.addActionListener(e -> doRegister());
        backBtn.addActionListener(e -> goTo("login"));
    }

    private void doRegister() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String id = idField.getText().trim();
        String pw = new String(pwField.getPassword());
        if (name.isEmpty() || phone.isEmpty() || id.isEmpty() || pw.isEmpty()) {
            showMessage("모든 항목을 입력하세요.");
            return;
        }
        if (userManager.getUserById(id) != null) {
            showMessage("이미 존재하는 아이디입니다.");
            return;
        }
        if (userManager.getUsers().stream().anyMatch(u -> u.getPhonenumber().equals(phone))) {
            showMessage("이미 존재하는 전화번호입니다.");
            return;
        }
        userManager.getUsers().add(new Volunteer(name, phone, id, pw));
        userManager.saveUsers();
        showMessage("회원가입 완료! 로그인 해주세요.");
        goTo("login");
    }

    @Override
    public void onShow() {
        nameField.setText("");
        phoneField.setText("");
        idField.setText("");
        pwField.setText("");
    }
}
