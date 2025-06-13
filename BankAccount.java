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
            System.out.println("入金が完了しました。現在の残高: " + this.balance + " 円");
        } else {
            System.out.println("入金額は正の数でなければなりません。");
        }
    }

    @Override
    public void withdraw(int amount) {
        if (amount > 0 && amount <= this.balance) {
            this.balance -= amount;
            System.out.println("出金が完了しました。現在の残高: " + this.balance + " 円");
        } else {
            System.out.println("出金額は正の数でないか、残高が不足しています。");
        }
    }

    @Override
    public void showBalance() {
        System.out.println("現在の残高は " + this.balance + " 円です。");
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
