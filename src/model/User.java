package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {
    /**
     * Name of the user:
     */
    private String firstName;
    private String lastName;
    /**
     * The id number of the user:
     * The user's ID is not deleted when the user quit this system.
     */
    private String uuid;
    /**
     * A byte array
     * Code to access user's account, an MD5 hash code:
     */
    private byte pinHash[];

    /**
     * The list of accounts for this user:
     * ArrayList can be used as a function
     */
    private ArrayList<Account> accounts;

    /**
     * Create a new user
     * @param firstName user's first name
     * @param lastName user's last name
     * @param pin the user's account pin number
     * @param theBank the bank object that holds the user as a customer
     */

    public User(String firstName, String lastName, String pin, Bank theBank) {
        // Set user's name
        this.firstName = firstName;
        this.lastName = lastName;

        // Store the pin's MD5 hash, rather than original value for security
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Get the bytes of the pin -> digest it to a different bytes -> store in pinHash
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("error: caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        // Get a uuid for the user
        this.uuid = theBank.getNewUserUUID();

        // Constructor, create an empty list of accounts
        this.accounts = new ArrayList<Account>();

        // Print log message
        System.out.printf("New user %s, %s, with ID %s created.%n",
                lastName,
                firstName,
                this.uuid);
    }

    /**
     * Get the user's first name
     * @return the first name
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * add an account for the user
     * @param account account to add
     */
    public void addAccount(Account account) {
        // Notice accounts is now been used as a function
        this.accounts.add(account);
    }

    public String getUUID() {
        return this.uuid;
    }

    /**
     * Check whether a given pin matches the true User's pin
     * @param aPin the pin to check
     * @return whether the pin is valid or not
     */
    public boolean validatePin(String aPin) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(aPin.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("error: caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }
        return false; // The 3rd possibility (Java is smart)
    }


    /**
     * Print summaries for each account that the user owns
     */
    public void printAccountsSummary() {
        // Loop through all the accounts of the particular user
        System.out.printf("%n%s's accounts summary%n", this.firstName);
        for (int i = 0; i < this.accounts.size(); i++) {
            System.out.printf(" %d) %s%n",
                    i+1, // index of the accounts for user to select
                    this.accounts.get(i).getSummaryLine());
        }
        System.out.println();
    }

    /**
     * Get the numbers of accounts that the user owns
     * @return
     */
    public int numberOfAccounts() {
        return this.accounts.size();
    }


    /**
     * Print transactions from a particular account
     * @param acctIndex the index of the account in ArrayList
     */
    public void printAcctTransHistory(int acctIndex) {
        this.accounts.get(acctIndex).printTransHistory();
    }

    /**
     * Get the balance of the a particular account
     * @param acctIndex index of the account in ArrayList
     * @return the amount in double
     */
    public double getAccountBalance(int acctIndex) {
        return this.accounts.get(acctIndex).getBalance();
    }

    /**
     * Get the UUID of the a particular account
     * @param acctIndex the index of the account in ArrayList
     * @return the account uuid
     */
    public String getAcctUUID(int acctIndex) {
        return this.accounts.get(acctIndex).getUUID();
    }

    /**
     * Add a transaction through user to a particular account
     * @param acctIndex index of the account in the ArrayList
     * @param amount amount to transfer
     * @param memo memo of the transaction
     */
    public void addAcctTransaction(int acctIndex, double amount, String memo) {
        this.accounts.get(acctIndex).addTransaction(amount, memo);
    }
}
