package model;

import java.util.ArrayList;
import java.util.Random;

public class Bank {
    private String name;
    private ArrayList<User> users;
    /**
     * Accounts in general for all the users
     */
    private ArrayList<Account> accounts;

    /**
     * Create a new Bank object with empty list of users and accounts
     * @param name bank's name
     */
    public Bank(String name) {
        this.name = name;
        this.users = new ArrayList<User>();
        this.accounts = new ArrayList<Account>();
    }

    /**
     * Get the name of the bank
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Generate a new uuid for a user
     * @return the uuid
     */
    public String getNewUserUUID() {
        // Make sure first it doesn't exist already in the accounts
        // initialize
        String uuid;
        Random rng = new Random();
        int len = 6;
        boolean nonUnique; // A boolean flag

        // Keep generating a new ID until we found a unique id:, until the condition is false
        do {
            // 1. Generate an ID 10 exclusive 0 inclusive, for len's time
            uuid = "";
            nonUnique = false;
            for (int c = 0; c < len; c++) {
                uuid += ((Integer)rng.nextInt(10)).toString();
            }

            // 2. Check in all the users to see if it's unique
            for (User user : this.users) {
                // If the generated ID is equal to one user's uuid
                if (uuid.compareTo(user.getUUID()) == 0) {
                    nonUnique = true;
                    break; // break so continue the loop until we found it
                }
            }
        } while (nonUnique); // while nonUnique = true

        return uuid;
    }

    /**
     * Generate a new uuid for an account
     * @return the uuid
     */
    public String getNewAccountUUID() {

        String uuid;
        Random rng = new Random();
        int len = 10;
        boolean nonUnique; // A boolean flag

        // Keep generating a new ID until we found a unique id:, until the condition is false
        do {
            // 1. Generate an ID 10 exclusive 0 inclusive, for len's time
            uuid = "";
            nonUnique = false;
            for (int c = 0; c < len; c++) {
                uuid += ((Integer)rng.nextInt(10)).toString();
            }

            // 2. Check in all the accounts to see if it's unique
            for (Account account : this.accounts) {
                // If the generated ID is equal to one account's uuid
                if (uuid.compareTo(account.getUUID()) == 0) {
                    nonUnique = true;
                    break; // break so continue the loop until we found it
                }
            }
        } while (nonUnique); // while nonUnique = true

        return uuid;
    }

    /**
     * Add an account
     * @param account the account to add
     */
    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    /**
     * Create a new User for the bank
     * @param firstName user's first name
     * @param lastName user's last name
     * @param pin user's pin
     * @return User object
     */
    public User addUser(String firstName, String lastName, String pin) {

        // Create a new User object and add it to the users list
        User newUser = new User(firstName, lastName, pin, this);
        this.users.add(newUser);

        // Create a savings account for the user
        Account newAccount = new Account("Savings", newUser, this);

        // Add account to bank and holder list, link it with it's holder and bank
        newUser.addAccount(newAccount);
        // Add an account to this bank
        this.accounts.add(newAccount);

        return newUser;
    }

    /**
     * Get the User object associated with a particular userId and pin, if they are valid.
     * @param userId uuid of the user to log in
     * @param pin users' pin
     * @return the User object, if the log in success, if no, return null
     */
    public User userLogin(String userId, String pin) {
        // Find the User id
        for (User user : users) {
            if (user.getUUID().equals(userId) && user.validatePin(pin)) {
                return user;
            }
        }

        // If we haven't found the user or the pin is incorrect
        return null;
    }

}
