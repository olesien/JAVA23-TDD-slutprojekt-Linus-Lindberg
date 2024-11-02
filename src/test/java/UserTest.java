import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    public void testThatBalanceIsFive() {
        User user = new User("id", "pin", 5.0);
        assertEquals(user.getBalance(), 5.0);
    }
}