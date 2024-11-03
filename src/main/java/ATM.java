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

    public User getCurrentUser () {
        return this.currentUser;
    }

    public void setCurrentUser (User user) {
        this.currentUser = user;
    }

    public User getLatestUser(String userId) {
        return getBank().getUserById(userId);
    }

    //Step one for a user is inserting the card.
    public User insertCard(String userId) {
        User user = getLatestUser(userId);
        setCurrentUser(user);
        return user;
    }

    public boolean enterPin(String pin) throws CardLockedException, NoUserFoundException {
        if (currentUser == null) {
            throw new NoUserFoundException("User Not Found");
        }
        User latestUser = getLatestUser(currentUser.getId());
        if (latestUser.isLocked()) {
            throw new CardLockedException("Card is locked");
        }

        if (latestUser.getPin().equals(pin)) {
            //Success
            latestUser.resetFailedAttempts();
            loggedIn = true;
            bank.updateUser(latestUser);
            return true;
        }
            if (latestUser.getFailedAttempts() > 2) {
                //Lock card
                latestUser.incrementFailedAttempts();
                latestUser.lockCard();
                throw new CardLockedException("Card is locked");
            } else {
                //Update failed attempts
                latestUser.incrementFailedAttempts();

            }
        bank.updateUser(latestUser);
        setCurrentUser(latestUser);
        return false;
    }

    public double checkBalance() throws NoUserFoundException {
        User bankUser = getBank().getUserById(currentUser.getId());
        if (bankUser == null) {
            throw new NoUserFoundException("User Not Found");
        }
        return bankUser.getBalance();
    }

    public double deposit(double amount) {
        //Get latest instance of user from bank and double check pin code to make sure it's real
        User latestUser = getLatestUser(this.currentUser.getId());

        //Change the money by amount in user object
        double bal = latestUser.deposit(amount);

        //Save changes
        bank.updateUser(latestUser);
        setCurrentUser(latestUser);

        //Return total amount
        return bal;

    }

    public double withdraw(double amount) {
        //Get latest instance of user from bank and double check pin code to make sure it's real
        User latestUser = getLatestUser(this.currentUser.getId());

        //Change the money by amount in user object
        double bal = latestUser.withdraw(amount);

        //Save changes
        bank.updateUser(latestUser);
        setCurrentUser(latestUser);

        //Return total amount
        return bal;
    }

}
