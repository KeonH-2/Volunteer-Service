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
        // 로그인 로직 후 성공 시 User 반환
        return null;
    }

    public List<User> getUsers() {
        return users;
    }
}
