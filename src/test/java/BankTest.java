import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

//Note that this will not be fully covered, it is just one test to make sure junit is happy.
public class BankTest {
//@Spy
    //@InjectMocks

    @Test
    public void testThatUsersIsEmpty() {
        Bank bank = new Bank();
        assertEquals(bank.getUserLength(), 0);
    }
}