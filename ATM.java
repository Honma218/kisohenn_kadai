import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class ATM implements UserInterface {

    private ArrayList<BankAccount> accounts;
    private Scanner scanner;
    private BankAccount currentAccount; // Will be null initially

    public ATM() {
        accounts = new ArrayList<>();
        scanner = new Scanner(System.in);

        // Create sample accounts - account numbers are now auto-generated
        BankAccount acc1 = new BankAccount("John Doe", 1000);
        BankAccount acc2 = new BankAccount("Jane Smith", 500);
        BankAccount acc3 = new BankAccount("Peter Jones", 1500);
        accounts.add(acc1);
        accounts.add(acc2);
        accounts.add(acc3);

        // For testing, print generated account numbers
        System.out.println("Available accounts (for testing/setup):");
        for (BankAccount acc : accounts) {
            System.out.println("Holder: " + acc.getAccountHolderName() + ", Number: " + acc.getAccountNumber() + ", Balance: " + acc.getBalance());
        }
        System.out.println("------------------------------------");
    }

    private boolean login() {
        System.out.print("Enter Account Number: ");
        String enteredAccountNumber = scanner.next();
        scanner.nextLine(); // Consume the rest of the line

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

    @Override
    public void run() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts configured in the ATM. Exiting.");
            return;
        }

        while (true) {
            if (currentAccount == null) {
                System.out.println("\nWelcome to the ATM. Please login.");
                if (!login()) {
                    // Optionally, give a few retries or directly exit
                    System.out.println("Login failed. Would you like to try again? (yes/no)");
                    String choice = scanner.nextLine().trim().toLowerCase();
                    if (!choice.equals("yes")) {
                        System.out.println("Exiting ATM. Thank you!");
                        break; // Exit the main while loop
                    }
                    // If 'yes', the loop continues, and login() will be called again.
                }
            }

            // If login was successful, or if already logged in from a previous iteration
            if (currentAccount != null) {
                displayMenuAndProcessActions();
                // displayMenuAndProcessActions handles logout by setting currentAccount = null
                // It also handles System.exit for "Exit ATM"
                // If currentAccount becomes null (due to logout), the next iteration will prompt for login.
            }

            // This check is for non-interactive environments; System.exit is the primary exit for users.
            if (currentAccount == null && !scanner.hasNextLine() && !System.console().writer().checkError()) {
                 System.out.println("ATM session ended due to no further input.");
                 break;
            }
        }
        scanner.close(); // Close scanner when ATM is fully exiting
    }

    private void displayMenuAndProcessActions() {
        // Ensure currentAccount is not null before proceeding (should be guaranteed by run() logic)
        if (currentAccount == null) {
             System.out.println("Error: No user logged in. Returning to login.");
             return;
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
                    break;
                case 2:
                    performDeposit();
                    break;
                case 3:
                    performWithdraw();
                    break;
                case 4:
                    System.out.println("Logging out " + currentAccount.getAccountHolderName() + "...");
                    currentAccount = null; // This will trigger login prompt in the next iteration of run()
                    break;
                case 5:
                    System.out.println("Exiting ATM. Thank you for using our services!");
                    System.exit(0); // Direct exit.
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number for menu choice.");
            scanner.nextLine(); // Consume the invalid input
        }
    }

    private void performDeposit() {
        if (currentAccount == null) { // Should not happen if called from displayMenuAndProcessActions
            System.out.println("Critical Error: performDeposit called without a logged-in account.");
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

    private void performWithdraw() {
        if (currentAccount == null) { // Should not happen
            System.out.println("Critical Error: performWithdraw called without a logged-in account.");
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
}
