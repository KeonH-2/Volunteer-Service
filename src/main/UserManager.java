import java.util.*;
import java.io.*;

public class UserManager {
    private Map<String, User> users;
    private static final String USER_FILE = "users.txt";

    public UserManager() {
        users = new HashMap<>();
    }

    public void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String id = parts[0];
                    String password = parts[1];
                    String name = parts[2];
                    String phone = parts[3];
                    boolean isAdmin = Boolean.parseBoolean(parts[4]);
                    users.put(id, new User(id, password, name, phone, isAdmin));
                }
            }
        } catch (IOException e) {
            System.out.println("사용자 데이터 로드 실패: " + e.getMessage());
        }
    }

    public void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
            for (User user : users.values()) {
                writer.println(String.format("%s,%s,%s,%s,%b",
                    user.getId(), user.getPassword(), user.getName(),
                    user.getPhone(), user.isAdmin()));
            }
        } catch (IOException e) {
            System.out.println("사용자 데이터 저장 실패: " + e.getMessage());
        }
    }

    public boolean registerUser(String id, String password, String name, String phone) {
        if (users.containsKey(id)) {
            return false;
        }
        users.put(id, new User(id, password, name, phone, false));
        saveUsers();
        return true;
    }

    public User login(String id, String password) {
        User user = users.get(id);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean updateUserInfo(User user, String password, String name, String phone) {
        if (user != null) {
            user.setPassword(password);
            user.setName(name);
            user.setPhone(phone);
            saveUsers();
            return true;
        }
        return false;
    }

    public User getUserById(String id) {
        return users.get(id);
    }
}
