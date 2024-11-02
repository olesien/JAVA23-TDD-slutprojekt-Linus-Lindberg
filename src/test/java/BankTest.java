import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class BankTest {
//@Spy
    //@InjectMocks

    @Test
    public void testThatUsersIsEmpty() {
        Bank bank = new Bank();
        assertEquals(bank.getUserLength(), 0);
    }
}