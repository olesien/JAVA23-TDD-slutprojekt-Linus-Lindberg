import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ATMTest {
    @Test
    public void testThatBalanceIsEmpty() {
       ATM atm = new ATM();
       assertEquals(atm.checkBalance(), 0.0);
    }
}