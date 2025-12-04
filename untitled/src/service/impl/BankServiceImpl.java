package service.impl;

import domain.Account;
import domain.Customer;
import domain.Transaction;
import domain.Type;
import repo.AccountRepo;
import repo.CustomerRepo;
import repo.TransactionRepo;
import service.BankService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {
    private final AccountRepo accountRepo= new AccountRepo();
    private final TransactionRepo transactionRepo= new TransactionRepo();
    private final CustomerRepo customerRepo= new CustomerRepo();
    @Override
    public String openAccount(String name, String email, String accountType) {
        String customerId = UUID.randomUUID().toString();
        String accountNumber = getAccountNumber();
        Account account=new Account(accountNumber,customerId,accountType, (double) 0);
        Customer customer=new Customer(customerId,name,email);
        accountRepo.save(account);
        customerRepo.save(customer);
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
        if(account.getBalance().compareTo(amount)<0){
            throw new RuntimeException("Insufficient balance");
        }
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
           System.out.println(t.toString());
       });
    }

    @Override
    public List<Account> searchAccounts(String customerName) {
         String query = customerName==null?"":customerName;
         List<Account> result = new ArrayList<>();
         for(Customer c: customerRepo.findAll())
         {
             if(c.getName().contains(query))
                 result.addAll(accountRepo.searchAccountsByCustomerId(c.getId()));
         }
         return result;
    }


    private String getAccountNumber() {
        int size= accountRepo.findAll().size() + 1;
        return String.format("AC%06d",size);
    }

}
