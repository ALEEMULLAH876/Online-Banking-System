public class Transaction {

    private long accountNumber;
    private String transactionType;
    private double amount;

    public Transaction(long accountNumber,
                       String transactionType,
                       double amount) {

        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    public long getAccountNumber() {

        return accountNumber;
    }

    public String getTransactionType() {

        return transactionType;
    }

    public double getAmount() {

        return amount;
    }
}