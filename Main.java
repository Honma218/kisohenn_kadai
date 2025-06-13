import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ATM atm = new ATM();

        // ATMに口座情報が設定されているか確認
        if (!atm.hasAccounts()) {
            System.out.println("ATMに登録された口座がありません。終了します。");
            scanner.close();
            return;
        }

        boolean running = true; // ATMの動作継続フラグ
        while (running) {
        	// ログインしていない場合、ログインを求める
            if (!atm.isUserLoggedIn()) {
                System.out.println("\nようこそ、ATMへ。ログインをお願いします。");
                if (!atm.login(scanner)) { // login now uses the passed scanner
                    System.out.println("ログインに失敗しました。再試行しますか？（yes/no）");
                    String choice = scanner.nextLine().trim().toLowerCase();
                    if (!choice.equals("yes")) {
                        System.out.println("ATMを終了します。ご利用ありがとうございました。");
                        running = false; // Exit the main loop
                    }
                    // If "yes", loop continues, and login will be prompted again
                }
                // If login was successful, atm.currentAccount is now set.
            }
            // ログイン中であればメニューを表示して処理
            if (atm.isUserLoggedIn()) {
                int action = atm.displayMenuAndProcessActions(scanner); // this method now uses passed scanner

                switch (action) {
                    case ATM.ACTION_LOGOUT:
                        // currentAccount is already set to null by atm.logout() via displayMenuAndProcessActions
                        System.out.println("ログアウトしました。");
                        // The loop will continue, and since currentAccount is null, login will be prompted.
                        break;
                    case ATM.ACTION_EXIT_SYSTEM:
                        running = false; // Exit the main loop
                        // Exit message is printed by displayMenuAndProcessActions
                        break;
                    case ATM.ACTION_CONTINUE:
                        // Do nothing, loop will continue to show menu again
                        break;
                    default:
                        System.out.println("不明な操作が実行されました。処理を継続します。");
                        break;
                }
            }

            // Check for scanner closure issue in non-interactive environments (less critical now)
            if (!running && !scanner.hasNextLine() && System.console() == null) {
                 System.out.println("ATMセッションを終了しました。");
            }
        }

        System.out.println("ATMアプリケーションを終了しました。");
        scanner.close(); // スキャナーを閉じる
    }
}
