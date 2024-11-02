public class ATM {
    private Bank bank;
    private User currentUser;

    public ATM(Bank bank) {  // Constructor injection
        this.bank = bank;
    }

    public Bank getBank() {
        return bank;
    }

    public boolean loggedIn = false;

    public void setCurrentUser (User user) {
        this.currentUser = user;
    }

    //Step one for a user is inserting the card.
    public User insertCard(String userId) {
        User user = getBank().getUserById(userId);
        setCurrentUser(user);
        return user;
    }

    public boolean enterPin(String pin) {
        return true;
    }

    public double checkBalance() throws NoUserFoundException {
        User bankUser = getBank().getUserById(currentUser.getId());
        if (bankUser == null) {
            throw new NoUserFoundException("User Not Found");
        }
        return bankUser.getBalance();
    }

    public void deposit(double amount) {
    }

    public boolean withdraw(double amount) {
        return true;
    }

}
