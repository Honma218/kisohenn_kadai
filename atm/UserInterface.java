package atm;

import java.util.Scanner;

public interface UserInterface {
    
    // ログイン
    public boolean login(Scanner scanner);
    
    // ログアウト
    public void logout();
    
    // ATM メニュー
    public int displayMenuAndProcessActions(Scanner scanner);
}
