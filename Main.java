import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ATM atm = new ATM();

        if (!atm.hasAccounts()) {
            System.out.println("No accounts configured in the ATM. Exiting.");
            scanner.close();
            return;
        }

        boolean running = true;
        while (running) {
            if (!atm.isUserLoggedIn()) {
                System.out.println("\nWelcome to the ATM. Please login.");
                if (!atm.login(scanner)) { // login now uses the passed scanner
                    System.out.println("Login failed. Would you like to try again? (yes/no)");
                    String choice = scanner.nextLine().trim().toLowerCase();
                    if (!choice.equals("yes")) {
                        System.out.println("Exiting ATM. Thank you!");
                        running = false; // Exit the main loop
                    }
                    // If "yes", loop continues, and login will be prompted again
                }
                // If login was successful, atm.currentAccount is now set.
            }

            if (atm.isUserLoggedIn()) {
                int action = atm.displayMenuAndProcessActions(scanner); // this method now uses passed scanner

                switch (action) {
                    case ATM.ACTION_LOGOUT:
                        // currentAccount is already set to null by atm.logout() via displayMenuAndProcessActions
                        System.out.println("You have been logged out.");
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
                        System.out.println("Unknown action received. Continuing.");
                        break;
                }
            }

            // Check for scanner closure issue in non-interactive environments (less critical now)
            if (!running && !scanner.hasNextLine() && System.console() == null) {
                 System.out.println("ATM session ended.");
            }
        }

        System.out.println("ATM application terminated.");
        scanner.close();
    }
}
