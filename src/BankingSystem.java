import java.sql.*;
import java.util.Scanner;

public class BankingSystem {

    private Scanner scanner = new Scanner(System.in);

    // =========================
    // REGISTER
    // =========================

    public void register() {

        System.out.println("\n--- Registration ---");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (username.isEmpty() || password.isEmpty()) {

            System.out.println("Username and password cannot be empty!");
            return;
        }

        String sql =
                "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            statement.executeUpdate();

            System.out.println("Registration successful!");

        } catch (SQLException e) {

            System.out.println("Registration failed!");
            System.out.println(e.getMessage());
        }
    }


    // =========================
    // LOGIN
    // =========================

    public String login() {

        System.out.println("\n--- Login ---");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String sql =
                "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                System.out.println("Login successful!");

                return username;

            } else {

                System.out.println("Invalid username or password!");

                return null;
            }

        } catch (SQLException e) {

            System.out.println("Login failed!");
            System.out.println(e.getMessage());

            return null;
        }
    }


    // =========================
    // CREATE ACCOUNT
    // =========================

    public void createAccount(String username) {

        System.out.println("\n--- Create Bank Account ---");

        System.out.print("Enter account number: ");
        long accountNumber = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Enter account holder name: ");
        String accountHolder = scanner.nextLine();

        System.out.print("Enter account type (Savings/Current): ");
        String accountType = scanner.nextLine();

        if (!accountType.equalsIgnoreCase("Savings")
                && !accountType.equalsIgnoreCase("Current")) {

            System.out.println("Invalid account type!");
            return;
        }

        System.out.print("Enter initial balance: ");
        double balance = scanner.nextDouble();
        scanner.nextLine();

        if (balance < 0) {

            System.out.println("Balance cannot be negative!");
            return;
        }

        String sql =
                "INSERT INTO accounts " +
                "(account_number, username, account_holder, account_type, balance) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, accountNumber);
            statement.setString(2, username);
            statement.setString(3, accountHolder);
            statement.setString(4, accountType);
            statement.setDouble(5, balance);

            statement.executeUpdate();

            System.out.println("Bank account created successfully!");

        } catch (SQLException e) {

            System.out.println("Account creation failed!");
            System.out.println(e.getMessage());
        }
    }


    // =========================
    // VIEW ACCOUNT
    // =========================

    public void viewAccount(String username) {

        System.out.println("\n--- Account Details ---");

        String sql =
                "SELECT * FROM accounts WHERE username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                System.out.println(
                        "Account Number: "
                        + result.getLong("account_number"));

                System.out.println(
                        "Account Holder: "
                        + result.getString("account_holder"));

                System.out.println(
                        "Account Type: "
                        + result.getString("account_type"));

                System.out.println(
                        "Balance: ₹"
                        + result.getDouble("balance"));

            } else {

                System.out.println("No bank account found!");
            }

        } catch (SQLException e) {

            System.out.println("Failed to view account!");
            System.out.println(e.getMessage());
        }
    }


    // =========================
    // DEPOSIT MONEY
    // =========================

    public void depositMoney(String username) {

        System.out.println("\n--- Deposit Money ---");

        System.out.print("Enter deposit amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {

            System.out.println(
                    "Deposit amount must be greater than 0!");

            return;
        }

        String findAccount =
                "SELECT account_number, balance " +
                "FROM accounts WHERE username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(findAccount)) {

            statement.setString(1, username);

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                long accountNumber =
                        result.getLong("account_number");

                double currentBalance =
                        result.getDouble("balance");

                double newBalance =
                        currentBalance + amount;


                // Update balance

                String updateBalance =
                        "UPDATE accounts " +
                        "SET balance = ? " +
                        "WHERE account_number = ?";

                try (PreparedStatement updateStatement =
                             connection.prepareStatement(updateBalance)) {

                    updateStatement.setDouble(1, newBalance);
                    updateStatement.setLong(2, accountNumber);

                    updateStatement.executeUpdate();
                }


                // Record transaction

                recordTransaction(
                        connection,
                        accountNumber,
                        "DEPOSIT",
                        amount
                );


                System.out.println(
                        "Money deposited successfully!");

                System.out.println(
                        "Deposited Amount: ₹" + amount);

                System.out.println(
                        "New Balance: ₹" + newBalance);

            } else {

                System.out.println("No bank account found!");
            }

        } catch (SQLException e) {

            System.out.println("Deposit failed!");
            System.out.println(e.getMessage());
        }
    }


    // =========================
    // WITHDRAW MONEY
    // =========================

    public void withdrawMoney(String username) {

        System.out.println("\n--- Withdraw Money ---");

        System.out.print("Enter withdrawal amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {

            System.out.println(
                    "Withdrawal amount must be greater than 0!");

            return;
        }

        String findAccount =
                "SELECT account_number, balance " +
                "FROM accounts WHERE username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(findAccount)) {

            statement.setString(1, username);

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                long accountNumber =
                        result.getLong("account_number");

                double currentBalance =
                        result.getDouble("balance");

                if (amount > currentBalance) {

                    System.out.println(
                            "Insufficient balance!");

                    return;
                }

                double newBalance =
                        currentBalance - amount;


                // Update balance

                String updateBalance =
                        "UPDATE accounts " +
                        "SET balance = ? " +
                        "WHERE account_number = ?";

                try (PreparedStatement updateStatement =
                             connection.prepareStatement(updateBalance)) {

                    updateStatement.setDouble(1, newBalance);
                    updateStatement.setLong(2, accountNumber);

                    updateStatement.executeUpdate();
                }


                // Record transaction

                recordTransaction(
                        connection,
                        accountNumber,
                        "WITHDRAW",
                        amount
                );


                System.out.println(
                        "Money withdrawn successfully!");

                System.out.println(
                        "Withdrawn Amount: ₹" + amount);

                System.out.println(
                        "New Balance: ₹" + newBalance);

            } else {

                System.out.println("No bank account found!");
            }

        } catch (SQLException e) {

            System.out.println("Withdrawal failed!");
            System.out.println(e.getMessage());
        }
    }


    // =========================
    // TRANSFER MONEY
    // =========================

    public void transferMoney(String username) {

        System.out.println("\n--- Transfer Money ---");

        System.out.print("Enter receiver account number: ");
        long receiverAccount = scanner.nextLong();

        System.out.print("Enter transfer amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {

            System.out.println(
                    "Transfer amount must be greater than 0!");

            return;
        }

        String senderSQL =
                "SELECT account_number, balance " +
                "FROM accounts WHERE username = ?";

        String receiverSQL =
                "SELECT account_number " +
                "FROM accounts WHERE account_number = ?";

        try (Connection connection = DBConnection.getConnection()) {

            // Find sender

            long senderAccount;
            double senderBalance;

            try (PreparedStatement statement =
                         connection.prepareStatement(senderSQL)) {

                statement.setString(1, username);

                ResultSet result = statement.executeQuery();

                if (!result.next()) {

                    System.out.println(
                            "Sender account not found!");

                    return;
                }

                senderAccount =
                        result.getLong("account_number");

                senderBalance =
                        result.getDouble("balance");
            }


            // Check receiver

            try (PreparedStatement statement =
                         connection.prepareStatement(receiverSQL)) {

                statement.setLong(1, receiverAccount);

                ResultSet result = statement.executeQuery();

                if (!result.next()) {

                    System.out.println(
                            "Receiver account not found!");

                    return;
                }
            }


            // Cannot transfer to same account

            if (senderAccount == receiverAccount) {

                System.out.println(
                        "You cannot transfer money to the same account!");

                return;
            }


            // Check balance

            if (amount > senderBalance) {

                System.out.println(
                        "Insufficient balance!");

                return;
            }


            double newSenderBalance =
                    senderBalance - amount;


            // Remove money from sender

            String withdrawSQL =
                    "UPDATE accounts " +
                    "SET balance = ? " +
                    "WHERE account_number = ?";

            try (PreparedStatement statement =
                         connection.prepareStatement(withdrawSQL)) {

                statement.setDouble(1, newSenderBalance);
                statement.setLong(2, senderAccount);

                statement.executeUpdate();
            }


            // Add money to receiver

            String depositSQL =
                    "UPDATE accounts " +
                    "SET balance = balance + ? " +
                    "WHERE account_number = ?";

            try (PreparedStatement statement =
                         connection.prepareStatement(depositSQL)) {

                statement.setDouble(1, amount);
                statement.setLong(2, receiverAccount);

                statement.executeUpdate();
            }


            // Record sender transaction

            recordTransaction(
                    connection,
                    senderAccount,
                    "TRANSFER_SENT",
                    amount
            );


            // Record receiver transaction

            recordTransaction(
                    connection,
                    receiverAccount,
                    "TRANSFER_RECEIVED",
                    amount
            );


            System.out.println(
                    "Money transferred successfully!");

            System.out.println(
                    "Transferred Amount: ₹" + amount);

            System.out.println(
                    "Remaining Balance: ₹"
                    + newSenderBalance);

        } catch (SQLException e) {

            System.out.println("Transfer failed!");
            System.out.println(e.getMessage());
        }
    }


    // =========================
    // RECORD TRANSACTION
    // =========================

    private void recordTransaction(
            Connection connection,
            long accountNumber,
            String transactionType,
            double amount) throws SQLException {

        String sql =
                "INSERT INTO transactions " +
                "(account_number, transaction_type, amount) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, accountNumber);
            statement.setString(2, transactionType);
            statement.setDouble(3, amount);

            statement.executeUpdate();
        }
    }


    // =========================
    // TRANSACTION HISTORY
    // =========================

    public void transactionHistory(String username) {

        System.out.println("\n--- Transaction History ---");

        String sql =
                "SELECT t.transaction_type, " +
                "t.amount, t.transaction_time " +
                "FROM transactions t " +
                "JOIN accounts a " +
                "ON t.account_number = a.account_number " +
                "WHERE a.username = ? " +
                "ORDER BY t.transaction_time DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);

            ResultSet result = statement.executeQuery();

            boolean found = false;

            while (result.next()) {

                found = true;

                System.out.println(
                        "Type: "
                        + result.getString("transaction_type"));

                System.out.println(
                        "Amount: ₹"
                        + result.getDouble("amount"));

                System.out.println(
                        "Time: "
                        + result.getTimestamp("transaction_time"));

                System.out.println("---------------------------");
            }

            if (!found) {

                System.out.println(
                        "No transactions found!");
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to load transaction history!");

            System.out.println(e.getMessage());
        }
    }
}