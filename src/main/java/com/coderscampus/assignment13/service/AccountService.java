package com.coderscampus.assignment13.service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;
import com.coderscampus.assignment13.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepo;

    public Account saveAccount(Long userId) {
        User user = userService.findById(userId);
        Account account = new Account();
        user.getAccounts().add(account);
        account.getUsers().add(user);
        account.setAccountName("Account: " + user.getAccounts().size());
        return accountRepo.save(account);
    }

    public Account findById(Long accountId) {
        Optional<Account> account = accountRepo.findById(accountId);
        return account.orElse(new Account());
    }

    public void saveAccount(Account account) {
        accountRepo.save(account);
    }
}
