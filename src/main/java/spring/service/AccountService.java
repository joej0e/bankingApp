package spring.service;

import spring.entity.Account;
import spring.exception.InsufficientFundsException;

import java.util.List;

public interface AccountService {
    Account withdraw(Double amount, Long accountId) throws InsufficientFundsException;

    Account deposit(Double amount, Long accountId);

    List<Account> findAllByUserId(Long userId);

    Account createAccount(Account account);

    Account findById(Long id);
}
