package model;

import java.text.MessageFormat;
import java.util.Scanner;

// Interface of the bank
public class ATM {
    public static void main(String[] args) {
        // Initialize Scanner
        Scanner scanner = new Scanner(System.in);

        // Initialize Bank
        Bank theBank = new Bank("Bank of Center");

        // Add a User, which also creates an account
        User aUser = theBank.addUser("Lily", "Rose", "1234");

        // Add a checking account for this user
        Account anAccount = new Account("Checking", aUser, theBank);
        aUser.addAccount(anAccount);
        theBank.addAccount(anAccount);

        // Login prompt
        User curUser;
        while (true) {
            // Always pass the scanner instance with us for all the methods
            // Stay in login until success
            curUser = ATM.mainMenuPrompt(theBank, scanner);
            // Stay in main menu until user quits
            ATM.printUserMenu(curUser, scanner);
        }
    }

    /**
     * Print the ATM's login menu
     * @param theBank the Bank object whose accounts associated
     * @param scanner Scanner object for user input
     * @return authenticated User object
     */
    public static User mainMenuPrompt(Bank theBank, Scanner scanner) {
        // Initialize
        String userId;
        String pin;
        User authUser;

        // Prompt the user for login Id & pin until success
        do {
            System.out.printf("%n%nWelcome to %s%n%n", theBank.getName());
                System.out.println("Enter user ID: ");
                userId = scanner.next();
                System.out.println("Enter the pin: ");
                pin = scanner.next();

                // Try to get the userId and pin corresponding to the input
            authUser = theBank.userLogin(userId, pin);
            if (authUser == null) {
                System.out.printf("Incorrect user ID/pin combination." +
                        "Please try again."); // Don't give too much information, just say the combination is wrong
            }
        } while (authUser == null); // Continue looping until login success

        return authUser;
    }

    public static void printUserMenu(User theUser, Scanner scanner) {

        // Print a summary of the user's accounts
        theUser.printAccountsSummary();

        // Initialize
        int option;

        // User's menu
        do {
            System.out.printf("Welcome %s, what would you like to do?%n", theUser.getFirstName());

            System.out.println(" 1) Show account transaction history");
            System.out.println(" 2) Withdraw");
            System.out.println(" 3) Deposit");
            System.out.println(" 4) Transfer");
            System.out.println(" 5) Quit");
            System.out.println();
            System.out.print("Enter option: ");
            option = scanner.nextInt();

            // Tell user it's wrong if No login for 2, 3, 4, or out of the range of options
            if (option < 1 || option > 5) {
                System.out.println("Invalid option. Please choose 1~5");
            }
        } while (option < 1 || option > 5);

        // Process the option
        switch (option) {
            case 1:
                ATM.showTransactionHistory(theUser, scanner);
                break;
            case 2:
                ATM.withdrawFunds(theUser, scanner);
                break;
            case 3:
                ATM.depositFunds(theUser, scanner);
                break;
            case 4:
                ATM.transferFunds(theUser, scanner);
                break;
            case 5:
                scanner.nextLine();
                break;
        }

        // Re-display the menu until the user quit, by a recursive call
        if (option !=5 ) {
            ATM.printUserMenu(theUser, scanner);
        }
    }

    /**
     * Show the transaction histories of an account
     * @param theUser the logged-in user
     * @param scanner the Scanner for user input
     */
    public static void showTransactionHistory(User theUser, Scanner scanner) {
        // Ask the user which account he/she wants to look at:
        int theAcct;
        do {
            System.out.printf("Enter the number (1~%d) of the account," + " which transactions you want to see: ",
                    theUser.numberOfAccounts());
            theAcct = scanner.nextInt()-1;

            // If User enter wrong number
            if (theAcct < 0 || theAcct >= theUser.numberOfAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (theAcct < 0 || theAcct >= theUser.numberOfAccounts());

        // Print the transaction histories
        theUser.printAcctTransHistory(theAcct);
    }

    /**
     * Process the transaction from one account to another of the same user
     * @param theUser the logged-in User object
     * @param scanner the Scanner for user input
     */
    public static void transferFunds(User theUser, Scanner scanner) {
        // Initialize
        int fromAccount;
        int toAccount;
        double amount;
        double accountBalance; // Don't allow user to transfer too much at a time
        String memo;

        // Get the account to transfer from
        do {
            System.out.printf("Enter the number (1~%d) of the account you want to transfer FROM: ",
                    theUser.numberOfAccounts());
            fromAccount = scanner.nextInt()-1;

            // If User enter wrong number
            if (fromAccount < 0 || fromAccount >= theUser.numberOfAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (fromAccount < 0 || fromAccount >= theUser.numberOfAccounts());
        accountBalance = theUser.getAccountBalance(fromAccount);

        // Get the account to transfer to
        do {
            System.out.printf("Enter the number (1~%d) of the account you want to transfer TO: ",
                    theUser.numberOfAccounts());
            toAccount = scanner.nextInt()-1;

            // If User enter wrong number
            if (toAccount < 0 || toAccount >= theUser.numberOfAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (toAccount < 0 || toAccount >= theUser.numberOfAccounts());

        // Get the amount to transfer
        do {
            System.out.printf("Enter the amount to transfer (max $%.02f): $", accountBalance);
            amount = scanner.nextDouble();

            // If User enter wrong number
            if (amount < 0) {
                System.out.println("Amount should not be less than 0.");
            } else if (amount > accountBalance) {
                System.out.printf("Amount must not be greater than%n" + "the balance of $%.02f.%n", accountBalance);
            }
        } while (amount < 0 || amount > accountBalance);

        // Gobble up rest of previous input, 加上这句才会被记录用户输入memo, 否则会被跳过
        scanner.nextLine();

        // Get a memo
        System.out.println("Enter a memo: ");
        memo = scanner.nextLine();

        // Finally, do the transfer
        theUser.addAcctTransaction(fromAccount, -1*amount,
                MessageFormat.format("Transfer to account #{0} for: {1}", theUser.getAcctUUID(toAccount), memo));
        theUser.addAcctTransaction(toAccount, amount,
                MessageFormat.format("Transfer from account #{0} for: {1}", theUser.getAcctUUID(fromAccount), memo));
    }

    /**
     * Process a withdraw
     * @param theUser the logged-in User object
     * @param scanner the Scanner for user input
     */
    public static void withdrawFunds(User theUser, Scanner scanner) {
        // Initialize
        int account;
        double amount;
        double accountBalance;
        String memo;

        // Get the account to withdraw
        do {
            System.out.printf("Enter the number (1~%d) of the account you want to withdraw FROM: ",
                    theUser.numberOfAccounts());
            account = scanner.nextInt()-1;

            // If User enter wrong number
            if (account < 0 || account >= theUser.numberOfAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (account < 0 || account >= theUser.numberOfAccounts());
        accountBalance = theUser.getAccountBalance(account);

        // Get the amount the user want to withdraw
        do {
            System.out.printf("Enter the amount to withdraw (max $%.02f): $", accountBalance);
            amount = scanner.nextDouble();

            // If User enter wrong number
            if (amount < 0) {
                System.out.println("Amount should not be less than 0.");
            } else if (amount > accountBalance) {
                System.out.printf("Amount must not be greater than%n" + "the balance of $%.02f.%n", accountBalance);
            }
        } while (amount < 0 || amount > accountBalance);

        // Gobble up rest of previous input
        scanner.nextLine();

        // Get a memo
        System.out.println("Enter a memo: ");
        memo = scanner.nextLine();

        // Take into account the withdraw history
        theUser.addAcctTransaction(account, -1*amount, memo);
    }

    /**
     * Process a deposit
     * @param theUser the logged-in User object
     * @param scanner the Scanner for user input
     */
    public static void depositFunds(User theUser, Scanner scanner) {
        // Initialize
        int account;
        double amount;
        double accountBalance;
        String memo;

        // Get the account to deposit
        do {
            System.out.printf("Enter the number (1~%d) of the account you want to deposit TO: ",
                    theUser.numberOfAccounts());
            account = scanner.nextInt()-1;

            // If User enter wrong number
            if (account < 0 || account >= theUser.numberOfAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (account < 0 || account >= theUser.numberOfAccounts());
        accountBalance = theUser.getAccountBalance(account);

        // Get the amount the user want to deposit
        do {
            System.out.printf("Enter the amount to deposit (current balance is $%.02f): $", accountBalance);
            amount = scanner.nextDouble();

            // If User enter wrong number
            if (amount < 0) {
                System.out.println("Amount should not be less than 0.");
            }
        } while (amount < 0);

        // Gobble up rest of previous input
        scanner.nextLine();

        // Get a memo
        System.out.println("Enter a memo: ");
        memo = scanner.nextLine();

        // Take into account the withdraw history
        theUser.addAcctTransaction(account, amount, memo);
    }

}
