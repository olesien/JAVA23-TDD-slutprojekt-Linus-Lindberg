import java.util.HashMap;
import java.util.Map;

public class Bank implements BankInterface {

    static String name;

    private final Map<String, User> users = new HashMap<>();

    Bank(String name) {
        users.put("test", new User("test", "test", 10.0));
        users.put("test2", new User("test2", "test2", 20.0));
        Bank.name = name;
    }

    public User getUserById(String id) {
        return users.get(id);
    }

    public void updateUser(User user) {
        users.replace(user.getId(), user);
    }

    public User deposit(String userId, double amount) throws NoUserFoundException {
        User latestUser = users.get(userId);
        if (latestUser == null) {
            throw new NoUserFoundException("User Not Found");
        }
        latestUser.deposit(amount);
        users.replace(userId, latestUser);
        return latestUser;
    }

    public User withdraw(String userId, double amount) throws NoUserFoundException, ExcessiveWithdrawAmount {
        User latestUser = users.get(userId);
        if (latestUser == null) {
            throw new NoUserFoundException("User Not Found");
        }
        if (latestUser.getBalance() < amount) {
            throw new ExcessiveWithdrawAmount("Can not withdraw this much.");
        }
        latestUser.withdraw(amount);
        users.replace(userId, latestUser);
        return latestUser;
    }

    public boolean isCardLocked(String userId) {
        User user = users.get(userId);
        return user != null && user.isLocked();
    }

    public boolean transferMoney(String fromUserId, String toUserId, double amount) throws NoUserFoundException, ExcessiveWithdrawAmount, InvalidAmount {
        if (amount <= 0) {
            throw new InvalidAmount("Invalid Amount");
        }
        User fromUser = users.get(fromUserId);
        User toUser = users.get(toUserId);
        if (fromUser == null || toUser == null) {
            throw new NoUserFoundException("User could not be found");
        }
        if (amount > fromUser.getBalance()) {
            throw new ExcessiveWithdrawAmount("Not enough money");
        }

        //Transfer
        fromUser.withdraw(amount);
        toUser.deposit(amount);

        return true;
    }

    public static String getBankName() {
        return name;
    }

    public int getUserLength() {
        return users.size();
    }
}
