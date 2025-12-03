package repo;

import domain.Account;
import domain.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionRepo {
    Map<String, List<Transaction>> txtByAccount = new HashMap<>();

    public void add(Transaction transaction) {
        txtByAccount.computeIfAbsent(transaction.getAccountNumber(),k -> new ArrayList<Transaction>()).add(transaction);
    }
}
