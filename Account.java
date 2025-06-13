public abstract class Account {

	// 口座名義
    protected String accountHolderName;
    // 残高
    protected int balance;
    // 口座番号
    protected String accountNumber;

    // 入金処理用の抽象メソッドを定義
    public abstract void deposit(int amount);

    // 出金処理用の抽象メソッドを定義
    public abstract void withdraw(int amount);
    
    // 残高表示処理用の抽象メソッドを定義
    public abstract void showBalance();

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
