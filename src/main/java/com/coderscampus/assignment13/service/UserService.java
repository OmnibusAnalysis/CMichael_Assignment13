package com.coderscampus.assignment13.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;
import com.coderscampus.assignment13.repository.AddressRepository;
import com.coderscampus.assignment13.repository.UserRepository;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private AddressRepository addressRepo;

    public Set<User> findAll() {
        return userRepo.findAllUsersWithAccountsAndAddresses();
    }

    public User findById(Long userId) {
        Optional<User> userOpt = userRepo.findById(userId);
        return userOpt.orElse(new User());
    }

    @Transactional
    public User saveUser(User user) {
        if (user.getUserId() == null) {
            // This is a new user
            if (user.getAddress() == null) {
                Address address = new Address();
                user.setAddress(address);
                address.setUser(user);
            } else {
                user.getAddress().setUser(user);
            }
        } else {
            // This is an existing user
            User existingUser = userRepo.findById(user.getUserId()).orElse(null);
            if (existingUser != null) {
                existingUser.setUsername(user.getUsername());
                if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
                    existingUser.setPassword(user.getPassword());
                }
                existingUser.setName(user.getName());

                // Update or create address
                if (existingUser.getAddress() == null) {
                    Address address = new Address();
                    existingUser.setAddress(address);
                    address.setUser(existingUser);
                }
                Address existingAddress = existingUser.getAddress();
                existingAddress.setAddressLine1(user.getAddress().getAddressLine1());
                existingAddress.setAddressLine2(user.getAddress().getAddressLine2());
                existingAddress.setCity(user.getAddress().getCity());
                existingAddress.setRegion(user.getAddress().getRegion());
                existingAddress.setCountry(user.getAddress().getCountry());
                existingAddress.setZipCode(user.getAddress().getZipCode());

                user = existingUser;
            }
        }

        return userRepo.save(user);
    }


@Transactional 
public void delete(Long userId) {
    User user = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    
  
    List<Account> accounts = user.getAccounts();
    accounts.forEach(account -> {
        boolean willBeEmpty = account.getUsers().size() <= 1;
        account.getUsers().remove(user);
        
        if (willBeEmpty) {
            accountRepo.delete(account);
        } else {
            accountRepo.save(account);
        }
    });
    
    // Clear user's account associations
    user.getAccounts().clear();

    // Delete associated address if exists
    Optional.ofNullable(user.getAddress())
            .ifPresent(address -> {
                user.setAddress(null);
                addressRepo.delete(address);
            });

    userRepo.delete(user);
        }
    }

