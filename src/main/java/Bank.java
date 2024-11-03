import java.util.HashMap;
import java.util.Map;

public class Bank implements BankInterface {

    static String name;

    private final Map<String, User> users = new HashMap<>();

    Bank(String name) {
        users.put("test", new User("test", "test", 10.0));
        Bank.name = name;
    }

    public User getUserById(String id) {
        return users.get(id);
    }

    public void updateUser(User user) {
        users.replace(user.getId(), user);
    }

    public boolean isCardLocked(String userId) {
        User user = users.get(userId);
        return user != null && user.isLocked();
    }

    public static String getBankName() {
        return name;
    }

    public int getUserLength() {
        return users.size();
    }
}
