package main.gui;

import javax.swing.*;
import java.awt.*;
import main.UserManager;
import main.Volunteer;
import main.Admin;

public class RegisterPanel extends BasePanel {
    private JTextField nameField, phoneField, idField;
    private JPasswordField pwField;
    private JRadioButton volunteerRadio, adminRadio;
    private ButtonGroup typeGroup;

    public RegisterPanel(main.VolunteerServiceGUI mainFrame) {
        super(mainFrame);
    }

    @Override
    protected void initializePanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        nameField = new JTextField(10);
        phoneField = new JTextField(10);
        idField = new JTextField(10);
        pwField = new JPasswordField(10);

        // 가입 유형 선택 라디오 버튼
        volunteerRadio = new JRadioButton("봉사자", true);
        adminRadio = new JRadioButton("관리자");
        typeGroup = new ButtonGroup();
        typeGroup.add(volunteerRadio);
        typeGroup.add(adminRadio);

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("이름:"), gbc);
        gbc.gridx = 1; add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("전화번호:"), gbc);
        gbc.gridx = 1; add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("아이디:"), gbc);
        gbc.gridx = 1; add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("비밀번호:"), gbc);
        gbc.gridx = 1; add(pwField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("가입 유형:"), gbc);
        gbc.gridx = 1;
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        typePanel.add(volunteerRadio);
        typePanel.add(adminRadio);
        add(typePanel, gbc);

        JButton registerBtn = new JButton("가입");
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; add(registerBtn, gbc);

        registerBtn.addActionListener(e -> doRegister());
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

        // 가입 유형에 따라 객체 생성
        if (adminRadio.isSelected()) {
            userManager.getUsers().add(new Admin(name, phone, id, pw));
        } else {
            userManager.getUsers().add(new Volunteer(name, phone, id, pw));
        }
        userManager.saveUsers();
        showMessage("회원가입 완료! 로그인 해주세요.");
        mainFrame.showLoginPanel();
    }
}
