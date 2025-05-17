package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.util.*;

public class UserManager {
    private List<User> users = new ArrayList<>();
    private final String USER_FILE = "users.json";
    private Scanner scanner = new Scanner(System.in);

    public void loadUsers() {
        users.clear();
        try (FileReader reader = new FileReader(USER_FILE)) {
            JSONArray userArray = (JSONArray) new JSONParser().parse(reader);
            for (Object o : userArray) {
                JSONObject obj = (JSONObject) o;
                String name = (String) obj.get("name");
                String phonenumber = (String) obj.get("phonenumber");
                String id = (String) obj.get("id");
                String password = (String) obj.get("password");
                int hours = obj.get("totalVolunteerHours") == null ? 0 : ((Long) obj.get("totalVolunteerHours")).intValue();
                User user = new User(name, phonenumber, id, password);
                user.setTotalVolunteerHours(hours);
                users.add(user);
            }
        } catch (Exception e) { /* 파일 없을 때 등 무시 */ }
    }

    public void saveUsers() {
        JSONArray userArray = new JSONArray();
        for (User user : users) {
            JSONObject obj = new JSONObject();
            obj.put("name", user.getName());
            obj.put("phonenumber", user.getPhonenumber());
            obj.put("id", user.getId());
            obj.put("password", user.getPassword());
            obj.put("totalVolunteerHours", user.getTotalVolunteerHours());
            userArray.add(obj);
        }
        try (FileWriter file = new FileWriter(USER_FILE)) {
            file.write(userArray.toJSONString());
        } catch (Exception e) { e.printStackTrace(); }
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
            if (user.getId().equals(id)) {
                System.out.println("이미 존재하는 아이디입니다.");
                return;
            }
        }
        User newUser = new User(name, phoneNumber, id, password);
        users.add(newUser);
        saveUsers();
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

    public List<User> getUsers() { return users; }
    public User getUserById(String id) {
        for (User u : users) if (u.getId().equals(id)) return u;
        return null;
    }
}
