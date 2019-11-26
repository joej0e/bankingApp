package spring.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import spring.security.jwt.JwtUser;
import spring.service.AccountService;
import spring.service.OperationService;
import spring.service.UserService;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping(value = "/user/accounts/")
public class AccountRestController {

    private final UserService userService;

    private final AccountService accountService;

    private final OperationService operationService;

    private static final Double INITIAL_BALANCE = 0.;

    private static final String INCORRECT_ACCOUNT_ID = "You don't have account with provided id," +
            " please provide correct account id or create new account";

    @Autowired
    public AccountRestController(UserService userService, AccountService accountService, OperationService operationService) {
        this.userService = userService;
        this.accountService = accountService;
        this.operationService = operationService;
    }

    private User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) auth.getPrincipal();
        return userService.findByLogin(jwtUser.getUsername());
    }

    @GetMapping("")
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
    public ResponseEntity deposit(@RequestBody Double amount, @PathVariable Long account_id) {
        User loggedInUser = getLoggedInUser();
        Account account = loggedInUser.getAccounts().stream()
                .filter(a -> account_id.equals(a.getId()))
                .findAny()
                .orElse(null);
        if (account != null) {
            Account result = accountService.deposit(amount, account_id);
            return ResponseEntity.ok(result);
        }
        return new ResponseEntity<>(INCORRECT_ACCOUNT_ID, HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "{account_id}/withdraw")
    public ResponseEntity withdraw(@RequestBody Double amount, @PathVariable Long account_id) {
        User loggedInUser = getLoggedInUser();
        Account account = loggedInUser.getAccounts().stream()
                .filter(a -> account_id.equals(a.getId()))
                .findAny()
                .orElse(null);
        if (account != null) {
            try {
                Account result = accountService.withdraw(amount, account_id);
                return ResponseEntity.ok(result);
            } catch (InsufficientFundsException e) {
                return new ResponseEntity<>("You have insufficient funds in your account! Your balance is " +
                        account.getBalance(), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(INCORRECT_ACCOUNT_ID, HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "{account_id}/statement")
    public ResponseEntity getStatement(@PathVariable Long account_id) {
        User loggedInUser = getLoggedInUser();
        Account account = loggedInUser.getAccounts().stream()
                .filter(a -> account_id.equals(a.getId()))
                .findAny()
                .orElse(null);
        if (account != null) {
            List<Operation> result = operationService.getStatement(account_id);
            return ResponseEntity.ok(result);
        }
        return new ResponseEntity<>(INCORRECT_ACCOUNT_ID, HttpStatus.NOT_FOUND);
    }
}
