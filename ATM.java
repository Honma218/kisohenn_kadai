import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner; // Added for method parameters

// UserInterface might be removed if ATM.run() is fully removed and Main handles all.
// For now, ATM still implements UserInterface, but its run() method will be empty or removed.
public class ATM implements UserInterface {

    private ArrayList<BankAccount> accounts;
    // private Scanner scanner; // Removed
    private BankAccount currentAccount;

    // Action status constants to be returned by displayMenuAndProcessActions
    public static final int ACTION_CONTINUE = 0;
    public static final int ACTION_LOGOUT = 1;
    public static final int ACTION_EXIT_SYSTEM = 2;


    public ATM() {
        accounts = new ArrayList<>();
        // scanner = new Scanner(System.in); // Removed

        BankAccount acc1 = new BankAccount("John Doe", 1000);
        BankAccount acc2 = new BankAccount("Jane Smith", 500);
        BankAccount acc3 = new BankAccount("Peter Jones", 1500);
        accounts.add(acc1);
        accounts.add(acc2);
        accounts.add(acc3);

        System.out.println("Available accounts (for testing/setup):");
        for (BankAccount acc : accounts) {
            System.out.println("Holder: " + acc.getAccountHolderName() + ", Number: " + acc.getAccountNumber() + ", Balance: " + acc.getBalance());
        }
        System.out.println("------------------------------------");
    }

    // Login method now accepts Scanner
    public boolean login(Scanner scanner) {
        System.out.print("Enter Account Number: ");
        String enteredAccountNumber = scanner.next();
        scanner.nextLine(); // Consume newline

        for (BankAccount account : accounts) {
            if (account.getAccountNumber().equals(enteredAccountNumber)) {
                this.currentAccount = account;
                System.out.println("Login successful. Welcome " + currentAccount.getAccountHolderName() + "!");
                return true;
            }
        }
        System.out.println("Login failed. Account not found or invalid account number.");
        this.currentAccount = null;
        return false;
    }

    public void logout() {
        if (this.currentAccount != null) {
            System.out.println("Logging out " + this.currentAccount.getAccountHolderName() + "...");
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
            System.out.println("Error: No user logged in. Cannot display menu.");
            return ACTION_LOGOUT; // Should lead to re-login
        }

        System.out.println("\nATM Menu (Account: " + currentAccount.getAccountNumber() + " | Holder: " + currentAccount.getAccountHolderName() + "):");
        System.out.println("1: Show Balance");
        System.out.println("2: Deposit");
        System.out.println("3: Withdraw");
        System.out.println("4: Logout");
        System.out.println("5: Exit ATM");
        System.out.print("Enter your choice: ");

        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

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
                    logout(); // Sets currentAccount to null
                    return ACTION_LOGOUT;
                case 5:
                    System.out.println("Exiting ATM. Thank you for using our services!");
                    return ACTION_EXIT_SYSTEM; // Signal to Main to exit
                default:
                    System.out.println("Invalid choice. Please try again.");
                    return ACTION_CONTINUE;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number for menu choice.");
            scanner.nextLine(); // Consume the invalid input
            return ACTION_CONTINUE; // Continue to show menu again
        }
    }

    // performDeposit now accepts Scanner
    public void performDeposit(Scanner scanner) {
        // currentAccount check is good, but should be guaranteed by calling context
        if (currentAccount == null) {
            System.out.println("Error: No account logged in for deposit.");
            return;
        }
        System.out.print("Enter deposit amount: ");
        try {
            int amount = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            currentAccount.deposit(amount);
        } catch (InputMismatchException e) {
            System.out.println("Invalid amount. Please enter a number.");
            scanner.nextLine(); // Consume the invalid input
        }
    }

    // performWithdraw now accepts Scanner
    public void performWithdraw(Scanner scanner) {
        if (currentAccount == null) {
            System.out.println("Error: No account logged in for withdrawal.");
            return;
        }
        System.out.print("Enter withdrawal amount: ");
        try {
            int amount = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            currentAccount.withdraw(amount);
        } catch (InputMismatchException e) {
            System.out.println("Invalid amount. Please enter a number.");
            scanner.nextLine(); // Consume the invalid input
        }
    }

    public BankAccount getCurrentAccount() {
        return currentAccount;
    }

    public boolean isUserLoggedIn() {
        return currentAccount != null;
    }

    // Method to check if accounts exist (used by Main before starting)
    public boolean hasAccounts() {
        return !accounts.isEmpty();
    }
}
