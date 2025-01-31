package com.coderscampus.assignment13.service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepo;

    public Account saveAccount(Account account) {
        return accountRepo.save(account);
    }

    public Account findById(Long userId) {
        Optional<Account> userOpt = accountRepo.findById(userId);
        return userOpt.orElse(new Account());
    }
}
