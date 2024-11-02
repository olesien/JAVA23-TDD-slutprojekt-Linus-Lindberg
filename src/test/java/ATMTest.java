import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        //verify(bank, times(1));
    }
    @Test
    @DisplayName("Balance Is Empty")
    public void testThatBalanceIsEmpty() throws NoUserFoundException { //Note that NoUserFoundException can never be thrown here.
        // Arrange
        User testUser = new User("id", "pin", 0.0);
        atm.setCurrentUser(testUser);  // Set the user in the ATM

        // Stub the bank retrieval of user
        when(bank.getUserById("id")).thenReturn(testUser);

        assertEquals(0.0, atm.checkBalance());
    }

    @Test
    @DisplayName("Card function sets user")
    public void testThatCardFunctionSetsUser() {
        // Arrange
        User testUser = new User("id", "pin", 0.0);

        // Stub the bank retrieval of user
        when(bank.getUserById("id")).thenReturn(testUser);

        atm.insertCard("id");

        //Is same
        assertSame(atm.getCurrentUser(), testUser);
    }

    @Test
    @DisplayName("Card gets locked if more than 3 attempts fail")
    public void testThatCardGetsLocked() {
        // Arrange
        User testUser = new User("id", "pin", 0.0);

        // Stub the bank retrieval of user
        when(bank.getUserById("id")).thenReturn(testUser);

        atm.setCurrentUser(testUser);  // Set the user in the ATM

        assertDoesNotThrow(() -> atm.enterPin("wrong"));
        assertDoesNotThrow(() -> atm.enterPin("wrong"));
        assertDoesNotThrow(() -> atm.enterPin("wrong"));

        //It is now locked
        assertThrows(CardLockedException.class, () -> atm.enterPin("wrong"));
    }

    @Test
    @DisplayName("Card remains locked if more than 3 attempts fail")
    public void testThatCardRemainsLocked() {
        // Arrange
        User testUser = new User("id", "pin", 0.0);

        // Stub the bank retrieval of user
        when(bank.getUserById("id")).thenReturn(testUser);

        atm.setCurrentUser(testUser);  // Set the user in the ATM

        assertDoesNotThrow(() -> atm.enterPin("wrong"));
        assertDoesNotThrow(() -> atm.enterPin("wrong"));
        assertDoesNotThrow(() -> atm.enterPin("wrong"));


        //It is now locked
        assertThrows(CardLockedException.class, () -> atm.enterPin("wrong"));
        assertThrows(CardLockedException.class, () -> atm.enterPin("wrong"));
        assertThrows(CardLockedException.class, () -> atm.enterPin("wrong"));
    }

    @Test
    @DisplayName("Logged in set to true if pin is correct")
    public void testThatCorrectLoginWorks() {
        // Arrange
        User testUser = new User("id", "pin", 0.0);

        // Stub the bank retrieval of user
        when(bank.getUserById("id")).thenReturn(testUser);

        atm.setCurrentUser(testUser);  // Set the user in the ATM

        assertDoesNotThrow(() -> atm.enterPin("pin"));
        assertTrue(atm.loggedIn);
    }


}