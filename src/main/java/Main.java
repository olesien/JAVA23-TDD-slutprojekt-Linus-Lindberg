import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void showOptions(BufferedReader reader, ATM atm, User user) {
        System.out.println("Please choose what you would like to do\nA: Check balance\nB: Deposit Money\nC: Withdraw Money\nD: Transfer Money\nE: Logout");
        try {
            String option = reader.readLine();
            switch (option.toLowerCase()) {
                case "a": {
                    //Check balance
                    double balance = atm.checkBalance();
                    System.out.println("Your currently have: $" + balance);
                    showOptions(reader,atm, user);
                    break;
                }
                case "b": {
                    //Deposit money
                    System.out.println("Deposit amount:");
                    double amount = Double.parseDouble(reader.readLine());
                    double currentAmount = atm.deposit(amount);
                    System.out.println("Success! You now have: " + currentAmount);
                    showOptions(reader,atm, user);
                    break;

                }
                case "c": {
                    //Withdraw money
                    System.out.println("Withdraw amount:");
                    double amount = Double.parseDouble(reader.readLine());
                    double currentAmount = atm.withdraw(amount);
                    System.out.println("Success! You now have: " + currentAmount);
                    showOptions(reader,atm, user);
                    break;
                }
                case "d": {
                    //Transfer money
                    System.out.println("Transfer amount:");
                    double amount = Double.parseDouble(reader.readLine());

                    System.out.println("User ID:");
                    String userId = reader.readLine();
                    boolean success = atm.transferMoney(userId, amount);
                    if (success) {
                        System.out.println("Successfully transfered money!");
                    } else{
                        System.out.println("Failed to transfer please try again.");
                    }
                    showOptions(reader,atm, user);
                    break;
                }
                case "e": {
                    //Log out
                    System.out.println("Successfully logged out!");
                    System.out.println("Enter Card ID:");
                    getCard(reader, atm);
                    break;
                }
                default: {
                    throw new IOException("Invalid input");
                }
            }
        } catch (IOException e) {
            //Retry
            System.out.println("Invalid input!");
            showOptions(reader,atm, user);
        } catch (NumberFormatException e) {
            //Retry
            System.out.println("The amount you entered was invalid and thus the transaction failed.");
            showOptions(reader,atm, user);
        } catch (NoUserFoundException e) {
            throw new RuntimeException(e); //This should not be possible so we want to just end the application here
        } catch (ExcessiveWithdrawAmount e) {
            System.out.println("The amount you entered is more than your total balance. Please try again with a lower amount.");
            showOptions(reader,atm, user);
        } catch (InvalidAmount e) {
            System.out.println("The amount you entered is invalid (likely at or below zero)");
            showOptions(reader,atm, user);
        }
    }

    public static void getPin(BufferedReader reader, ATM atm, User user) {
        System.out.println("Enter PIN: ");
        try {
            String pin = reader.readLine();
            boolean loggedIn = atm.enterPin(pin);
            if (!loggedIn) {
                System.out.println("Incorrect PIN. Retry please.");
                getPin(reader,atm, user);
            } else {
                System.out.println("Login successful!");
                showOptions(reader,atm, user);
            }

        } catch (IOException e) {
            //Retry
            System.out.println("Invalid input!");
            getPin(reader,atm, user);
        } catch (CardLockedException e) {
            System.out.println("Sadly the card has been locked due to too many attempts.");
        } catch (NoUserFoundException e) {
            System.out.println("A user could not be located :(");
        }
    }

    public static void getCard(BufferedReader reader, ATM atm) {

        try {
            String card = reader.readLine();
            User user = atm.insertCard(card);
            if (user == null) {
                System.out.println("User Not Found! Please reenter Card (ID):");
                getCard(reader, atm);
            } else {
                getPin(reader,atm, user);
            }

        } catch (IOException e) {
            //Retry
            System.out.println("Invalid input! Please reenter Card (ID):");
            getCard(reader, atm);
        }
    }
    public static void main(String[] args) {
        Bank bank = new Bank("Fake Bank AB");
        ATM atm = new ATM(bank);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println("Welcome to " + Bank.getBankName() + "! \nPlease enter Card (ID): ");
        getCard(reader, atm);

    }
}
