public class BankAccount {

    private long accountNumber;
    private String username;
    private String accountHolder;
    private String accountType;
    private double balance;

    public BankAccount(long accountNumber,
                       String username,
                       String accountHolder,
                       String accountType,
                       double balance) {

        this.accountNumber = accountNumber;
        this.username = username;
        this.accountHolder = accountHolder;
        this.accountType = accountType;
        this.balance = balance;
    }

    public long getAccountNumber() {

        return accountNumber;
    }

    public String getUsername() {

        return username;
    }

    public String getAccountHolder() {

        return accountHolder;
    }

    public String getAccountType() {

        return accountType;
    }

    public double getBalance() {

        return balance;
    }
}