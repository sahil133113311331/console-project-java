package repo;

import domain.Account;

import java.util.*;

public class AccountRepo {
    private final Map<String,Account> accountsByNumber=new HashMap<String,Account>();

    public Optional<Account> findByNumber(String accountNumber) {
       return Optional.ofNullable(accountsByNumber.get(accountNumber));
    }

    public void save(Account account){
        accountsByNumber.put(account.getAccountNumber(),account);
    }
    public List<Account> findAll(){
        return new ArrayList<Account>(accountsByNumber.values());
    }
}
