package model;

import java.util.Date;

public class Transaction {

    private double amount;
    private Date timestamp;
    /**
     * To note why for this transaction
     */
    private String memo;
    /**
     * Link to it's account
     */
    private Account inAccount;

    /**
     * Create a new transaction
     * @param amount the amount transacted
     * @param inAccount the account affected
     */
    public Transaction(double amount, Account inAccount) {
        this.amount = amount;
        this.inAccount = inAccount;
        this.timestamp = new Date(); // Today
        this.memo = "";
    }

    /**
     * Same as above, but with a memo
     * @param amount amount transacted
     * @param memo the memo for this transaction
     * @param inAccount account affected
     */
    public Transaction(double amount, String memo, Account inAccount) {
        // Call the above constructor first
        this(amount, inAccount);
        this.memo = memo;
    }

    /**
     * Get the amount of the transaction
     * @return the amount
     */
    public double getAmount() {
        return this.amount;
    }

    /**
     * Getting a string summarizing of the transaction
     * @return the summary string
     */
    public String getSummaryLine() {
        if (this.amount >= 0) {
            return String.format("%n%s : $%.02f : %s", this.timestamp.toString(), this.amount, this.memo);
        } else {
            return String.format("%n%s : $(%.02f) : %s", this.timestamp.toString(), -this.amount, this.memo);
        }
    }

}
