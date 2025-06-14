package account;

// Accountクラス継承のBankAccountクラス
public class BankAccount extends Account {
    
    // 口座番号カウンター
    private static int nextAccountNumber = 1;
    
    // 口座アカウント
    public BankAccount(String accountHolderName, int initialBalance) {
        // 口座名義の設定
        this.accountHolderName = accountHolderName;
        // 初期残高の設定
        this.balance = initialBalance;
        // 自動生成される口座番号
        this.accountNumber = "ACC" + String.format("%03d", nextAccountNumber++);
    }

    @Override
    public void deposit(int amount) {
        if (amount > 0) {
            // 入金額が0より大きい場合、現在の残高に加算
            this.balance += amount; 
            System.out.println("入金が完了しました。現在の残高: " + this.balance + " 円");
        } else {
            // エラー処理
            System.out.println("入金額は正の数でなければなりません。");
        }
    }
    
    // 出金処理
    @Override
    public void withdraw(int amount) { 
        if (amount > 0 && amount <= this.balance) {
            // 残高から出金額を減算
            this.balance -= amount;
            System.out.println("出金が完了しました。現在の残高: " + this.balance + " 円");
        } else {
            // エラー処理
            System.out.println("出金額は正の数でないか、残高が不足しています。");
        }
    }
    
    // 現在の残高表示
    @Override
    public void showBalance() { 
        System.out.println("現在の残高は " + this.balance + " 円です。");
    }
    
    // balanceのゲッター
    public int getBalance() { 
        return balance;
    }
    
    // balanceのセッター
    public void setBalance(int balance) { 
        this.balance = balance;
    }
}
