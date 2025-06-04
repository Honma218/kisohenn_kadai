import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {

    private BankAccount account;
    private BankAccount account2; // For account number generation test

    @BeforeEach
    void setUp() {
        // Reset static counter for predictable account numbers in tests
        // This requires a method in BankAccount to reset the counter, or reflection.
        // For simplicity, we'll assume tests run in an order or BankAccount is modified.
        // Let's try to modify BankAccount to add a reset for testing or manage instance creation carefully.
        // For now, we will create accounts sequentially and test based on that.
        // To ensure clean state for account numbers, we'll handle it in the specific test.
        account = new BankAccount("Test User", 1000);
    }

    @Test
    void testDepositPositiveAmount() {
        account.deposit(500);
        assertEquals(1500, account.getBalance(), "Balance should be 1500 after depositing 500.");
    }

    @Test
    void testDepositZeroAmount() {
        account.deposit(0);
        assertEquals(1000, account.getBalance(), "Balance should remain 1000 after depositing 0.");
        // Current implementation prints "Deposit amount must be positive." - this is fine.
    }

    @Test
    void testDepositNegativeAmount() {
        account.deposit(-100);
        assertEquals(1000, account.getBalance(), "Balance should remain 1000 after attempting to deposit -100.");
        // Current implementation prints "Deposit amount must be positive."
    }

    @Test
    void testWithdrawSufficientFunds() {
        account.withdraw(200);
        assertEquals(800, account.getBalance(), "Balance should be 800 after withdrawing 200.");
    }

    @Test
    void testWithdrawMaxAmount() {
        account.withdraw(1000);
        assertEquals(0, account.getBalance(), "Balance should be 0 after withdrawing entire balance.");
    }

    @Test
    void testWithdrawInsufficientFunds() {
        account.withdraw(1200); // More than balance
        assertEquals(1000, account.getBalance(), "Balance should remain 1000 after attempting to withdraw 1200 (insufficient funds).");
        // Current implementation prints "Invalid withdrawal amount or insufficient balance."
    }

    @Test
    void testWithdrawZeroAmount() {
        account.withdraw(0);
        assertEquals(1000, account.getBalance(), "Balance should remain 1000 after attempting to withdraw 0.");
        // Current implementation prints "Invalid withdrawal amount or insufficient balance."
    }

    @Test
    void testWithdrawNegativeAmount() {
        account.withdraw(-100);
        assertEquals(1000, account.getBalance(), "Balance should remain 1000 after attempting to withdraw -100.");
        // Current implementation prints "Invalid withdrawal amount or insufficient balance."
    }

    // testShowBalance() is omitted as it prints to console; getBalance() is used for assertions.

    @Test
    void testAccountNumberGeneration() {
        // Account 'account' (ACCxxx) is created in setUp.
        // To make this test robust, we need to ensure `nextAccountNumberSuffix` state.
        // Assuming BankAccount.nextAccountNumberSuffix is package-private or has a reset method for tests.
        // If not, this test depends on the order of test execution or initial state.
        // For this subtask, we'll assume it's okay to test sequential generation.
        // The first account 'account' will get ACC00X (e.g. ACC001 if tests start fresh)
        // The second account 'account2' should get ACC00(X+1)

        // To make this test independent, let's create accounts locally.
        // This means the static counter `nextAccountNumberSuffix` in BankAccount will be affected
        // by other tests if they run before this one and create BankAccount instances.
        // A proper solution would involve a reset mechanism for `nextAccountNumberSuffix`
        // or making it non-static and part of a factory.
        // For now, let's assume we're testing the sequence.

        BankAccount accTest1 = new BankAccount("User One", 100);
        BankAccount accTest2 = new BankAccount("User Two", 200);

        assertNotNull(accTest1.getAccountNumber(), "Account 1 number should not be null.");
        assertNotNull(accTest2.getAccountNumber(), "Account 2 number should not be null.");
        assertNotEquals(accTest1.getAccountNumber(), accTest2.getAccountNumber(), "Account numbers should be unique.");

        // Check format if possible (e.g., starts with "ACC" and ends with 3 digits)
        // Example: ACC001, ACC002 (if BankAccount.nextAccountNumberSuffix was reset to 1 before these)
        // This test is somewhat fragile without a reset for the static counter.
        // We can get the number from `account` created in setUp if its number is predictable.
        String initialAccNum = account.getAccountNumber(); // From setUp
        String accTest1Num = accTest1.getAccountNumber();
        String accTest2Num = accTest2.getAccountNumber();

        System.out.println("Account number from setUp: " + initialAccNum); // e.g. ACC001
        System.out.println("Account number for accTest1: " + accTest1Num); // e.g. ACC002
        System.out.println("Account number for accTest2: " + accTest2Num); // e.g. ACC003

        assertTrue(initialAccNum.matches("ACC\\d{3}"), "Account number from setUp should match format ACCddd.");
        assertTrue(accTest1Num.matches("ACC\\d{3}"), "Account number for accTest1 should match format ACCddd.");
        assertTrue(accTest2Num.matches("ACC\\d{3}"), "Account number for accTest2 should match format ACCddd.");

        // Assuming initialAccNum = "ACC001", accTest1Num = "ACC002", accTest2Num = "ACC003"
        // This requires knowing the state of `nextAccountNumberSuffix`.
        // Let's verify the suffix increments.
        try {
            int suffixInitial = Integer.parseInt(initialAccNum.substring(3));
            int suffixTest1 = Integer.parseInt(accTest1Num.substring(3));
            int suffixTest2 = Integer.parseInt(accTest2Num.substring(3));

            assertEquals(suffixInitial + 1, suffixTest1, "accTest1 suffix should be incremented from initial account.");
            assertEquals(suffixTest1 + 1, suffixTest2, "accTest2 suffix should be incremented from accTest1.");

        } catch (NumberFormatException e) {
            fail("Account number suffix is not a valid number: " + e.getMessage());
        }
    }

    @Test
    void testConstructorInitialValues() {
        assertEquals("Test User", account.getAccountHolderName(), "Account holder name should be 'Test User'.");
        assertEquals(1000, account.getBalance(), "Initial balance should be 1000.");
        assertNotNull(account.getAccountNumber(), "Account number should not be null upon creation.");
        assertTrue(account.getAccountNumber().startsWith("ACC"), "Account number should start with 'ACC'.");
    }
}
