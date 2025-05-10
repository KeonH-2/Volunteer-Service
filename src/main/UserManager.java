package main;

import java.io.*;
import java.util.*;

public class UserManager {
    private List<User> users = new ArrayList<>();
    private final String USER_FILE = "users.txt";
    private Scanner scanner = new Scanner(System.in);

    public void loadUsers() {
    	File file = new File(USER_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 파일 형식: name,phoneNumber,id,password
                String[] tokens = line.split(",");
                if (tokens.length == 4) {
                    String name = tokens[0];
                    String phoneNumber = tokens[1];
                    String id = tokens[2];
                    String password = tokens[3];
                    users.add(new User(name, phoneNumber, id, password));
                }
            }
        } catch (IOException e) {
            System.out.println("사용자 정보 불러오기 실패: " + e.getMessage());
        }
    }

    public void saveUsers() {
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (User user : users) {
                String line = String.join(",", user.getName(), user.getPhonenumber(), user.getId(), user.getPassword());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("사용자 정보 저장 실패: " + e.getMessage());
        }
    }

    public void registerUser() {
    	System.out.println("\n[회원가입]");
        System.out.print("이름: ");
        String name = scanner.nextLine();
        System.out.print("전화번호: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("아이디: ");
        String id = scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getPhonenumber().equals(phoneNumber)) {
                System.out.println("이미 존재하는 전화번호입니다.");
                return;
            }
        }
        
        for (User user : users) {
            if (user.getId().equals(id)) {
                System.out.println("이미 존재하는 아이디입니다.");
                return;
            }
        }

        User newUser = new User(name, phoneNumber, id, password);
        users.add(newUser);
        System.out.println("회원가입이 완료되었습니다.");
    }

    public User login() {        
        System.out.print("아이디를 입력하세요: ");
        String inputId = scanner.nextLine();
        
        System.out.print("비밀번호를 입력하세요: ");
        String inputPw = scanner.nextLine();
        
        for (User user : users) {
            if (user.getId().equals(inputId) && user.getPassword().equals(inputPw)) {
                System.out.println(user.getName() + "님, 로그인 성공!");
                return user;
            }
        }

        System.out.println("아이디 또는 비밀번호가 틀렸습니다.");
        return null;
    }

    public List<User> getUsers() {
        return users;
    }
}
