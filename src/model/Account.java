package model;

import java.util.ArrayList;

public class Account {
    /**
     * Name of the account
     */
    private String name;

    /**
     * The account ID number
     */
    private String uuid;

    /**
     * The user object that owns the account
     */
    private User holder;

    /**
     * The list of the transactions of the account
     */
    private ArrayList<Transaction> transactions;

    /**
     *
     * @param name the name of the account
     * @param holder the user that holds this account
     * @param theBank the bank that issues the account
     */

    public Account(String name, User holder, Bank theBank) {
        // Set the account name and holder
        this.name = name;
        this.holder = holder;

        // Get new account uuid
        this.uuid = theBank.getNewAccountUUID();

        // initialize transactions
        this.transactions = new ArrayList<Transaction>();

    }

    public String getUUID() {
        return this.uuid;
    }

    /**
     * Get summary of the account
     * @return the string summary
     */
    public String getSummaryLine() {

        // Get the account's balance
        double balance = this.getBalance();

        // For User friendly, format the message depending on the balance
        if (balance >= 0) {
            return String.format("%s : $%.02f : %s", this.uuid, balance, this.name);
        } else {
            return String.format("%s : $(%.02f) : %s", this.uuid, balance, this.name);
        }
    }

    /**
     * Getting the balance by adding the amount of transactions
     * @return the balance in $
     */
    public double getBalance() {
        double balance = 0;
        for (Transaction transaction : this.transactions) {
            balance += transaction.getAmount();
        }
        return balance;
    }

    /**
     * Print the transaction histories of the account
     */
    public void printTransHistory() {
        System.out.printf("%nTransaction history for account %s:%n",
                this.uuid);
        if (this.transactions.size() > 0) {
            // Loop through and show from recent history to old ones
            for (int i = this.transactions.size() - 1; i >= 0; i--) {
                System.out.println(this.transactions.get(i).getSummaryLine());
            }
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        } else {
            System.out.println("No transactions yet for this account.");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
    }

    /**
     * Add the new transaction history to the ArrayList `transactions` once it happened
     * @param amount amount of the transaction
     * @param memo memo of the transaction
     */
    public void addTransaction(double amount, String memo) {
        // Create new transaction object to add to our list
        Transaction newTransaction = new Transaction(amount, memo, this);
        this.transactions.add(newTransaction);
    }
}
