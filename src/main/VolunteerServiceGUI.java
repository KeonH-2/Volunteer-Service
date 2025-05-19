import java.awt.*;
import java.util.List;
import javax.swing.*; 

public class VolunteerServiceGUI extends JFrame {
    private UserManager userManager;
    private VolunteerProgramManager programManager;
    private ReservationManager reservationManager;
    private User loggedInUser;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public VolunteerServiceGUI() {
        userManager = new UserManager();
        programManager = new VolunteerProgramManager();
        reservationManager = new ReservationManager();
        
        // 데이터 로드
        userManager.loadUsers();
        programManager.loadPrograms();
        reservationManager.loadReservations();
        reservationManager.confirmReservations(programManager, userManager);

        // 기본 프레임 설정
        setTitle("봉사 신청 시스템");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // 카드 레이아웃 설정
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 각 패널 초기화
        mainPanel.add(new LoginPanel(this), "LOGIN");
        mainPanel.add(new AdminPanel(this), "ADMIN");
        mainPanel.add(new UserPanel(this), "USER");

        // 메인 패널 추가
        add(mainPanel);

        // 로그인 패널을 처음에 보이게 설정
        cardLayout.show(mainPanel, "LOGIN");
    }

    private void initLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
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
        loginPanel.add(titleLabel, gbc);

        JTextField idField = new JTextField(20);
        JPasswordField pwField = new JPasswordField(20);
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
        loginPanel.add(new JLabel("아이디:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(new JLabel("비밀번호:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(pwField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        loginPanel.add(loginButton, gbc);

        gbc.gridy = 4;
        loginPanel.add(registerButton, gbc);

        loginButton.addActionListener(e -> {
            String id = idField.getText();
            String password = new String(pwField.getPassword());
            loggedInUser = userManager.login(id, password);
            
            if (loggedInUser != null) {
                if (loggedInUser.isAdmin()) {
                    cardLayout.show(mainPanel, "ADMIN");
                } else {
                    cardLayout.show(mainPanel, "USER");
                }
            } else {
                JOptionPane.showMessageDialog(this, "로그인에 실패했습니다.");
            }
        });

        registerButton.addActionListener(e -> {
            // 회원가입 다이얼로그 표시
            showRegisterDialog();
        });

        mainPanel.add(loginPanel, "LOGIN");
    }

    private void initAdminPanel() {
        JPanel adminPanel = new JPanel(new BorderLayout());
        
        JPanel buttonPanel = new JPanel();
        JButton programRegisterButton = new JButton("봉사 프로그램 등록");
        JButton programListButton = new JButton("봉사 프로그램 조회/필터");
        JButton logoutButton = new JButton("로그아웃");

        buttonPanel.add(programRegisterButton);
        buttonPanel.add(programListButton);
        buttonPanel.add(logoutButton);

        adminPanel.add(buttonPanel, BorderLayout.NORTH);

        programRegisterButton.addActionListener(e -> {
            // 프로그램 등록 다이얼로그 표시
            showProgramRegisterDialog();
        });

        programListButton.addActionListener(e -> {
            // 프로그램 조회/필터 다이얼로그 표시
            showProgramListDialog();
        });

        logoutButton.setBackground(new Color(240, 240, 240));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            loggedInUser = null;
            cardLayout.show(mainPanel, "LOGIN");
        });

        mainPanel.add(adminPanel, "ADMIN");
    }

    private void initUserPanel() {
        JPanel userPanel = new JPanel(new BorderLayout());
        
        JPanel buttonPanel = new JPanel();
        JButton programListButton = new JButton("봉사 프로그램 조회/필터");
        JButton reservationButton = new JButton("예약 신청");
        JButton myPageButton = new JButton("마이페이지");
        JButton updateInfoButton = new JButton("회원정보 수정");
        JButton logoutButton = new JButton("로그아웃");

        buttonPanel.add(programListButton);
        buttonPanel.add(reservationButton);
        buttonPanel.add(myPageButton);
        buttonPanel.add(updateInfoButton);
        buttonPanel.add(logoutButton);

        userPanel.add(buttonPanel, BorderLayout.NORTH);

        programListButton.addActionListener(e -> {
            // 프로그램 조회/필터 다이얼로그 표시
            showProgramListDialog();
        });

        reservationButton.addActionListener(e -> {
            // 예약 신청 다이얼로그 표시
            showReservationDialog();
        });

        myPageButton.addActionListener(e -> {
            // 마이페이지 다이얼로그 표시
            showMyPageDialog();
        });

        updateInfoButton.addActionListener(e -> {
            // 회원정보 수정 다이얼로그 표시
            showUpdateInfoDialog();
        });

        logoutButton.setBackground(new Color(240, 240, 240));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            loggedInUser = null;
            cardLayout.show(mainPanel, "LOGIN");
        });

        mainPanel.add(userPanel, "USER");
    }

    private void showRegisterDialog() {
        // 회원가입 다이얼로그 구현
        JDialog dialog = new JDialog(this, "회원가입", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField idField = new JTextField(30);
        JPasswordField pwField = new JPasswordField(30);
        JTextField nameField = new JTextField(30);
        JTextField phoneField = new JTextField(30);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("아이디:"), gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("비밀번호:"), gbc);
        gbc.gridx = 1;
        panel.add(pwField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("이름:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("전화번호:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        
        JButton registerButton = new JButton("가입하기");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);
        
        registerButton.addActionListener(e -> {
            String id = idField.getText();
            String password = new String(pwField.getPassword());
            String name = nameField.getText();
            String phone = phoneField.getText();
            
            if (userManager.registerUser(id, password, name, phone)) {
                JOptionPane.showMessageDialog(dialog, "회원가입이 완료되었습니다.");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "회원가입에 실패했습니다.");
            }
        });
        
        dialog.add(panel);
        dialog.setVisible(true);
    }

    public void showProgramRegisterDialog() {
        // 프로그램 등록 다이얼로그 구현
        JDialog dialog = new JDialog(this, "봉사 프로그램 등록", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField nameField = new JTextField(20);
        JTextField dateField = new JTextField(20);
        JTextField locationField = new JTextField(20);
        JTextField categoryField = new JTextField(20);
        JTextField maxParticipantsField = new JTextField(20);
        JTextField hoursField = new JTextField(20);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("프로그램명:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("날짜(yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("장소:"), gbc);
        gbc.gridx = 1;
        panel.add(locationField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("카테고리:"), gbc);
        gbc.gridx = 1;
        panel.add(categoryField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("최대 인원:"), gbc);
        gbc.gridx = 1;
        panel.add(maxParticipantsField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("봉사 시간:"), gbc);
        gbc.gridx = 1;
        panel.add(hoursField, gbc);
        
        JButton registerButton = new JButton("등록하기");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);
        
        registerButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String date = dateField.getText();
                String location = locationField.getText();
                String category = categoryField.getText();
                int maxParticipants = Integer.parseInt(maxParticipantsField.getText());
                int hours = Integer.parseInt(hoursField.getText());
                
                programManager.uploadProgram(name, date, location, category, maxParticipants, hours);
                JOptionPane.showMessageDialog(dialog, "프로그램이 등록되었습니다.");
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "숫자 입력이 잘못되었습니다.");
            }
        });
        
        dialog.add(panel);
        dialog.setVisible(true);
    }

    public void showProgramListDialog() {
        // 프로그램 조회/필터 다이얼로그 구현
        JDialog dialog = new JDialog(this, "봉사 프로그램 조회/필터", true);
        dialog.setSize(700, 450);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 필터 입력란을 한 줄에 배치
        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 8, 0, 8);
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel locationLabel = new JLabel("장소(구 단위):");
        JTextField locationField = new JTextField(10);
        JLabel dateLabel = new JLabel("날짜(yyyy-MM-dd):");
        JTextField dateField = new JTextField(10);
        JLabel categoryLabel = new JLabel("카테고리:");
        JTextField categoryField = new JTextField(10);
        JButton searchButton = new JButton("검색");
        searchButton.setBackground(new Color(240, 240, 240));
        searchButton.setForeground(Color.BLACK);
        searchButton.setFocusPainted(false);

        gbc.gridx = 0;
        filterPanel.add(locationLabel, gbc);
        gbc.gridx = 1;
        filterPanel.add(locationField, gbc);
        gbc.gridx = 2;
        filterPanel.add(dateLabel, gbc);
        gbc.gridx = 3;
        filterPanel.add(dateField, gbc);
        gbc.gridx = 4;
        filterPanel.add(categoryLabel, gbc);
        gbc.gridx = 5;
        filterPanel.add(categoryField, gbc);
        gbc.gridx = 6;
        filterPanel.add(searchButton, gbc);

        // 결과 영역
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            String location = locationField.getText();
            String date = dateField.getText();
            String category = categoryField.getText();

            List<VolunteerProgram> filtered = programManager.filterPrograms(location, date, category);
            StringBuilder sb = new StringBuilder();

            if (filtered.isEmpty()) {
                sb.append("조건에 맞는 봉사 프로그램이 없습니다.");
            } else {
                for (VolunteerProgram p : filtered) {
                    sb.append(String.format("- %s | %s | %s | %s | 인원: %d | 시간: %d\n",
                            p.getName(), p.getDate(), p.getLocation(), p.getCategory(),
                            p.getMaxParticipants(), p.getHours()));
                }
            }

            resultArea.setText(sb.toString());
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    public void showReservationDialog() {
        // 예약 신청 다이얼로그 구현
        showProgramListDialog(); // 기존처럼 프로그램 목록을 보여주고
        // 선택한 프로그램에 대한 예약 처리
    }

    public void showMyPageDialog() {
        JDialog dialog = new JDialog(this, "마이페이지", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout());

        JTextArea reservationArea = new JTextArea();
        reservationArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reservationArea);

        // 예약 내역 출력
        StringBuilder sb = new StringBuilder();
        List<Reservation> myReservations = reservationManager.getMyReservations(loggedInUser.getId());
        if (myReservations.isEmpty()) {
            sb.append("예약된 봉사 프로그램이 없습니다.");
        } else {
            for (Reservation r : myReservations) {
                sb.append("- 프로그램명: ").append(r.getProgramName())
                  .append(" | 승인여부: ").append(r.isConfirmed() ? "승인됨" : "대기중").append("\n");
            }
        }
        reservationArea.setText(sb.toString());

        panel.add(scrollPane, BorderLayout.CENTER);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    public void showUpdateInfoDialog() {
        // 회원정보 수정 다이얼로그 구현
        JDialog dialog = new JDialog(this, "회원정보 수정", true);
        dialog.setSize(300, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JPasswordField pwField = new JPasswordField(20);
        JTextField nameField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("새 비밀번호:"), gbc);
        gbc.gridx = 1;
        panel.add(pwField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("이름:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("전화번호:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        
        JButton updateButton = new JButton("수정하기");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(updateButton, gbc);
        
        updateButton.addActionListener(e -> {
            String password = new String(pwField.getPassword());
            String name = nameField.getText();
            String phone = phoneField.getText();
            
            if (userManager.updateUserInfo(loggedInUser, password, name, phone)) {
                JOptionPane.showMessageDialog(dialog, "회원정보가 수정되었습니다.");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "회원정보 수정에 실패했습니다.");
            }
        });
        
        dialog.add(panel);
        dialog.setVisible(true);
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public VolunteerProgramManager getProgramManager() {
        return programManager;
    }

    public ReservationManager getReservationManager() {
        return reservationManager;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public void showLoginPanel() {
        cardLayout.show(mainPanel, "LOGIN");
    }

    public void showAdminPanel() {
        cardLayout.show(mainPanel, "ADMIN");
    }

    public void showUserPanel() {
        cardLayout.show(mainPanel, "USER");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VolunteerServiceGUI().setVisible(true);
        });
    }
} 