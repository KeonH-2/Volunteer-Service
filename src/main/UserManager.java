package main;

import java.io.*;
import java.util.*;

public class UserManager {
    private List<User> users = new ArrayList<>();
    private final String USER_FILE = "users.txt";

    public void loadUsers() {
        // 파일로부터 사용자 정보 로드
    }

    public void saveUsers() {
        // 사용자 정보 파일에 저장
    }

    public void registerUser() {
        // 회원가입 로직
    }

    public User login() {
    	Scanner scanner = new Scanner(System.in);
        
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
