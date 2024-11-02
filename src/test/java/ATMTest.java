import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ATMTest {


    @Mock
    private Bank bank;

    @Mock
    private User user;

    @InjectMocks
    private ATM atm;

    @BeforeEach
    public void setUp() {
        atm = new ATM(bank);  // Manually set atm each time
    }

    @Test()
    @DisplayName("No User Throws Exception")
    public void testThatNoUserThrowsException() {
        User testUser = new User("id", "pin", 0.0);
        atm.setCurrentUser(testUser);  // Set the user in the ATM

        // Stub the bank retrieval of user
        when(bank.getUserById("id")).thenReturn(null);

        // Act and Assert
        assertThrows(NoUserFoundException.class, () -> atm.checkBalance());
    }
    @Test
    @DisplayName("Balance Is Empty")
    public void testThatBalanceIsEmpty() throws NoUserFoundException {
        // Arrange
        User testUser = new User("id", "pin", 0.0);
        atm.setCurrentUser(testUser);  // Set the user in the ATM

        // Stub the bank retrieval of user
        when(bank.getUserById("id")).thenReturn(testUser);

        // Act and Assert
        assertEquals(0.0, atm.checkBalance());
    }
}