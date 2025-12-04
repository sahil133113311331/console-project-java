package app;

import service.BankService;
import service.impl.BankServiceImpl;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        BankService bankService = new BankServiceImpl();
        System.out.println("Welcome to console bank");
        boolean running = true;
        while (running) {
            System.out.println("""
                        1) Open Account
                        2) Deposit
                        3) Withdraw
                        4) Transfer
                        5) Account Statement
                        6) List Accounts
                        7) Search Accounts by Customer Name
                        8) Balance
                        0) Exit
                    """);
            System.out.println("Enter choice: ");
            String choice = input.nextLine().trim();
            switch(choice){
                case "1" -> openAccount(input,bankService);
                case "2" -> deposit(input, bankService);
                case "3" -> withdraw(input,bankService);
                case "4" -> transfer(input,bankService);
                case "5" -> statement(input,bankService);
                case "6" -> listAccounts(input,bankService);
                case "7" -> searchAccounts(input);
                case "8" -> balance(input,bankService);
                case "0" ->  running = false;
                
            }
        }
    }

    private static void balance(Scanner input,BankService bankService) {
        System.out.println("Enter AccountNumber: ");
        String accountNumber = input.nextLine().trim();
        Double amount = bankService.balance(accountNumber);
        System.out.println("Your balance is " + amount);
    }

    private static void openAccount(Scanner input,BankService bankService) {
        System.out.println("Enter Customer Name: ");
        String name = input.nextLine().trim();
        System.out.println("Enter Customer Email: ");
        String email = input.nextLine().trim();
        System.out.println("Account Type (SAVING/CURRENT): ");
        String type = input.nextLine().trim();
        System.out.println("Enter Initial Deposit (blank for 0): ");
        String amountString = input.nextLine().trim();
        Double initialDeposit = Double.valueOf(amountString);
        String accountNumber=bankService.openAccount(name,email,type);
        if(initialDeposit>0)
        {
            bankService.deposit(accountNumber,initialDeposit,"initial deposit");
        }
        System.out.println("Account opened successfully");
    }

    private static void deposit(Scanner input, BankService bankService) {
        System.out.println("Enter Account Number: ");
        String accountNumber = input.nextLine().trim();
        System.out.println("Enter Deposit Amount: ");
        Double amount = Double.valueOf(input.nextLine().trim());
        System.out.println("Enter Note:");
        String note = input.nextLine().trim();
        bankService.deposit(accountNumber,amount,note);
        System.out.println("Account deposited successfully");


    }

    private static void withdraw(Scanner input, BankService bankService) {
        System.out.println("Enter Account Number: ");
        String accountNumber = input.nextLine().trim();
        System.out.println("Enter Withdraw Amount: ");
        Double amount = Double.valueOf(input.nextLine().trim());
        System.out.println("Enter Note:");
        String note = input.nextLine().trim();
        bankService.withdraw(accountNumber,amount,note);
        System.out.println("withdrawal successfully");
    }

    private static void transfer(Scanner input, BankService bankService) {
        System.out.println("Enter FROM Account Number: ");
        String accountNumber = input.nextLine().trim();
        System.out.println("Enter TO Account Number: ");
        String accountNumber2 = input.nextLine().trim();
        System.out.println("Enter Note: ");
        String note = input.nextLine().trim();
        System.out.println("Enter Amount to Transfer: ");
        Double amount = Double.valueOf(input.nextLine().trim());
        bankService.transfer(accountNumber,accountNumber2,amount,note);
        System.out.println("Transfer successfully");
    }

    private static void statement(Scanner input, BankService bankService) {
        System.out.println("Enter Account Number: ");
        String accountNumber = input.nextLine().trim();
        bankService.statement(accountNumber);
    }

    private static void listAccounts(Scanner input,BankService bankService) {
        bankService.ListAccounts().forEach(a -> {
            System.out.println(a.getAccountNumber()+" | "+a.getAccountType()+" | "+" = "+a.getBalance());
        } );
    }

    private static void searchAccounts(Scanner input) {

    }

}
