import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class ATMTest {

    private ATM atm;
    private final String testAccount1Num = "ACC001"; // John Doe
    private final String testAccount2Num = "ACC002"; // Jane Smith
    private final int testAccount1InitialBalance = 1000;

    // Helper to create a Scanner from a String input
    private Scanner createScanner(String input) {
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        return new Scanner(inputStream);
    }

    @BeforeEach
    void setUp() {
        BankAccount.resetNextAccountNumberSuffixForTesting(); // Reset for predictable account numbers
        atm = new ATM(); // ATM constructor creates ACC001, ACC002, ACC003
    }

    @Test
    void testLoginSuccess() {
        Scanner scanner = createScanner(testAccount1Num + "\n");
        assertTrue(atm.login(scanner), "Login should succeed with correct account number.");
        assertNotNull(atm.getCurrentAccount(), "Current account should be set after successful login.");
        assertEquals(testAccount1Num, atm.getCurrentAccount().getAccountNumber(), "Logged in account number should match.");
        assertEquals("John Doe", atm.getCurrentAccount().getAccountHolderName(), "Logged in account holder name should match.");
    }

    @Test
    void testLoginFailure_WrongAccountNumber() {
        Scanner scanner = createScanner("ACC999\n");
        assertFalse(atm.login(scanner), "Login should fail with incorrect account number.");
        assertNull(atm.getCurrentAccount(), "Current account should be null after failed login.");
    }

    @Test
    void testLoginFailure_EmptyInput() {
        Scanner scanner = createScanner("\n"); // Simulates user just pressing Enter
        // Scanner might wait for actual input or read empty string depending on its state.
        // Assuming scanner.next() will block or throw if truly empty and newline is delimiter.
        // If scanner.next() reads an empty string:
        assertFalse(atm.login(scanner), "Login should fail with empty account number.");
        assertNull(atm.getCurrentAccount(), "Current account should be null after failed login.");
    }

    @Test
    void testPerformDeposit_Success() {
        // Login first
        atm.login(createScanner(testAccount1Num + "\n"));
        assertNotNull(atm.getCurrentAccount(), "Login required for deposit test.");

        Scanner depositScanner = createScanner("500\n");
        atm.performDeposit(depositScanner);
        assertEquals(testAccount1InitialBalance + 500, atm.getCurrentAccount().getBalance(), "Balance should increase after deposit.");
    }

    @Test
    void testPerformDeposit_InvalidAmount_Negative() {
        atm.login(createScanner(testAccount1Num + "\n"));
        assertNotNull(atm.getCurrentAccount());

        Scanner depositScanner = createScanner("-100\n"); // Invalid deposit amount
        atm.performDeposit(depositScanner);
        assertEquals(testAccount1InitialBalance, atm.getCurrentAccount().getBalance(), "Balance should not change with negative deposit.");
    }

    @Test
    void testPerformDeposit_InvalidAmount_NonNumeric() {
        atm.login(createScanner(testAccount1Num + "\n"));
        assertNotNull(atm.getCurrentAccount());
        int initialBalance = atm.getCurrentAccount().getBalance();

        Scanner depositScanner = createScanner("abc\n"); // Non-numeric input
        atm.performDeposit(depositScanner);
        assertEquals(initialBalance, atm.getCurrentAccount().getBalance(), "Balance should not change with non-numeric deposit amount.");
    }


    @Test
    void testPerformWithdraw_Success() {
        atm.login(createScanner(testAccount1Num + "\n"));
        assertNotNull(atm.getCurrentAccount());

        Scanner withdrawScanner = createScanner("200\n");
        atm.performWithdraw(withdrawScanner);
        assertEquals(testAccount1InitialBalance - 200, atm.getCurrentAccount().getBalance(), "Balance should decrease after withdrawal.");
    }

    @Test
    void testPerformWithdraw_InsufficientFunds() {
        atm.login(createScanner(testAccount1Num + "\n"));
        assertNotNull(atm.getCurrentAccount());

        Scanner withdrawScanner = createScanner((testAccount1InitialBalance + 100) + "\n"); // More than balance
        atm.performWithdraw(withdrawScanner);
        assertEquals(testAccount1InitialBalance, atm.getCurrentAccount().getBalance(), "Balance should not change on insufficient funds withdrawal.");
    }

    @Test
    void testPerformWithdraw_InvalidAmount_Negative() {
        atm.login(createScanner(testAccount1Num + "\n"));
        assertNotNull(atm.getCurrentAccount());
        int initialBalance = atm.getCurrentAccount().getBalance();

        Scanner withdrawScanner = createScanner("-100\n");
        atm.performWithdraw(withdrawScanner);
        assertEquals(initialBalance, atm.getCurrentAccount().getBalance(), "Balance should not change on negative withdrawal amount.");
    }

    @Test
    void testPerformWithdraw_InvalidAmount_NonNumeric() {
        atm.login(createScanner(testAccount1Num + "\n"));
        assertNotNull(atm.getCurrentAccount());
        int initialBalance = atm.getCurrentAccount().getBalance();

        Scanner withdrawScanner = createScanner("xyz\n");
        atm.performWithdraw(withdrawScanner);
        assertEquals(initialBalance, atm.getCurrentAccount().getBalance(), "Balance should not change on non-numeric withdrawal amount.");
    }

    // Tests for displayMenuAndProcessActions
    @Test
    void testDisplayMenu_ShowBalance_RedirectsToContinue() {
        atm.login(createScanner(testAccount1Num + "\n")); // Login
        assertNotNull(atm.getCurrentAccount());

        // Capture console output to verify showBalance was called (optional)
        PrintStream originalOut = System.out;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));

        Scanner menuScanner = createScanner("1\n"); // 1: Show Balance
        int action = atm.displayMenuAndProcessActions(menuScanner);
        assertEquals(ATM.ACTION_CONTINUE, action, "Action should be CONTINUE after showing balance.");

        // Restore System.out and check output (optional)
        System.setOut(originalOut);
        assertTrue(bos.toString().contains("Balance: " + atm.getCurrentAccount().getBalance()), "Show balance output not found.");
    }

    @Test
    void testDisplayMenu_Deposit_RedirectsToContinue() {
        atm.login(createScanner(testAccount1Num + "\n"));
        assertNotNull(atm.getCurrentAccount());
        // For deposit, it will ask for amount. Provide it.
        Scanner menuScanner = createScanner("2\n50\n"); // 2: Deposit, Amount: 50
        int action = atm.displayMenuAndProcessActions(menuScanner);
        // displayMenuAndProcessActions calls performDeposit which consumes the "50"
        // So, the "50" must be part of the input for the scanner passed to displayMenuAndProcessActions
        assertEquals(ATM.ACTION_CONTINUE, action);
        assertEquals(testAccount1InitialBalance + 50, atm.getCurrentAccount().getBalance());
    }

    @Test
    void testDisplayMenu_Withdraw_RedirectsToContinue() {
        atm.login(createScanner(testAccount1Num + "\n"));
        assertNotNull(atm.getCurrentAccount());
        Scanner menuScanner = createScanner("3\n30\n"); // 3: Withdraw, Amount: 30
        int action = atm.displayMenuAndProcessActions(menuScanner);
        assertEquals(ATM.ACTION_CONTINUE, action);
        assertEquals(testAccount1InitialBalance - 30, atm.getCurrentAccount().getBalance());
    }


    @Test
    void testDisplayMenu_Logout() {
        atm.login(createScanner(testAccount1Num + "\n"));
        assertNotNull(atm.getCurrentAccount(), "Must be logged in to test logout.");

        Scanner menuScanner = createScanner("4\n"); // 4: Logout
        int action = atm.displayMenuAndProcessActions(menuScanner);
        assertEquals(ATM.ACTION_LOGOUT, action, "Action should be LOGOUT.");
        assertNull(atm.getCurrentAccount(), "Current account should be null after logout action.");
    }

    @Test
    void testDisplayMenu_Exit() {
        atm.login(createScanner(testAccount1Num + "\n"));
        assertNotNull(atm.getCurrentAccount(), "Must be logged in to test exit action.");

        Scanner menuScanner = createScanner("5\n"); // 5: Exit ATM
        int action = atm.displayMenuAndProcessActions(menuScanner);
        assertEquals(ATM.ACTION_EXIT_SYSTEM, action, "Action should be EXIT_SYSTEM.");
        // currentAccount might still be set here, as System.exit() would be called in Main.
        // The method itself just returns the action.
        assertNotNull(atm.getCurrentAccount(), "Current account should still be set, Main handles exit.");
    }

    @Test
    void testDisplayMenu_InvalidChoice() {
        atm.login(createScanner(testAccount1Num + "\n"));
        assertNotNull(atm.getCurrentAccount());

        Scanner menuScanner = createScanner("9\n"); // Invalid menu choice
        int action = atm.displayMenuAndProcessActions(menuScanner);
        assertEquals(ATM.ACTION_CONTINUE, action, "Action should be CONTINUE on invalid menu choice.");
    }

    @Test
    void testDisplayMenu_InvalidInput_NonNumericChoice() {
        atm.login(createScanner(testAccount1Num + "\n"));
        assertNotNull(atm.getCurrentAccount());

        Scanner menuScanner = createScanner("xyz\n"); // Non-numeric menu choice
        int action = atm.displayMenuAndProcessActions(menuScanner);
        assertEquals(ATM.ACTION_CONTINUE, action, "Action should be CONTINUE on non-numeric menu choice.");
    }
     @Test
    void testLogoutMethodDirectly() {
        atm.login(createScanner(testAccount1Num + "\n"));
        assertNotNull(atm.getCurrentAccount(), "Must be logged in to test logout.");
        atm.logout();
        assertNull(atm.getCurrentAccount(), "Current account should be null after direct logout call.");
    }

    @Test
    void testIsUserLoggedIn() {
        assertFalse(atm.isUserLoggedIn(), "Initially, no user should be logged in.");
        atm.login(createScanner(testAccount1Num + "\n"));
        assertTrue(atm.isUserLoggedIn(), "User should be logged in after successful login.");
        atm.logout();
        assertFalse(atm.isUserLoggedIn(), "User should not be logged in after logout.");
    }
}
