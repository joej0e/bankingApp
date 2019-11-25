package spring.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.service.AccountService;
import spring.service.OperationService;
import spring.service.UserService;

@RestController
@RequestMapping(value = "/user/")
public class UserRestController {

    private final UserService userService;

    private final AccountService accountService;

    private final OperationService operationService;

    private final Double INITIAL_BALANCE = 0.;

    @Autowired
    public UserRestController(UserService userService, AccountService accountService, OperationService operationService) {
        this.userService = userService;
        this.accountService = accountService;
        this.operationService = operationService;
    }

}
