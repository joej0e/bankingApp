package spring.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.entity.Account;
import spring.entity.Operation;
import spring.entity.User;
import spring.exception.InsufficientFundsException;
import spring.service.AccountService;
import spring.service.OperationService;
import spring.service.UserService;

import java.sql.Statement;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping(value = "/user/accounts/")
public class AccountRestController {

    private final UserService userService;

    private final AccountService accountService;

    private final OperationService operationService;

    private final Double INITIAL_BALANCE = 0.;

    @Autowired
    public AccountRestController(UserService userService, AccountService accountService, OperationService operationService) {
        this.userService = userService;
        this.accountService = accountService;
        this.operationService = operationService;
    }

    private User getLoggedInUser() {
        String login = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByLogin(login);
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAccounts() {
        User loggedInUser = getLoggedInUser();
        List<Account> result = accountService.findAllByUserId(loggedInUser.getId());
        return ResponseEntity.ok(result);
    }


    @PostMapping(value = "create_account")
    public ResponseEntity<Account> createAccount() {
        User loggedInUser = getLoggedInUser();
        Account account = new Account();
        account.setBalance(INITIAL_BALANCE);
        account.setUser(loggedInUser);
        account.setOperations(Collections.emptyList());
        Account result = accountService.createAccount(account);
        return ResponseEntity.ok(result);
    }

    @PutMapping(value = "{account_id}/deposit")
    public ResponseEntity<Account> deposit(@RequestBody Double amount, @PathVariable Long account_id) {
        User loggedInUser = getLoggedInUser();
        Account account = accountService.findById(account_id);
        if(loggedInUser.getAccounts().contains(account)) {
            Account result = accountService.deposit(amount, account_id);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(value = "{account_id}/withdraw")
    public ResponseEntity<Account> withdraw(@RequestBody Double amount, @PathVariable Long account_id) throws InsufficientFundsException {
        User loggedInUser = getLoggedInUser();
        Account account = accountService.findById(account_id);
        if(loggedInUser.getAccounts().contains(account)) {
            Account result = accountService.withdraw(amount, account_id);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "{account_id}/statement")
    public ResponseEntity<List<Operation>> getStatement(@PathVariable Long account_id) {
        User loggedInUser = getLoggedInUser();
        Account account = accountService.findById(account_id);
        if(loggedInUser.getAccounts().contains(account)) {
            List<Operation> result = operationService.getStatement(account_id);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }
}
