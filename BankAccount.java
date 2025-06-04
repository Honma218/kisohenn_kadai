public class BankAccount extends Account {

    private static int nextAccountNumberSuffix = 1; // Renamed for clarity

    public BankAccount(String accountHolderName, int initialBalance) { // accountNumber parameter removed
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        // Assign auto-generated account number
        this.accountNumber = "ACC" + String.format("%03d", nextAccountNumberSuffix++);
    }

    @Override
    public void deposit(int amount) {
        if (amount > 0) {
            this.balance += amount;
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    @Override
    public void withdraw(int amount) {
        if (amount > 0 && amount <= this.balance) {
            this.balance -= amount;
        } else {
            System.out.println("Invalid withdrawal amount or insufficient balance.");
        }
    }

    @Override
    public void showBalance() {
        System.out.println("Balance: " + this.balance);
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    // Method for resetting the static counter, intended for test purposes.
    public static void resetNextAccountNumberSuffixForTesting() {
        nextAccountNumberSuffix = 1;
    }
}
