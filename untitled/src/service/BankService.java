package service;

import domain.Account;

import java.util.List;

public interface BankService {
    String openAccount(String name,String email,String accountType);
    List<Account> ListAccounts();

    void deposit(String accountNumber, Double amount, String note);

    void withdraw(String accountNumber, Double amount, String note);

    void transfer(String accountNumber, String accountNumber2, Double amount, String note);

    Double balance(String accountNumber);

    void statement(String accountNumber);
}
