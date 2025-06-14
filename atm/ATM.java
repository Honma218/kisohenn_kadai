package atm;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import account.BankAccount;

// ATM クラス UserInterface インターフェースを実装
public class ATM implements UserInterface {
    // 登録されたすべての口座を保持
    private ArrayList<BankAccount> accounts;
    // 現在ログイン中の口座
    private BankAccount currentAccount;
    // 続行
    public static final int ACTION_CONTINUE = 0;   
    // ログアウト
    public static final int ACTION_LOGOUT = 1;   
    // 終了
    public static final int ACTION_EXIT_SYSTEM = 2; 

    // コンストラクタ：テスト用口座を3つ作成しリストに追加
    public ATM() {
        accounts = new ArrayList<>();

        // 初期テスト口座の作成
        BankAccount acc1 = new BankAccount("John Doe", 1000);
        BankAccount acc2 = new BankAccount("Jane Smith", 500);
        BankAccount acc3 = new BankAccount("Peter Jones", 1500);

        accounts.add(acc1);
        accounts.add(acc2);
        accounts.add(acc3);

        // テスト口座の表示
        System.out.println("使用可能なテスト用の初期口座一覧：");
        for (BankAccount acc : accounts) {
            System.out.println("口座名義人: " + acc.getAccountHolderName()
                + ", 口座番号: " + acc.getAccountNumber()
                + ", 残高: ¥ " + acc.getBalance());
        }
        System.out.println("------------------------------------");
    }

    // ユーザーのログイン処理
    @Override
    public boolean login(Scanner scanner) {
        System.out.print("口座番号を入力してください: ");
        String enteredAccountNumber = scanner.next();
        // バッファクリア
        scanner.nextLine(); 

        // 一致する口座番号を検索
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
    @Override
    public void logout() {
        if (this.currentAccount != null) {
            System.out.println(this.currentAccount.getAccountHolderName() + " さんをログアウトします...");
        }
        this.currentAccount = null;
    }

    // ATM メニューの表示および処理
    @Override
    public int displayMenuAndProcessActions(Scanner scanner) {
        if (currentAccount == null) {
            System.out.println("エラー：ログイン中の口座がありません。");
            return ACTION_LOGOUT;
        }

        // メニューの表示
        System.out.println("\n【ATM メニュー】（口座番号: " + currentAccount.getAccountNumber()
            + "｜名義人: " + currentAccount.getAccountHolderName() + "）");
        System.out.println("1: 残高照会");
        System.out.println("2: 入金");
        System.out.println("3: 出金");
        System.out.println("4: ログアウト");
        System.out.println("5: ATMを終了");
        System.out.print("1～5から操作を選択してください: ");

        int choice;
        try {
            choice = scanner.nextInt();
            // バッファクリア
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    currentAccount.showBalance();
                    return ACTION_CONTINUE;
                case 2:
                    performDeposit(scanner);
                    return ACTION_CONTINUE;
                case 3:
                    performWithdraw(scanner);
                    return ACTION_CONTINUE;
                case 4:
                    logout();
                    return ACTION_LOGOUT;
                case 5:
                    System.out.println("ATM を終了します。ご利用ありがとうございました。");
                    return ACTION_EXIT_SYSTEM;
                default:
                    System.out.println("無効な選択です。もう一度お試しください。");
                    return ACTION_CONTINUE;
            }
        } catch (InputMismatchException e) {
            System.out.println("無効な入力です。数字を入力してください。");
            // バッファクリア
            scanner.nextLine(); 
            return ACTION_CONTINUE;
        }
    }

    // 入金処理
    public void performDeposit(Scanner scanner) {
        if (currentAccount == null) {
            System.out.println("エラー：ログインしていないため、入金できません。");
            return;
        }
        System.out.print("入金する金額を入力してください: ");
        try {
            int amount = scanner.nextInt();
            scanner.nextLine();
            currentAccount.deposit(amount);
        } catch (InputMismatchException e) {
            System.out.println("無効な金額です。数字を入力してください。");
            scanner.nextLine();
        }
    }

    // 出金処理
    public void performWithdraw(Scanner scanner) {
        if (currentAccount == null) {
            System.out.println("エラー：ログインしていないため、出金できません。");
            return;
        }
        System.out.print("出金する金額を入力してください: ");
        try {
            int amount = scanner.nextInt();
            scanner.nextLine();
            currentAccount.withdraw(amount);
        } catch (InputMismatchException e) {
            System.out.println("無効な金額です。数字を入力してください。");
            scanner.nextLine();
        }
    }

    // 現在のログイン中の口座取得
    public BankAccount getCurrentAccount() {
        return currentAccount;
    }

    // ログイン状態確認
    public boolean isUserLoggedIn() {
        return currentAccount != null;
    }

    // 口座が存在するか確認
    public boolean hasAccounts() {
        return !accounts.isEmpty();
    }
}