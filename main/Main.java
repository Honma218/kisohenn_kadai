package main;

import java.util.Scanner;

import atm.ATM;

// メインクラス
public class Main {

    // アプリケーションのエントリーポイント
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in); 
        
        // ATMクラスのインスタンスを生成
        ATM atm = new ATM(); 

        // 口座情報が存在するか確認
        if (!atm.hasAccounts()) {
            System.out.println("ATMに登録された口座がありません。終了します。");
            scanner.close();
            return;
        }

        // アプリケーションの継続フラグ
        boolean running = true;

        while (running) {

            // ユーザーがログインしていない場合、ログイン処理を行う
            if (!atm.isUserLoggedIn()) {
                System.out.println("\nようこそ、ATMへ。ログインをお願いします。");

                if (!atm.login(scanner)) {
                    System.out.println("ログインに失敗しました。再試行しますか？（yes/no）");
                    String choice = scanner.nextLine().trim().toLowerCase();

                    if (!choice.equals("yes")) {
                        System.out.println("ATMを終了します。ご利用ありがとうございました。");
                        running = false;
                    }
                }
            }

            // ログイン成功後、ATMメニューを表示して操作を処理
            if (atm.isUserLoggedIn()) {
                int action = atm.displayMenuAndProcessActions(scanner);

                switch (action) {
                    case ATM.ACTION_LOGOUT:
                        System.out.println("ログアウトしました。");
                        break;
                    case ATM.ACTION_EXIT_SYSTEM:
                        running = false;
                        break;
                    case ATM.ACTION_CONTINUE:
                        // ループを継続
                        break;
                    default:
                        System.out.println("不明な操作が実行されました。処理を継続します。");
                        break;
                }
            }

            // 終了処理（標準入力がないケースの対応）
            if (!running && !scanner.hasNextLine() && System.console() == null) {
                System.out.println("ATMセッションを終了しました。");
            }
        }

        // 最後にスキャナを閉じる
        System.out.println("ATMアプリケーションを終了しました。");
        scanner.close();
    }
}
