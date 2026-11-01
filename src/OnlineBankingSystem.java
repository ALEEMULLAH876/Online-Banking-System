import java.util.Scanner;

public class OnlineBankingSystem {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        BankingSystem bankingSystem =
                new BankingSystem();


        // =========================
        // MAIN MENU
        // =========================

        while (true) {

            System.out.println(
                    "\n===== ONLINE BANKING SYSTEM =====");

            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();


            // REGISTER

            if (choice == 1) {

                bankingSystem.register();
            }


            // LOGIN

            else if (choice == 2) {

                String username =
                        bankingSystem.login();


                if (username != null) {


                    // =========================
                    // USER MENU
                    // =========================

                    while (true) {

                        System.out.println(
                                "\n===== USER MENU =====");

                        System.out.println(
                                "1. Create Bank Account");

                        System.out.println(
                                "2. View Account");

                        System.out.println(
                                "3. Deposit Money");

                        System.out.println(
                                "4. Withdraw Money");

                        System.out.println(
                                "5. Transfer Money");

                        System.out.println(
                                "6. Transaction History");

                        System.out.println(
                                "7. Logout");

                        System.out.print(
                                "Enter your choice: ");


                        int userChoice =
                                scanner.nextInt();

                        scanner.nextLine();


                        if (userChoice == 1) {

                            bankingSystem.createAccount(
                                    username);
                        }


                        else if (userChoice == 2) {

                            bankingSystem.viewAccount(
                                    username);
                        }


                        else if (userChoice == 3) {

                            bankingSystem.depositMoney(
                                    username);
                        }


                        else if (userChoice == 4) {

                            bankingSystem.withdrawMoney(
                                    username);
                        }


                        else if (userChoice == 5) {

                            bankingSystem.transferMoney(
                                    username);
                        }


                        else if (userChoice == 6) {

                            bankingSystem.transactionHistory(
                                    username);
                        }


                        else if (userChoice == 7) {

                            System.out.println(
                                    "Logged out successfully.");

                            break;
                        }


                        else {

                            System.out.println(
                                    "Invalid choice!");
                        }
                    }
                }
            }


            // EXIT

            else if (choice == 3) {

                System.out.println(
                        "Thank you for using Online Banking System.");

                break;
            }


            else {

                System.out.println(
                        "Invalid choice!");
            }
        }

        scanner.close();
    }
}