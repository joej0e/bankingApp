package spring.service.impl;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.entity.Account;
import spring.entity.Operation;
import spring.exception.InsufficientFundsException;
import spring.repository.AccountRepository;
import spring.repository.OperationRepository;
import spring.service.AccountService;
import spring.service.OperationService;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final OperationService operationService;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, OperationRepository transactionRepository, OperationService operationService) {
        this.accountRepository = accountRepository;
        this.operationService = operationService;
    }


    @Override
    public List<Account> findAllByUserId(Long userId) {
        return accountRepository.findAllByUserId(userId);
    }

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account withdraw(Double amount, Long accountId) throws InsufficientFundsException {
        Account account = accountRepository.findById(accountId).orElseThrow();
        Double currentBalance = account.getBalance();
        if(currentBalance < amount) {
            throw new InsufficientFundsException("Insufficient funds in your account!");
        }
        Double newBalance = currentBalance - amount;
        account.setBalance(newBalance);
        Account result = accountRepository.save(account);
        Date date = new Date();
        Operation operation = new Operation(account, -amount, newBalance, date);
        operationService.save(operation);
        return result;
    }

    @Override
    public Account deposit(Double amount, Long account_id) {
        Account account = accountRepository.findById(account_id).orElseThrow();
        Double currentBalance = account.getBalance();
        Double newBalance = currentBalance + amount;
        account.setBalance(newBalance);
        Account result = accountRepository.save(account);
        Date date = new Date();
        Operation operation = new Operation(account, -amount, newBalance, date);
        operationService.save(operation);
        return result;
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow();
    }
}
