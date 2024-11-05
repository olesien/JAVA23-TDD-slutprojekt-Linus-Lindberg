import org.junit.jupiter.api.*;
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

    User testUser = new User("id", "pin", 10.0);

    @InjectMocks
    private ATM atm;

    @BeforeEach
    public void setUp(TestInfo testInfo) {

        atm = new ATM(bank);  // Manually set atm each time
        // Stub the bank retrieval of user
        if (!testInfo.getTags().contains("NoUserStub")) { //We don't always want to stub the user, but we do most of the time
            when(bank.getUserById("id")).thenReturn(testUser);
        }
        atm.setCurrentUser(testUser);  // Set the user in the ATM
    }

    @Test()
    @DisplayName("No User Throws Exception")
    public void testThatNoUserThrowsException() {
        User testUser = new User("id", "pin", 0.0);
        atm.setCurrentUser(testUser);  // Set the user in the ATM

        // Stub the bank retrieval of user
        when(bank.getUserById("id")).thenReturn(null); //This will override the previous stub.

        // Act and Assert
        assertThrows(NoUserFoundException.class, () -> atm.checkBalance());

        //verify(bank, times(1));
    }
    @Test
    @DisplayName("Balance Is Ten")
    public void testThatBalanceIsTen() throws NoUserFoundException { //Note that NoUserFoundException can never be thrown here.

        // Stub the bank retrieval of user
        when(bank.getUserById("id")).thenReturn(testUser);

        assertEquals(10.0, atm.checkBalance());
    }

    @Test
    @DisplayName("Card function sets user")
    public void testThatCardFunctionSetsUser() {

        atm.insertCard("id");

        //Is same
        assertSame(atm.getCurrentUser(), testUser);
    }

    @Test
    @DisplayName("Card gets locked if more than 3 attempts fail")
    public void testThatCardGetsLocked() {

        assertDoesNotThrow(() -> atm.enterPin("wrong"));
        assertDoesNotThrow(() -> atm.enterPin("wrong"));
        assertDoesNotThrow(() -> atm.enterPin("wrong"));

        //It is now locked
        assertThrows(CardLockedException.class, () -> atm.enterPin("wrong"));
    }

    @Test
    @DisplayName("Card remains locked if more than 3 attempts fail")
    public void testThatCardRemainsLocked() {

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

        assertDoesNotThrow(() -> atm.enterPin("pin"));
        assertTrue(atm.loggedIn);
    }

    @Test
    @DisplayName("Null pin does not crash")
    public void testThatNullDoesntCrash() {
        assertDoesNotThrow(() -> atm.enterPin(null));
    }

    @Test
    @DisplayName("Deposit changes amount 10 to 20")
    public void testDepositChangesAmount() {

        assertDoesNotThrow(() -> atm.deposit(10.0));
        assertEquals(atm.getCurrentUser().getBalance(), 20.0);
        verify(bank, times(1)).updateUser(atm.getCurrentUser());
    }

    @Test
    @DisplayName("Deposit can't go below 0")
    public void testDepositIsMinZero() {

        assertThrows(InvalidAmount.class, () -> atm.deposit(-1));
        verify(bank, times(0)).updateUser(atm.getCurrentUser());
    }

    @Test
    @DisplayName("Withdraw can't go below 0")
    public void testWithdrawIsMinZero() {

        assertThrows(InvalidAmount.class, () -> atm.withdraw(-1));
        verify(bank, times(0)).updateUser(atm.getCurrentUser());
    }
    @Test
    @DisplayName("Withdraw changes amount from 10 to 0")
    public void testWithdrawChangesAmount() {

        assertDoesNotThrow(() -> atm.withdraw(10.0));
        assertEquals(atm.getCurrentUser().getBalance(), 0.0);
        verify(bank, times(1)).updateUser(atm.getCurrentUser());;
    }
    @Test
    @DisplayName("Withdraw can not take an account into below 0")
    public void testWithdrawCantGoNegative() {
        assertThrows(ExcessiveWithdrawAmount.class, () -> atm.withdraw(20.01));
        verify(bank, times(0)).updateUser(atm.getCurrentUser());;
    }

    @Tag("NoUserStub")
    @Test
    @DisplayName("Null Transfer gives NoUserFoundException")
    public void testTransferNullUser() throws NoUserFoundException, InvalidAmount, ExcessiveWithdrawAmount {
        when(bank.transferMoney("id", null, 1)).thenThrow(new NoUserFoundException("Invalid"));
        assertThrows(NoUserFoundException.class, () -> atm.transferMoney(null, 1));
    }

    @Tag("NoUserStub")
    @Test
    @DisplayName("Can Not Transfer more than you have")
    public void testTransferExcessiveAmount() throws NoUserFoundException, InvalidAmount, ExcessiveWithdrawAmount {
        when(bank.transferMoney("id", "test2", 1000000000)).thenThrow(new ExcessiveWithdrawAmount("Invalid"));
        assertThrows(ExcessiveWithdrawAmount.class, () -> atm.transferMoney("test2", 1000000000));
    }

    @Tag("NoUserStub")
    @Test
    @DisplayName("Can Not Transfer to invalid user")
    public void testInvalidUserTransfer() throws NoUserFoundException, InvalidAmount, ExcessiveWithdrawAmount {
        when(bank.transferMoney("id", "test2", 1)).thenThrow(new NoUserFoundException("Invalid"));
        assertThrows(NoUserFoundException.class, () -> atm.transferMoney("test2", 1));
    }

    @Tag("NoUserStub")
    @Test
    @DisplayName("Can Not Transfer less than zero")
    public void testTransferFailOnLessThanZero() throws NoUserFoundException, InvalidAmount, ExcessiveWithdrawAmount {
        when(bank.transferMoney("id", "test2", -1)).thenThrow(new InvalidAmount("Invalid"));
        assertThrows(InvalidAmount.class, () -> atm.transferMoney("test2", -1));
    }

    @Tag("NoUserStub")
    @Test
    @DisplayName("Valid transfer is success")
    public void testTransferWorks() throws NoUserFoundException, ExcessiveWithdrawAmount, InvalidAmount {
        when(bank.transferMoney("id", "test2", 1)).thenReturn(true);
        assertTrue(atm.transferMoney("test2", 1));
        verify(bank, times(1)).transferMoney("id", "test2", 1);;
    }
}