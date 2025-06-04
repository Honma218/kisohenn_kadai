public abstract class Account {

    protected String accountHolderName;
    protected int balance;
    protected String accountNumber;

    public abstract void deposit(int amount);

    public abstract void withdraw(int amount);

    public abstract void showBalance();

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
