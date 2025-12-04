package service.impl;

import domain.Account;
import domain.Transaction;
import domain.Type;
import repo.AccountRepo;
import repo.TransactionRepo;
import service.BankService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {
    private final AccountRepo accountRepo= new AccountRepo();
    private final TransactionRepo transactionRepo= new TransactionRepo();
    @Override
    public String openAccount(String name, String email, String accountType) {
        String customerId = UUID.randomUUID().toString();
        String accountNumber = getAccountNumber();
        Account account=new Account(accountNumber,customerId,accountType, (double) 0);
        accountRepo.save(account);
        return accountNumber;
    }

    @Override
    public List<Account> ListAccounts() {
        return accountRepo.findAll().stream().sorted(Comparator.comparing(Account::getAccountNumber)).collect(Collectors.toList());
    }

    @Override
    public void deposit(String accountNumber, Double amount,String note) {
         Account account = accountRepo.findByNumber(accountNumber).orElseThrow(()->{
            return new RuntimeException("Account number not found"+accountNumber);
         });
         account.setBalance(account.getBalance()+amount);
         Transaction transaction = new Transaction(UUID.randomUUID().toString(),accountNumber,amount, LocalDateTime.now(),note, Type.DEPOSIT);
         transactionRepo.add(transaction);
    }

    @Override
    public void withdraw(String accountNumber, Double amount, String note) {
        Account account = accountRepo.findByNumber(accountNumber).orElseThrow(()->{
            return new RuntimeException("Account number not found"+accountNumber);
        });
        if(account.getBalance().compareTo(amount)<0){
            throw new RuntimeException("Insufficient balance");
        }
        account.setBalance(account.getBalance()-amount);
        Transaction transaction = new Transaction(UUID.randomUUID().toString(),accountNumber,amount, LocalDateTime.now(),note, Type.WITHDRAW);
        transactionRepo.add(transaction);
    }

    @Override
    public void transfer(String accountNumber, String accountNumber2, Double amount, String note) {
        Account account = accountRepo.findByNumber(accountNumber).orElseThrow(()->{
            return new RuntimeException("Account number not found"+accountNumber);
        });
        Account account2 = accountRepo.findByNumber(accountNumber2).orElseThrow(()->{
            return new RuntimeException("Account number not found"+accountNumber);
        });
        account.setBalance(account.getBalance()-amount);
        account2.setBalance(account2.getBalance()+amount);
        Transaction transaction = new Transaction(UUID.randomUUID().toString(),accountNumber,amount, LocalDateTime.now(),note, Type.TRANSFER_OUT);
        transactionRepo.add(transaction);
        Transaction transaction2 = new Transaction(UUID.randomUUID().toString(),accountNumber2,amount, LocalDateTime.now(),note, Type.TRANSFER_IN);
        transactionRepo.add(transaction2);
    }

    @Override
    public Double balance(String accountNumber) {
        Account account = accountRepo.findByNumber(accountNumber).orElseThrow(()-> new RuntimeException("Account number not found"+accountNumber));
        return account.getBalance();
    }

    @Override
    public void statement(String accountNumber) {
       List<Transaction> transactions= transactionRepo.allTransection(accountNumber).orElseThrow(()-> new RuntimeException("Account number not found"+accountNumber));
       transactions.forEach((t)->{
           t.toString();
       });
    }

    private String getAccountNumber() {
        int size= accountRepo.findAll().size() + 1;
        return String.format("AC%06d",size);
    }

}
