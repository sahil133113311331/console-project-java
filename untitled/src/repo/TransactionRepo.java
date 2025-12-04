package repo;

import domain.Account;
import domain.Transaction;

import java.util.*;

public class TransactionRepo {
    Map<String, List<Transaction>> txtByAccount = new HashMap<>();

    public void add(Transaction transaction) {
        txtByAccount.computeIfAbsent(transaction.getAccountNumber(),k -> new ArrayList<Transaction>()).add(transaction);
    }

    public Optional<List<Transaction>> allTransection(String accountNumber) {
       return Optional.ofNullable(txtByAccount.get(accountNumber));
    }

}
