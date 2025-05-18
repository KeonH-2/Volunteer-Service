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
                boolean isAdmin = obj.get("isAdmin") != null && (Boolean) obj.get("isAdmin");
                int hours = obj.get("totalVolunteerHours") == null ? 0 : ((Long) obj.get("totalVolunteerHours")).intValue();
                User user = new User(name, phonenumber, id, password, isAdmin);
                user.setTotalVolunteerHours(hours);
                // 알림 복원
                JSONArray notiArr = (JSONArray) obj.get("notifications");
                if (notiArr != null) {
                    for (Object msg : notiArr) {
                        user.addNotification((String) msg);
                    }
                }
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
            obj.put("isAdmin", user.isAdmin());
            obj.put("totalVolunteerHours", user.getTotalVolunteerHours());
            JSONArray notiArr = new JSONArray();
            for (String msg : user.getNotifications()) notiArr.add(msg);
            obj.put("notifications", notiArr);
            userArray.add(obj);
        }
        try (FileWriter file = new FileWriter(USER_FILE)) {
            file.write(userArray.toJSONString());
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void registerUser() {
        System.out.println("\n[회원가입]");
        System.out.print("1. 봉사자 가입\n2. 관리자 가입\n선택 > ");
        String type = scanner.nextLine();
        boolean isAdmin = "2".equals(type);

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
        User newUser = new User(name, phoneNumber, id, password, isAdmin);
        users.add(newUser);
        saveUsers();
        System.out.println((isAdmin ? "관리자" : "봉사자") + " 회원가입이 완료되었습니다.");
    }

    public User login() {
        System.out.print("아이디를 입력하세요: ");
        String inputId = scanner.nextLine();
        System.out.print("비밀번호를 입력하세요: ");
        String inputPw = scanner.nextLine();
        for (User user : users) {
            if (user.getId().equals(inputId) && user.getPassword().equals(inputPw)) {
                System.out.println(user.getName() + "님, 로그인 성공!");
                // 알림 출력
                if (!user.getNotifications().isEmpty()) {
                    System.out.println("== 알림 ==");
                    for (String msg : user.getNotifications()) {
                        System.out.println(msg);
                    }
                    user.clearNotifications();
                    saveUsers();
                }
                return user;
            }
        }
        System.out.println("아이디 또는 비밀번호가 틀렸습니다.");
        return null;
    }
    
    public void updateUserInfo(User user) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n[회원정보 수정]");
            System.out.println("1. 이름 변경");
            System.out.println("2. 전화번호 변경");
            System.out.println("3. 비밀번호 변경");
            System.out.println("4. 뒤로가기");
            System.out.print("선택 > ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("새 이름: ");
                    String newName = scanner.nextLine();
                    user.setName(newName);
                    System.out.println("이름이 변경되었습니다.");
                    break;
                case "2":
                    System.out.print("새 전화번호: ");
                    String newPhone = scanner.nextLine();
                    // 중복 체크
                    boolean duplicate = false;
                    for (User u : users) {
                        if (!u.getId().equals(user.getId()) && u.getPhonenumber().equals(newPhone)) {
                            duplicate = true;
                            break;
                        }
                    }
                    if (duplicate) {
                        System.out.println("이미 사용 중인 전화번호입니다.");
                    } else {
                        user.setPhonenumber(newPhone);
                        System.out.println("전화번호가 변경되었습니다.");
                    }
                    break;
                case "3":
                    System.out.print("새 비밀번호: ");
                    String newPw = scanner.nextLine();
                    user.setPassword(newPw);
                    System.out.println("비밀번호가 변경되었습니다.");
                    break;
                case "4":
                    saveUsers();
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
            saveUsers();
        }
    }


    public List<User> getUsers() { return users; }
    public User getUserById(String id) {
        for (User u : users) if (u.getId().equals(id)) return u;
        return null;
    }
}
