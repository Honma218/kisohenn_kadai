import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner; // Added for method parameters

// UserInterface might be removed if ATM.run() is fully removed and Main handles all.
// For now, ATM still implements UserInterface, but its run() method will be empty or removed.
public class ATM implements UserInterface {

    private ArrayList<BankAccount> accounts; // 登録されているすべての口座情報
    // private Scanner scanner; // Removed
    private BankAccount currentAccount; // 現在ログインしている口座

    // Action status constants to be returned by displayMenuAndProcessActions
    public static final int ACTION_CONTINUE = 0; // 継続（ログイン中）
    public static final int ACTION_LOGOUT = 1; // ログアウト
    public static final int ACTION_EXIT_SYSTEM = 2; // システム終了

    // コンストラクタ：初期口座を作成してリストに追加する
    public ATM() {
        accounts = new ArrayList<>();
        // scanner = new Scanner(System.in); // Removed

        BankAccount acc1 = new BankAccount("John Doe", 1000);
        BankAccount acc2 = new BankAccount("Jane Smith", 500);
        BankAccount acc3 = new BankAccount("Peter Jones", 1500);
        accounts.add(acc1);
        accounts.add(acc2);
        accounts.add(acc3);

        System.out.println("使用可能なテスト用の初期口座一覧：");
        for (BankAccount acc : accounts) {
            System.out.println("口座名義人: " + acc.getAccountHolderName() + ", 口座番号: " + acc.getAccountNumber() + ", 残高: ¥ " + acc.getBalance());
        }
        System.out.println("------------------------------------");
    }

    // Login method now accepts Scanner
    public boolean login(Scanner scanner) {
        System.out.print("口座番号を入力してください: ");
        String enteredAccountNumber = scanner.next();
        scanner.nextLine(); // Consume newline

        for (BankAccount account : accounts) {
            if (account.getAccountNumber().equals(enteredAccountNumber)) {
                this.currentAccount = account;
                System.out.println("ログイン成功！ようこそ、" + currentAccount.getAccountHolderName() + " さん。");
                return true;
            }
        }
        System.out.println("ログイン失敗：該当する口座が見つかりません。");
        this.currentAccount = null;
        return false;
    }

    // ログアウト処理
    public void logout() {
        if (this.currentAccount != null) {
            System.out.println(this.currentAccount.getAccountHolderName() + " さんをログアウトします...");
        }
        this.currentAccount = null;
    }

    // run() from UserInterface. If Main takes over all looping, this might become obsolete
    // or simply call a method in Main. For now, it's empty as per plan.
    @Override
    public void run() {
        // This method's logic is effectively moved to Main.java
        // It could be left empty, or throw an UnsupportedOperationException,
        // or delegate to a new main loop driver in Main.java if necessary.
        // For now, let's print a message indicating it's not the primary entry point.
        System.out.println("ATM.run() called. Primary execution should be through Main.java's new loop.");
    }


    // displayMenuAndProcessActions now accepts Scanner and returns an action status
    public int displayMenuAndProcessActions(Scanner scanner) {
        if (currentAccount == null) {
            System.out.println("エラー：ログイン中の口座がありません。");
            return ACTION_LOGOUT; // Should lead to re-login
        }

        System.out.println("\n【ATM メニュー】（口座番号: " + currentAccount.getAccountNumber() + "｜名義人: " + currentAccount.getAccountHolderName() + "）");
        System.out.println("1: 残高照会");
        System.out.println("2: 入金");
        System.out.println("3: 出金");
        System.out.println("4: ログアウト");
        System.out.println("5: ATMを終了");
        System.out.print("1～5から操作を選択してください: ");

        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    currentAccount.showBalance(); // 残高表示
                    return ACTION_CONTINUE;
                case 2:
                    performDeposit(scanner); // 入金処理
                    return ACTION_CONTINUE;
                case 3:
                    performWithdraw(scanner); // 出金処理
                    return ACTION_CONTINUE;
                case 4:
                    logout(); // Sets currentAccount to null
                    return ACTION_LOGOUT;
                case 5:
                    System.out.println("ATM を終了します。ご利用ありがとうございました。");
                    return ACTION_EXIT_SYSTEM; // Signal to Main to exit
                default:
                    System.out.println("無効な選択です。もう一度お試しください。");
                    return ACTION_CONTINUE;
            }
        } catch (InputMismatchException e) {
            System.out.println("無効な入力です。数字を入力してください。");
            scanner.nextLine(); // Consume the invalid input
            return ACTION_CONTINUE; // Continue to show menu again
        }
    }

    // performDeposit now accepts Scanner
    public void performDeposit(Scanner scanner) {
        // currentAccount check is good, but should be guaranteed by calling context
        if (currentAccount == null) {
            System.out.println("エラー：ログインしていないため、入金できません。");
            return;
        }
        System.out.print("入金する金額を入力してください: ");
        try {
            int amount = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            currentAccount.deposit(amount);
        } catch (InputMismatchException e) {
            System.out.println("無効な金額です。数字を入力してください。");
            scanner.nextLine(); // Consume the invalid input
        }
    }

    // performWithdraw now accepts Scanner
    public void performWithdraw(Scanner scanner) {
        if (currentAccount == null) {
            System.out.println("エラー：ログインしていないため、出金できません。");
            return;
        }
        System.out.print("出金する金額を入力してください: ");
        try {
            int amount = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            currentAccount.withdraw(amount);
        } catch (InputMismatchException e) {
            System.out.println("無効な金額です。数字を入力してください。");
            scanner.nextLine(); // Consume the invalid input
        }
    }

    // 現在ログイン中の口座を取得
    public BankAccount getCurrentAccount() {
        return currentAccount;
    }

    // ログイン状態かどうかを判定
    public boolean isUserLoggedIn() {
        return currentAccount != null;
    }

    // Method to check if accounts exist (used by Main before starting)
    public boolean hasAccounts() {
        return !accounts.isEmpty();
    }
}
